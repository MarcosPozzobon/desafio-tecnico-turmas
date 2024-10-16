package com.marcos.desenvolvimento.desafio_tecnico.exception;

import java.time.LocalDateTime;

public class StandardError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    private StandardError(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public Builder() {
            this.timestamp = LocalDateTime.now();
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public StandardError build() {
            return new StandardError(this);
        }
    }
}

