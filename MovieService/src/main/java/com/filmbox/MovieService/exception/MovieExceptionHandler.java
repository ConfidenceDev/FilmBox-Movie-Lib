package com.filmbox.MovieService.exception;

import com.filmbox.MovieService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MovieExceptionHandler {

    @ExceptionHandler(MovieException.class)
    public ResponseEntity<ErrorResponse> handleMovieException(MovieException movieException){
        ErrorResponse errorResponse = new ErrorResponse(movieException.getMessage(), movieException.getErrorCode());
        return new ResponseEntity<>(errorResponse,
                HttpStatus.valueOf(movieException.getStatus()));
    }
}
