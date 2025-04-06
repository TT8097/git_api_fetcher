package com.example.demo.models;

import java.util.List;

public class ResponseModel {
    public List<BranchModel> branches;
    public String repositoryName;
    public String ownerLogin;


    public ResponseModel(RepositoryModel repositoryModel,List<BranchModel> branches) {
        this.repositoryName = repositoryModel.name;
        this.ownerLogin = repositoryModel.owner.login;
        this.branches = branches;
    }
}
