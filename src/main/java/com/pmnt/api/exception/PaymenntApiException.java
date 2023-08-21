package com.pmnt.api.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class PaymenntApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public PaymenntApiException(String message){
        super(message);
        this.message = message;
    }

    public PaymenntApiException(HttpStatus status, String message){
        super(message);
        this.message = message;
        this.status=status;
    }
}
