package ru.yandex.praktikum.ivanov.kanban.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
    }
}
