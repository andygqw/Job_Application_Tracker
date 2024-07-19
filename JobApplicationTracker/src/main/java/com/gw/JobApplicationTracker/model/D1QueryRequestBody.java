package com.gw.JobApplicationTracker.model;

import java.util.ArrayList;
import java.util.List;


public class D1QueryRequestBody {

    private List<String> params;
    private String query;

    public D1QueryRequestBody() {

        params = new ArrayList<>();
        query = "";
    }

    public D1QueryRequestBody(String query) {

        params = new ArrayList<>();
        this.query = query;
    }

    public void setParams(List<String> params){

        this.params = params;
    }

    public void setQuery(String query){

        this.query = query;
    }

    public List<String> getParams(){
        return params;
    }

    public String getQuery(){
        return query;
    }
}