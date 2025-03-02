package com.delivery.product.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
public class CustomeException extends RuntimeException implements Serializable {

    private static final long serialVersionId = 1L;

    private final String message;
    @Getter
    private final HttpStatus httpStatus;
    private final String exceptionMessage;
    private final transient Object[] messageArgs;

    public CustomeException(String message, HttpStatus httpStatus, String exceptionMessage, Object[] messageArgs) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.exceptionMessage = exceptionMessage;
        this.messageArgs = messageArgs;
    }

}
