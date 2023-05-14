package com.sd.util;

public class Constants {
    public static Long JWT_EXPIRY_TIME = 1_800_000L;
    public static String JWT_TOKEN_PREFIX = "Bearer ";
    public enum Errors {
        USER_NOT_FOUND_EXCEPTION("User Not Found"),
        AUTHORIZATION_HEADER_NOT_FOUND("Authorization Header Not Present"),
        BEARER_TOKEN_NOT_PRESENT("Bearer Token Not Present");
        public String errorMessage;

        Errors(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        public String getErrorMessage(){
            return this.errorMessage;
        }
    }
}
