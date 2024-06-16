package com.odaat.odaat.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccessToken {

    String userId;
    String token;
    String refreshToken;
    Instant expiresAt;

    public boolean isValid(){
        return this.expiresAt.isAfter(Instant.now());
    }

    public void refreshData(String token, String refreshToken, Instant expiresAt){
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }
}
