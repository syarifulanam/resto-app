package com.ngulik.resto_app.exception.error;

public class TokenBlacklistException extends RuntimeException{

    public TokenBlacklistException(String message) {
        super(message);
    }
}
