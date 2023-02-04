package ru.yandex.praktikum.ivanov.kanban.managers;

import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.*;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.*;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");                                 // <ТЗ-8>
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
