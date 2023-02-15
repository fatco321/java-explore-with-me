package ru.practicum.exception;

public class ConflictException extends IllegalArgumentException  {
    public ConflictException(String message) {
        super(message);
    }
}
