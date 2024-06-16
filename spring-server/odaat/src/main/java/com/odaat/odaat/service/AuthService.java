package com.odaat.odaat.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.odaat.odaat.model.AccessToken;

@Service
public class AuthService {

    // Backlog tokens
    Map<Integer, AccessToken> backlogAccess;
    

    public AuthService(){
        backlogAccess = new HashMap<>();
    }

    public Integer getCurrentUserId(){
        return 1;
    }

    public void addToken(String userId, String access, String refresh, Instant expiry){
        backlogAccess.put(getCurrentUserId(), new AccessToken(userId, access, refresh, expiry));
    }

    public AccessToken getTokenObject(){
        return backlogAccess.get(getCurrentUserId());
    }

}
