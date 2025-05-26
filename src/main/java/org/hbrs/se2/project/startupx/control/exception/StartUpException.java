package org.hbrs.se2.project.startupx.control.exception;

public class StartUpException extends RuntimeException {

    public StartUpException(String message) {
        super(message);
    }

    public StartUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
