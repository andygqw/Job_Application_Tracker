package com.gw.JobApplicationTracker.model;

import java.util.ArrayList;
import java.util.List;

public class Results {

    private List<String> columns;
    private List<List<Object>> rows;

    public Results() {

        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    //getters
    public List<String> getColumns() {
        return columns;
    }

    public List<List<Object>> getRows() {
        return rows;
    }
}