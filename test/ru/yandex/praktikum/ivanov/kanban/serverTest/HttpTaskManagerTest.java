package ru.yandex.praktikum.ivanov.kanban.serverTest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.HttpTaskManager;
import ru.yandex.praktikum.ivanov.kanban.managersTest.tasksManagersTest.TaskManagerTest;
import ru.yandex.praktikum.ivanov.kanban.server.KVServer;
import ru.yandex.praktikum.ivanov.kanban.server.KVTaskClient;
import ru.yandex.praktikum.ivanov.kanban.tasks.Epic;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Subtask;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    Gson gson = new Gson();
    KVServer kvServer;

    @BeforeEach
    @Override
    protected void createManager() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @Test
    void save_returnTask() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
        taskManager.createTask(task1);
        String token = taskManager.getToken();

        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        String jsonResponse = client.load(token);
        String[] div = jsonResponse.split("\",\"\"customList:");
        String taskArray = div[0] + "]";

        JsonElement jsonElement = JsonParser.parseString(taskArray);
        String jsonObject = jsonElement.getAsJsonArray().get(0).toString();

        Task loadedTask = gson.fromJson(jsonObject, Task.class);

        Assertions.assertNotNull(loadedTask);
        Assertions.assertEquals(loadedTask.getName(), task1.getName());
        Assertions.assertEquals(loadedTask.getId(), task1.getId());
        Assertions.assertEquals(loadedTask.getDescription(), task1.getDescription());
        Assertions.assertEquals(loadedTask.getDuration(), task1.getDuration());
        Assertions.assertEquals(loadedTask.getStartTime(), task1.getStartTime());
    }

    @Test
    void load_returnManager() throws IOException, InterruptedException {
        Task task10 = new Task("task1", "descriptionTask1", Status.NEW);
        Task task20 = new Task("task2", "descriptionTask2", Status.NEW);
        Epic epic10 = new Epic("epic1", "description1", new ArrayList<>());
        Subtask subtaskNew10 = new Subtask("subtaskNew1", "descriptionSubtaskNew1", Status.NEW, epic10);
        taskManager.createTask(task10);
        taskManager.createTask(task20);
        taskManager.createEpic(epic10);
        taskManager.createSubtask(subtaskNew10);

        taskManager.getSubtaskById(4);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);

        HttpTaskManager loadedHttpManager = (HttpTaskManager) Managers.getDefault();
        loadedHttpManager.load();

        System.out.println();
        for (Task tasks: loadedHttpManager.getTaskList())  System.out.println("task: " + tasks);
        for (Epic epics: loadedHttpManager.getEpicList())  System.out.println("epic: " + epics);
        for (Subtask subtasks: loadedHttpManager.getSubtaskList())  System.out.println("subtask: " + subtasks);
        System.out.println("history: " + loadedHttpManager.getHistory());

        Assertions.assertEquals(2, loadedHttpManager.getTaskList().size());
        Assertions.assertEquals(1, loadedHttpManager.getEpicList().size());
        Assertions.assertEquals(1, loadedHttpManager.getSubtaskList().size());
        Assertions.assertEquals(4, loadedHttpManager.getHistory().size());
    }

    @AfterEach
    void stop() {
        kvServer.stop(1);
    }

}