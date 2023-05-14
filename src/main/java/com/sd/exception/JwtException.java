package com.sd.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
