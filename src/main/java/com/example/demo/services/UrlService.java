package com.example.demo.services;

public class UrlService {
    public String urlRepository(String name){
        String baseUrl = "https://api.github.com/users/";
        return baseUrl + name + "/repos";
    }
    public String urlBranches(String url){
        return url.replace("{/branch}","");
    }
}
