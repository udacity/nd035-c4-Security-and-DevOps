package com.example.demo.exception;

import java.time.LocalDate;

public class ErrorMessage {
    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, LocalDate timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
