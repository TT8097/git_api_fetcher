package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryModel {
    @JsonProperty("name")
   public String name;
    @JsonProperty("owner")
    public OwnerModel owner;
    @JsonProperty("fork")
    public Boolean fork;
    @JsonProperty("branches_url")
    public String branches_url;

    @Override
    public String toString() {
        return  "Repository Name : "+name+"\n"+"Owner Login"+owner.login+"\n"+"Branches:";
    }
}
