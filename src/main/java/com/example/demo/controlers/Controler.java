package com.example.demo.controlers;

import com.example.demo.models.NotFoundResponseModel;
import com.example.demo.services.GitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequestMapping("/test")
public class Controler {
    GitService gitService;

    public Controler(GitService gitService) {
        this.gitService = gitService;
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<?> getInfoRepository(@PathVariable("name") String name)  {
        return gitService.response(name);
    }
}
