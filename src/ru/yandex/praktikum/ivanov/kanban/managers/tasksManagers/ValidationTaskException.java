package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

public class ValidationTaskException extends RuntimeException {
    public ValidationTaskException(final String message) {
        super(message);
    }
}
