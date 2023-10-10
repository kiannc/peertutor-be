package com.peertutor.TuitionOrderMgr.exception;

public class ExistingTuitionOrderException extends Exception {
    public ExistingTuitionOrderException(String errorMessage) {
        super(errorMessage);
    }
}

