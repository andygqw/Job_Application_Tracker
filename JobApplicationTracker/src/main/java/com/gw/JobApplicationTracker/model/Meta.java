package com.gw.JobApplicationTracker.model;


public class Meta {
    
    private boolean changed_db;
    private int changes;
    private int duration;
    private int last_row_id;
    private int rows_read;
    private int rows_written;
    private int size_after;

    public Meta() {

        changed_db = false;
        changes = 0;
        duration = 0;
        last_row_id = 0;
        rows_read = 0;
        rows_written = 0;
        size_after = 0;
    }

    // Getters and Setters
}