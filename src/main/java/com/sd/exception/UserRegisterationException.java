package com.sd.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterationException extends RuntimeException{
    public UserRegisterationException(String message) {
        super(message);
    }
}
