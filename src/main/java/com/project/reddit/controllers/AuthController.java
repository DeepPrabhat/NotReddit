package com.project.reddit.controllers;


import com.project.reddit.dto.RegisterRequest;
import com.project.reddit.models.User;
import com.project.reddit.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class AuthController {


    @Autowired
    private AuthService authService;


    @PreAuthorize("hasAuthority('NORMAL')")
    @GetMapping("/normal")
    public ResponseEntity<String> normalUser(){
        return ResponseEntity.ok("Yes, I am a Normal User");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminUser(){
        return ResponseEntity.ok("Yes, I am a admin User");
    }

    @GetMapping("/public")
    public ResponseEntity<String> User(){
        return ResponseEntity.ok("Yes, I am a public User");
    }

    @PostMapping("/signup")
    public void signup(@RequestBody RegisterRequest registerRequest)
    {

    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user)
    {
        return authService.addUser(user);
    }

}
