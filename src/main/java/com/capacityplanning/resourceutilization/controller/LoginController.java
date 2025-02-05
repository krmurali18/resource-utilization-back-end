package com.capacityplanning.resourceutilization.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class LoginController {
    @GetMapping("/login")
    public String login(Authentication authentication) {
        System.out.println("User" + authentication.getName()
            + " has logged in");
        return "Welcome, " + authentication.getName();
    }
}
