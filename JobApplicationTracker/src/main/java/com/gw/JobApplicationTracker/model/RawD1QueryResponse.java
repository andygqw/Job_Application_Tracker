package com.gw.JobApplicationTracker.model;

import java.util.ArrayList;
import java.util.List;

public class RawD1QueryResponse {

    private List<String> errors;
    private List<String> messages;
    private List<Result> result;
    private boolean success;

    public RawD1QueryResponse() {

        errors = new ArrayList<>();
        messages = new ArrayList<>();
        result = new ArrayList<>();
        success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Result> getResult() {
        return result;
    }
}