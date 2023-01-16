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

class HistoryManagerTest {

    /* В текущей логике приложения тест истории без ТаскМенеджера не получится,
    так как методы HistoryManager (add и remove) работают через Id Тасков,
    который в свою очередь рассчитывается только в методе createTask класса TaskManager */

    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager taskManager = Managers.getDefault();
    Task task1 = new Task("task1","descriptionTask1", Status.NEW);
    Task task2 = new Task("task2", "descriptionTask2", Status.DONE);
    Task task3 = new Task("task3", "descriptionTask3", Status.IN_PROGRESS);

    @Test
    void addToHistory_getHistory_returnHistory() {
        taskManager.createTask(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "история пустая");
        assertEquals(1, history.size(),"история не соответствует");
    }

    @Test
    void getHistory_returnHistoryInOrderOfAdding() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);          // дубликат для проверки перезаписи истории

        assertEquals(2, historyManager.getHistory().size(),"размер истории не соответствует");
        assertEquals(2, historyManager.getHistory().get(0).getId(),"порядок вызова задачи не соответствует");
        assertEquals(1, historyManager.getHistory().get(1).getId(),"порядок вызова задачи не соответствует");
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

        // Так как задачи неразрывно связаны с Id,
        // то в данных тестах на наличие задачи в списке
        // достаточно проверить только Id
        assertNotEquals(task1.getId(), history.get(0).getId());
        assertNotEquals(task1.getId(), history.get(1).getId());
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

        assertNotEquals(task2.getId(), history.get(0).getId());
        assertNotEquals(task2.getId(), history.get(1).getId());
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

        assertNotEquals(task3.getId(), history.get(0).getId());
        assertNotEquals(task3.getId(), history.get(1).getId());
    }
}