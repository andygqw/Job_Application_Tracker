package com.gw.JobApplicationTracker.model;



public class Result {

    private Meta meta;
    private Results results;
    private boolean success;


    public Result() {

        meta = new Meta();
        results = new Results();
        success = false;
    }


    // Getters and Setters

    public Results getResults() {
        return results;
    }

}