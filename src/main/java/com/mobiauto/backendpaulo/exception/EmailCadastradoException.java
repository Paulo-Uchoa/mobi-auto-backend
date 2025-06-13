package com.mobiauto.backendpaulo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailCadastradoException extends RuntimeException{
    public EmailCadastradoException(String message) {
        super(message);
    }



}
