package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager taskManager = Managers.getDefault();

    Task task1 = new Task("Купить кроссовки",
            "Выделить время в течение недели, чтобы сходить в магазин обуви",
            Status.NEW);
    Task task2 = new Task("Повесить картину",
            "Подобрать место на стене, забить гвоздь и повесить картину",
            Status.DONE);

    @Test
    void getTaskListTest() {
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//
//        assertEquals(2, taskManager.getTaskList().size(), "не соответствует");
    }

    @Test
    void getSubtaskList() {
    }

    @Test
    void getEpicList() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void deleteAllSubtasks() {
    }

    @Test
    void deleteAllEpics() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getSubtaskById() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void createTask() {
    }

    @Test
    void createSubtask() {
    }

    @Test
    void createEpic() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void deleteSubtask() {
    }

    @Test
    void deleteEpic() {
    }

    @Test
    void getEpicSubtasks() {
    }

    @Test
    void getHistory() {
    }
}