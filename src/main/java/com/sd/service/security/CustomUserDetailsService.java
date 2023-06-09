package com.sd.service.security;

import com.sd.entity.User;
import com.sd.entity.dto.CustomUserDetails;
import com.sd.repository.UserRepository;
import com.sd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return userDetails;
    }
}
