package com.StanleyDavid.flashchatnewfirebase;

/**
 * Created by StevenSeagul on 11/30/2017.
 */

public class InstantMessage {
    private String message;
    private String author;

    public InstantMessage(String message, String author){
        this.message = message;
        this.author = author;
    }

    public InstantMessage(){}

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
