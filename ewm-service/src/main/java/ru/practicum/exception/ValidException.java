package ru.practicum.exception;

public class ValidException extends IllegalArgumentException {
    public ValidException(String message) {
        super(message);
    }
}
