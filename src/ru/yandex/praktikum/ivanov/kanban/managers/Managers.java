package ru.yandex.praktikum.ivanov.kanban.managers;

import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.*;
import ru.yandex.praktikum.ivanov.kanban.managers.memoryManagers.*;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
