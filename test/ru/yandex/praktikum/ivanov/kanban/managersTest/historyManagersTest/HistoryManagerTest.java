package ru.yandex.praktikum.ivanov.kanban.managersTest.historyManagersTest;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.HistoryManager;
import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.InMemoryHistoryManager;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();
    Task task1 = new Task("task1","descriptionTask1", Status.NEW);
    Task task2 = new Task("task2", "descriptionTask2", Status.DONE);
    Task task3 = new Task("task3", "descriptionTask3", Status.IN_PROGRESS);

    @Test
    void addToHistory_getHistory_returnHistory() {
        task1.setId(1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "история пустая");
        assertEquals(1, history.size(),"история не соответствует");
    }

    @Test
    void getHistory_returnHistoryInOrderOfAdding() {
        task1.setId(1);
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);          // дубликат для проверки перезаписи истории

        assertEquals(2, historyManager.getHistory().size(),"размер истории не соответствует");
        assertEquals(2, historyManager.getHistory().get(0).getId(),"порядок вызова задачи не соответствует");
        assertEquals(1, historyManager.getHistory().get(1).getId(),"порядок вызова задачи не соответствует");
    }

    @Test
    void removeFirst() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task1.getId(), history.get(0).getId());
        assertNotEquals(task1.getId(), history.get(1).getId());
    }

    @Test
    void removeMiddle() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task2.getId(), history.get(0).getId());
        assertNotEquals(task2.getId(), history.get(1).getId());
    }

    @Test
    void removeLast() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task3.getId(), history.get(0).getId());
        assertNotEquals(task3.getId(), history.get(1).getId());
    }
}