package com.example.poc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException {

    private final HttpStatus code;
    private final String message;

    public GenericException(HttpStatus code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }
    public HttpStatus getStatus() {
        return code;
    }
}
