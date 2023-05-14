package com.sd.service;

import com.sd.entity.User;
import com.sd.exception.UserNotFoundException;
import com.sd.repository.UserRepository;
import com.sd.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.constant.Constable;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User findByUserName(String userName){
        log.info("[findByUserName] Fetching user based on userName: {}", userName);
        User user = userRepository.findByUserName(userName).orElse(null);
        if(Objects.isNull(user)){
            log.error("[findByUserName] User not found with userName: {}", userName);
            throw new UserNotFoundException(Constants.Errors.USER_NOT_FOUND_EXCEPTION.getErrorMessage());
        }
        else {
            return user;
        }
    }
    public Boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
