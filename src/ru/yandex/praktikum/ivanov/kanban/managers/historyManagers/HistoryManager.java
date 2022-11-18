package ru.yandex.praktikum.ivanov.kanban.managers.historyManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
