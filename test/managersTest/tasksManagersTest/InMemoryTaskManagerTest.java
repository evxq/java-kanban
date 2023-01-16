package managersTest.tasksManagersTest;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.InMemoryTaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager memoryManager = new InMemoryTaskManager();
    LocalDateTime time = LocalDateTime.now();
    Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
    Epic epic = new Epic("epic1", "descriptionEpic1", new ArrayList<>());
    Subtask subtaskNew1 = new Subtask("subtaskNew1", "descriptionSubtaskNew1", Status.NEW, epic);

    @Test
    void getPrioritizedTasks_returnSet() {
            task1.setStartTime(time);
            task1.setDuration(Duration.ofMinutes(15));
        memoryManager.createTask(task1);
            subtaskNew1.setStartTime(time.plus(Duration.ofMinutes(30)));
            subtaskNew1.setDuration(Duration.ofMinutes(15));
        memoryManager.createEpic(epic);
        memoryManager.createSubtask(subtaskNew1);
        Set<Task> set = memoryManager.getPrioritizedTasks();

        assertNotNull(set, "список приоритетов пуст");
        assertEquals(2, set.size(), "размер списка приоритетов не соответствует");
        assertEquals(task1, set.toArray()[0], "неправильный порядок задач");
        assertEquals(subtaskNew1, set.toArray()[1], "неправильный порядок задач");
    }
}