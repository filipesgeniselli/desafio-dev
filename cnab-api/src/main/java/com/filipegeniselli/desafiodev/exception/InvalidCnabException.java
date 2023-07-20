package com.filipegeniselli.desafiodev.exception;

import java.io.Serializable;

public class InvalidCnabException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public InvalidCnabException(String message) {
        super(message);
    }
}
