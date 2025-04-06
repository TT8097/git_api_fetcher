package com.example.demo.services;

import com.example.demo.models.BranchModel;
import com.example.demo.models.NotFoundResponseModel;
import com.example.demo.models.RepositoryModel;
import com.example.demo.models.ResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class GitService {
    HttpClient httpClient;
    ObjectMapper maper;
    UrlService urlService;
    @Autowired
    public GitService(HttpClient httpClient, ObjectMapper maper ,UrlService urlService) {
        this.httpClient = httpClient;
        this.maper = maper;
        this.urlService=urlService;
    }

    public List<RepositoryModel> getRepositories(String name ) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlService.urlRepository(name))).build();
        HttpResponse<String> responseEntity =httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(responseEntity.statusCode()==404){
            throw new IllegalArgumentException();
        }

        return maper.readValue(responseEntity.body(),new TypeReference<>() {});

    }
    public List<RepositoryModel> filterRepositories(List<RepositoryModel> repositories){
        return repositories.stream().filter(x-> !x.fork).toList();
    }
   public List<BranchModel> getBranches(String url) throws IOException, InterruptedException {
        url= urlService.urlBranches(url);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> responseEntity = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return maper.readValue(responseEntity.body(),new TypeReference<>() {});
    }
    public ResponseEntity<?> response(String name)  {
        try (ExecutorService executor =  Executors.newVirtualThreadPerTaskExecutor()){

            List<RepositoryModel> repositories = getRepositories(name);
            repositories = filterRepositories(repositories);
            List<Callable<ResponseModel>> tasks = new ArrayList<>();

            for (RepositoryModel repository : repositories) {
                tasks.add(() -> {
                    return new ResponseModel(repository, getBranches(repository.branches_url));
                });
            }

            List<Future<ResponseModel>> futures = executor.invokeAll(tasks);

            List<ResponseModel> responses = new ArrayList<>();
            for (Future<ResponseModel> future : futures) {
                responses.add(future.get());
            }

            return ResponseEntity.status(HttpStatus.OK).body(responses);
        }catch (IllegalArgumentException e){

            return ResponseEntity.status(404).body(new NotFoundResponseModel());

        }catch (IOException | InterruptedException | ExecutionException e) {

            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build();

        }
    }

}
