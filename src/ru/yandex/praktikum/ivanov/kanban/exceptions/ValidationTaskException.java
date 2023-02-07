package ru.yandex.praktikum.ivanov.kanban.exceptions;

public class ValidationTaskException extends RuntimeException {
    public ValidationTaskException(final String message) {
        super(message);
    }
}
