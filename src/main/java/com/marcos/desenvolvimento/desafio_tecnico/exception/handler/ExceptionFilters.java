package com.marcos.desenvolvimento.desafio_tecnico.exception.handler;

import java.time.LocalDateTime;

public class ExceptionFilters {

    private String title;
    private Integer status;
    private String details;
    private LocalDateTime timestamp;
    private String devMsg;

    private ExceptionFilters(Builder builder) {
        this.title = builder.title;
        this.status = builder.status;
        this.details = builder.details;
        this.timestamp = builder.timestamp;
        this.devMsg = builder.devMsg;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDevMsg() {
        return devMsg;
    }

    public static class Builder {
        private String title;
        private Integer status;
        private String details;
        private LocalDateTime timestamp;
        private String devMsg;

        public Builder() {
            this.timestamp = LocalDateTime.now();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder devMsg(String devMsg) {
            this.devMsg = devMsg;
            return this;
        }

        public ExceptionFilters build() {
            return new ExceptionFilters(this);
        }
    }
}
