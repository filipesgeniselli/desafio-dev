package com.filipegeniselli.desafiodev.exception;

import java.io.Serializable;

public class NotFoundException extends RuntimeException implements Serializable {

    public static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
