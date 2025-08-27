package com.filmbox.MovieService.exception;

public class MovieException extends RuntimeException{

    private String errorCode;
    private int status;

    public MovieException(String message, String errorCode, int status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
