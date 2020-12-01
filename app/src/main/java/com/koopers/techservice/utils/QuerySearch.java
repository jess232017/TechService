package com.koopers.techservice.utils;

public class QuerySearch {
    private final String message;
    private boolean show;

    public QuerySearch(String message, boolean show) {
        this.message = message;
        this.show = show;
    }

    public String getMessage() {
        return message;
    }

    public boolean getBoolean() {
        return show;
    }
}
