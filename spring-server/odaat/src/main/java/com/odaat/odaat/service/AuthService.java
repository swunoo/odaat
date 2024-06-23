package com.odaat.odaat.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.BacklogAuth;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Uzer;

@Service
public class AuthService {

    @Autowired
    private UzerService uzerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProjectService projectService;

    // BacklogAuth data for each user
    Map<String, BacklogAuth> backlogAccess;

    public AuthService(){
        backlogAccess = new HashMap<>();
    }

    public Uzer getCurrentUser() {

        String currUserId = getCurrentUserId();

        Optional<Uzer> uzer = uzerService.findById(currUserId);
        if(uzer.isPresent()) return uzer.get();

        return createNewUzer(currUserId);
    }

    public String getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String originalName = auth.getName();
        String cleanedName = originalName.replaceAll("\\|", "_"); // Cleaned special characters
        return cleanedName;
    }

    private Uzer createNewUzer(String id){
        // Create a new uzer based on user id
        Uzer newUzer = new Uzer();
        newUzer.setId(id);
        uzerService.save(newUzer);

        // Add a default category
        Category newCategory = categoryService.saveDefault(newUzer);

        // Add a default project
        projectService.saveDefault(newUzer, newCategory);

        return newUzer;
    }

    public void initToken(String userId, BacklogAuth token){
        backlogAccess.put(userId, token);
    }

    public void addToken(String userId, String backlogUserId, String access, String refresh, Instant expiry){
        backlogAccess.put(userId, new BacklogAuth(backlogUserId, access, refresh, expiry));
    }

    public void removeToken(){
        backlogAccess.remove(getCurrentUserId());
    }

    public BacklogAuth getTokenObject(){
        return backlogAccess.get(getCurrentUserId());
    }

}
