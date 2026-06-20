package org.guvi.dto;

public class LogInResponse {
    private String message;
    private String token;
    private String email;

    public LogInResponse(String message, String email, String token) {
        this.message = message;
        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
