package com.sd.controller;

import com.sd.entity.dto.*;
import com.sd.service.security.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<RegisterationResponse> registerUser(@RequestBody RegisterationRequest registerationRequest){
        RegisterationResponse registerationResponse = authenticationService.register(registerationRequest);
        return new ResponseEntity<>(registerationResponse, HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        AuthResponse authResponse = authenticationService.authenticate(authenticationRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
