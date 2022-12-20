package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
    }
}
