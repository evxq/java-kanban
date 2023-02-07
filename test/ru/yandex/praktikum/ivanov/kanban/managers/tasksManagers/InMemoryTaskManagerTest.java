package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    protected void createManager() throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
    }
}
