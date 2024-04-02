package com.school.exception;

public record ExceptionResponse(String message,
                                String typeException,
                                String exceptionDateTime) {
}
