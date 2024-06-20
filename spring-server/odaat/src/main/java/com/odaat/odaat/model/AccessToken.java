package com.odaat.odaat.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessToken {

    String userId;
    String token;
    String refreshToken;
    Instant expiresAt;

    public boolean isExpired(){
        return this.expiresAt == null || this.expiresAt.isBefore(Instant.now());
    }

    public void refreshData(String token, String refreshToken, Instant expiresAt){
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }
}
