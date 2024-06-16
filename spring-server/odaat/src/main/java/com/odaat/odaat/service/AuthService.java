package com.odaat.odaat.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.odaat.odaat.model.AccessToken;
import com.odaat.odaat.model.Uzer;

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

    public Uzer getCurrentUser() {
        Uzer mockUzer = new Uzer();
        mockUzer.setId(1);
        mockUzer.setName("Test User");
        return mockUzer;
    }

}