package com.project.demo.easydl;

public class EasyDlUnavailableException extends RuntimeException {

    public EasyDlUnavailableException(String message) {
        super(message);
    }

    public EasyDlUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
