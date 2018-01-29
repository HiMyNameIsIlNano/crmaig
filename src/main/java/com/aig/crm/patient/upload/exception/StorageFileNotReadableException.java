package com.aig.crm.patient.upload.exception;

public class StorageFileNotReadableException extends StorageException {

    public StorageFileNotReadableException(String message) {
        super(message);
    }

    public StorageFileNotReadableException(String message, Throwable cause) {
        super(message, cause);
    }
}
