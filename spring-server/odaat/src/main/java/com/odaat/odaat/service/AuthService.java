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

    public void initToken(Integer localId, AccessToken token){
        backlogAccess.put(localId, token);
    }

    public void addToken(Integer localId, String externalId, String access, String refresh, Instant expiry){
        backlogAccess.put(localId, new AccessToken(externalId, access, refresh, expiry));
    }

    public void removeToken(){
        backlogAccess.remove(getCurrentUserId());
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
