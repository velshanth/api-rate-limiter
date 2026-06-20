package org.guvi.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rate_limits")
public class RateLimitBucket {

    @NotNull(message = "Tokens cannot be null")
    private Integer tokens;

    @NotNull(message = "Last refill timestamp cannot be null")
    private Long lastRefillTimeStamp;

    public RateLimitBucket(){

    }

    public RateLimitBucket(int tokens, long lastRefillTimeStamp) {
        this.lastRefillTimeStamp = lastRefillTimeStamp;
        this.tokens = tokens;
    }

    public long getLastRefillTimeStamp() {
        return lastRefillTimeStamp;
    }

    public void setLastRefillTimeStamp(long lastRefillTimeStamp) {
        this.lastRefillTimeStamp = lastRefillTimeStamp;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}
