package ru.yandex.praktikum.ivanov.kanban.managersTest.tasksManagersTest;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.InMemoryTaskManager;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    protected void createManager() throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();

    }
}
