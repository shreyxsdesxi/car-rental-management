package com.sd.controller;

import com.sd.entity.dto.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/car")
public class CarController {
    @GetMapping
    public ResponseEntity<String> testFetchAllCars(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails loggedInUser = (CustomUserDetails)authentication.getPrincipal();
        String response = "Fetch All Cars Endpoint Triggered for User: " + loggedInUser.getUsername();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
