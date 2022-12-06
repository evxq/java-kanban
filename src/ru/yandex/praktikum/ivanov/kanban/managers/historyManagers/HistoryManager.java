package ru.yandex.praktikum.ivanov.kanban.managers.historyManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);            // <ТЗ-5> удаление задач из истории_просмотра

    List<Task> getHistory();
}
