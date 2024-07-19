package com.gw.JobApplicationTracker.model;


public class D1QueryRequestBody {

    private String[] params;
    private String query;

    public D1QueryRequestBody() {

        params = new String[0];
        query = "";
    }

    public D1QueryRequestBody(String query) {

        params = new String[0];
        this.query = query;
    }

    public void setQuery(String query){

        this.query = query;
    }
}