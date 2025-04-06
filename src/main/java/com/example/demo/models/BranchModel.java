package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchModel {
    @JsonProperty("name")
    public String name;
    @JsonProperty("commit")
    public CommitModel commit;

    @Override
    public String toString() {
        return "name: "+name+"\n"+"Last commit ssh: "+commit.sha;
    }
}
