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
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

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
    RestTemplate httpClient;
    ObjectMapper maper;
    UrlService urlService;
    @Autowired
    public GitService(RestTemplate httpClient, ObjectMapper maper ,UrlService urlService) {
        this.httpClient = httpClient;
        this.maper = maper;
        this.urlService=urlService;
    }

    public List<RepositoryModel> getRepositories(String name ) throws IOException, InterruptedException {

        ResponseEntity<List<RepositoryModel>> responseEntity = httpClient.exchange(URI.create(urlService.urlRepository(name)), HttpMethod.GET, null, new ParameterizedTypeReference<List<RepositoryModel>>() {});

        return responseEntity.getBody();

    }
    public List<RepositoryModel> filterRepositories(List<RepositoryModel> repositories){
        return repositories.stream().filter(x-> !x.fork).toList();
    }
   public List<BranchModel> getBranches(String url) throws IOException, InterruptedException {
        ResponseEntity<List<BranchModel>> responseEntity = httpClient.exchange(urlService.urlBranches(url), HttpMethod.GET, null, new ParameterizedTypeReference<List<BranchModel>>() {});
        return responseEntity.getBody();
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
        }catch (HttpClientErrorException.NotFound e) {

            System.out.println("Błąd: Repozytorium użytkownika " + name + " nie zostało znalezione.");

            return ResponseEntity.status(404).body(new NotFoundResponseModel());
        } catch (Exception e) {

            System.out.println("Błąd : " + " - " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

}
