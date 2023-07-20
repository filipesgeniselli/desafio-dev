package com.filipegeniselli.desafiodev.exception;

import java.io.Serializable;

public class StorageException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Exception exception) {
        super(message, exception);
    }
}
