package com.test.kalahagame.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorDetails {

    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private String message;
}
