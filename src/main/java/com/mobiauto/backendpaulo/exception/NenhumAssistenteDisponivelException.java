package com.mobiauto.backendpaulo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NenhumAssistenteDisponivelException extends RuntimeException{
    public NenhumAssistenteDisponivelException(String message) {
        super(message);
    }



}
