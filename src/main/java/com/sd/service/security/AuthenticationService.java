package com.sd.service.security;

import com.sd.entity.User;
import com.sd.entity.dto.AuthResponse;
import com.sd.entity.dto.AuthenticationRequest;
import com.sd.entity.dto.RegisterationRequest;
import com.sd.entity.dto.RegisterationResponse;
import com.sd.exception.AuthenticationException;
import com.sd.exception.UserRegisterationException;
import com.sd.service.UserService;
import com.sd.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    public RegisterationResponse register(RegisterationRequest registerationRequest){
        if (userService.existsByUserName(registerationRequest.getUserName())){
                throw new UserRegisterationException("User with same user name already exists. Try with different username");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = User.builder().userName(registerationRequest.getUserName())
                    .password(passwordEncoder.encode(registerationRequest.getPassword()))
                    .city(registerationRequest.getCity())
                    .contact(registerationRequest.getContact()).build();
        RegisterationResponse registerationResponse = new RegisterationResponse();
        try {
            userService.saveUser(user);
            registerationResponse.setIsRegisteredSuccessfully(Boolean.TRUE);
        }catch (Exception e){
            log.error("[register] Error occurred: ", e);
            throw new UserRegisterationException(e.getMessage());
        }
        return registerationResponse;
    }

    public AuthResponse authenticate(AuthenticationRequest authenticationRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        if (!authentication.isAuthenticated()){
            throw new AuthenticationException("Unauthenticated user");
        }
        String jwtToken = tokenUtils.generateJWT(authentication);
        AuthResponse authResponse = AuthResponse.builder().authToken(jwtToken).build();
        return authResponse;
    }
}
