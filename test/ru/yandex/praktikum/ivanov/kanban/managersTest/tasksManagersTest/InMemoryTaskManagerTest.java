package ru.yandex.praktikum.ivanov.kanban.managersTest.tasksManagersTest;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }
}
