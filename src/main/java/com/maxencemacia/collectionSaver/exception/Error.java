package com.maxencemacia.collectionSaver.exception;


import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum Error {
    MUST_HAVE_A_TYPE("La collection doit avoir un type", HttpStatus.SC_BAD_REQUEST),
    BAD_REQUEST("bad request", HttpStatus.SC_BAD_REQUEST);
    private final String message;
    private final int status;
    Error(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
