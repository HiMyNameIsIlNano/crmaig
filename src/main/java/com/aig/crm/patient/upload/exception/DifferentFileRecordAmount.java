package com.aig.crm.patient.upload.exception;

public class DifferentFileRecordAmount extends StorageException {

    public DifferentFileRecordAmount(String message) {
        super(message);
    }

    public DifferentFileRecordAmount(String message, Throwable cause) {
        super(message, cause);
    }
}
