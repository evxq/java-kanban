package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.server.KVServer;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.io.IOException;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    @Override
    protected void createManager() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @Test
    void save_load_returnTask() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
        taskManager.createTask(task1);

        HttpTaskManager anotherHttpTaskManager = (HttpTaskManager) Managers.getDefault();
        anotherHttpTaskManager.load();
        Task loadedTask = anotherHttpTaskManager.getTaskList().get(0);

        Assertions.assertNotNull(loadedTask);
        Assertions.assertEquals(loadedTask.getName(), task1.getName());
        Assertions.assertEquals(loadedTask.getId(), task1.getId());
        Assertions.assertEquals(loadedTask.getDescription(), task1.getDescription());
        Assertions.assertEquals(loadedTask.getDuration(), task1.getDuration());
        Assertions.assertEquals(loadedTask.getStartTime(), task1.getStartTime());
    }

    @AfterEach
    void stop() {
        kvServer.stop(0);
    }

}