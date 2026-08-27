package com.euphorie.exception;

public record ErrorResponse(
    int status,
    String message
) {}