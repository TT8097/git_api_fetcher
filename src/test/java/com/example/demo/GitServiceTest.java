package com.example.demo;

import com.example.demo.models.BranchModel;
import com.example.demo.models.RepositoryModel;
import com.example.demo.services.GitService;
import com.example.demo.services.UrlService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitServiceTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UrlService urlService;
    @InjectMocks
    private GitService gitService;


    @Test
    void testGetRepositories_success() throws Exception {

        String json = "[{\"name\":\"repo1\"}, {\"name\":\"repo2\"}]";
        RepositoryModel repositoryModel=new RepositoryModel();
        repositoryModel.name="repo1";
        RepositoryModel repositoryModel2=new RepositoryModel();
        repositoryModel2.name="repo2";
        List<RepositoryModel> mockRepos = List.of(
                repositoryModel,
                repositoryModel2
        );

        when(urlService.urlRepository(anyString())).thenReturn("http://fake.url");
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(json);
        when(objectMapper.readValue(eq(json), any(TypeReference.class)))
                .thenReturn(mockRepos);


        List<RepositoryModel> result = gitService.getRepositories("");


        assertEquals(2, result.size());
        assertEquals("repo1", result.get(0).name);
        assertEquals("repo2", result.get(1).name);
    }

    @Test
    void testGetRepositories_throwsIllegalArgumentException_on404() throws Exception {
        when(urlService.urlRepository(anyString())).thenReturn("http://fake.url");
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);

        assertThrows(IllegalArgumentException.class, () -> gitService.getRepositories(""));
    }
    @Test
    void testGetBranches_shouldReturnBranchList() throws Exception {
        String url = "https://api.github.com/repos/user/repo/branches{/branch}";
        String cleanUrl = "https://api.github.com/repos/user/repo/branches";
        String json = "[{\"name\":\"main\"}, {\"name\":\"dev\"}]";

        BranchModel b1 = new BranchModel();
        b1.name = "main";
        BranchModel b2 = new BranchModel();
        b2.name = "dev";

        List<BranchModel> mockBranches = List.of(b1, b2);
        when(urlService.urlBranches(anyString())).thenReturn("http://fake.url");
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(json);
        when(objectMapper.readValue(eq(json), any(TypeReference.class)))
                .thenReturn(mockBranches);;

        List<BranchModel> result = gitService.getBranches(url);

        assertEquals(2, result.size());
        assertEquals("main", result.get(0).name);
        assertEquals("dev", result.get(1).name);
    }
}
