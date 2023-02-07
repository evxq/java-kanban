package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file = new File("C:\\Users\\ev\\dev\\java-kanban\\src\\ru\\yandex\\praktikum\\ivanov\\kanban\\save.csv");
    Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
    Epic epic1 = new Epic("epic1", "descriptionEpic1", new ArrayList<>());
    Subtask subtask11 = new Subtask("subtask11", "descriptionSubtask11", Status.DONE, epic1);

    @BeforeEach
    @Override
    protected void createManager() {
        taskManager = FileBackedTaskManager.getDefaultFile(file);
    }

    @Test
    void getDefaultFile_returnFileBackedTaskManager() {
        assertNotNull(taskManager, "объект файл-менеджера пуст");
    }

    @Test
    void loadFromFile_returnFileBackedTaskManagerFromFile() {
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(15));
        subtask11.setStartTime(LocalDateTime.now().plus(Duration.ofMinutes(30)));
        subtask11.setDuration(Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask11);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        assertNotNull(taskManager.getTaskList());
        assertNotNull(taskManager.getSubtaskList());
        assertNotNull(taskManager.getEpicList());
        assertEquals(taskManager.getTaskList().get(0), task1);
        assertEquals(taskManager.getSubtaskList().get(0), subtask11);
        assertEquals(taskManager.getEpicList().get(0).getId(), epic1.getId());
        assertNotNull(taskManager.getHistory());
        assertEquals(taskManager.getHistory().get(0), task1);
        assertEquals(taskManager.getHistory().get(1).getId(), epic1.getId());
        assertEquals(taskManager.getHistory().get(2), subtask11);

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);        // создаем новый менеджер из файла

        assertNotNull(loadedTaskManager.getTaskList());
        assertNotNull(loadedTaskManager.getSubtaskList());
        assertNotNull(loadedTaskManager.getEpicList());
        assertEquals(loadedTaskManager.getTaskList().get(0), task1);
        assertEquals(loadedTaskManager.getSubtaskList().get(0), subtask11);
        assertEquals(loadedTaskManager.getEpicList().get(0).getId(), epic1.getId());
        assertNotNull(loadedTaskManager.getHistory());
        assertEquals(loadedTaskManager.getHistory().get(0), task1);
        assertEquals(loadedTaskManager.getHistory().get(1).getId(), epic1.getId());
        assertEquals(loadedTaskManager.getHistory().get(2), subtask11);
    }
}