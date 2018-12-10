package com.dans.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/hello")
    public String authenticateUser() {
        return "hi from your service finder platform";
    }
}
