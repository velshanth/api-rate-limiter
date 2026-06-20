package org.guvi.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "api_log")
public class ApiLog {

    private String id;
    private String endpoint;
    private String method;
    private String clientIP;
    private Integer statusCode;
    private Long responseTime;
    private Instant timeStamp;
    private Boolean rateLimited;

    public ApiLog(String id, String endpoint, String method, String clientIP, Integer statusCode, Long responseTime, Instant timeStamp, Boolean rateLimited) {
        this.id = id;
        this.endpoint = endpoint;
        this.method = method;
        this.clientIP = clientIP;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
        this.timeStamp = timeStamp;
        this.rateLimited = rateLimited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getRateLimited() {
        return rateLimited;
    }

    public void setRateLimited(Boolean rateLimited) {
        this.rateLimited = rateLimited;
    }
}
