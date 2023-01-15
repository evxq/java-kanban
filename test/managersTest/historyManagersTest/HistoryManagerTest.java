package managersTest.historyManagersTest;

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

/* Привет.
При первом коммите я некорректно сохранил в гите проект, хотя все тесты были сделаны.
Поэтому прошу прощения за пустоту в тестах.
В данной версии я также учел твои замечания по тестам */

class HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager taskManager = Managers.getDefault();
    Task task1 = new Task("task1","descriptionTask1", Status.NEW);
    Task task2 = new Task("task2", "descriptionTask2", Status.DONE);
    Task task3 = new Task("task3", "descriptionTask3", Status.IN_PROGRESS);

    @Test
    void getHistory_returnHistory_addToHistory() {
        taskManager.createTask(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "история пустая");
        assertEquals(1, history.size(),"история не соответствует");

        taskManager.createTask(task2);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history2 = historyManager.getHistory();
        assertEquals(2, history2.size(),"история не соответствует");
    }

    @Test
    void removeFirst() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task1, history.get(0));
        assertNotEquals(task1, history.get(1));
    }

    @Test
    void removeMiddle() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task2, history.get(0));
        assertNotEquals(task2, history.get(1));
    }

    @Test
    void removeLast() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        List<Task> history = historyManager.getHistory();

        assertNotEquals(task3, history.get(0));
        assertNotEquals(task3, history.get(1));
    }
}