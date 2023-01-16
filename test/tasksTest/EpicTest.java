package tasksTest;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager = Managers.getDefault();

    Task task1 = new Task("task1","descriptionTask1", Status.NEW);
    Epic epic = new Epic("epic1", "description1", new ArrayList<>());
    Subtask subtaskNew1 = new Subtask("subtaskNew1", "descriptionSubtaskNew1", Status.NEW, epic);
    Subtask subtaskNew2 = new Subtask("subtaskNew2", "descriptionSubtaskNew2", Status.NEW, epic);
    Subtask subtaskDone1 = new Subtask("subtaskDone1", "descriptionSubtaskDone1", Status.DONE, epic);
    Subtask subtaskDone2 = new Subtask("subtaskDone2", "descriptionSubtaskDone2", Status.DONE, epic);
    Subtask subtaskInProgress = new Subtask("subtaskInProgress", "subtaskInProgress", Status.IN_PROGRESS, epic);

    @Test
    public void getStatus_returnNew_epicIsEmpty() {                                // a. Пустой список подзадач
        taskManager.createEpic(epic);

        assertEquals(Status.NEW, epic.getStatus(),"Статус пустого эпика не соответствует");
    }

    @Test
    public void getStatus_returnNew_allSubtasksAreNew() {                      // b. Все подзадачи со статусом NEW
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        taskManager.createSubtask(subtaskNew2);

        assertEquals(Status.NEW, epic.getStatus(),"Статус эпика не соответствует");
    }

    @Test
    public void getStatus_returnDone_allSubtasksAreDone() {                    // c. Все подзадачи со статусом DONE
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskDone1);
        taskManager.createSubtask(subtaskDone2);

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика не соответствует");
    }

    @Test
    public void getStatus_returnNew_subtasksAreNewAndDone() {               // d. Подзадачи со статусами NEW и DONE
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        taskManager.createSubtask(subtaskDone1);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика не соответствует");
    }

    @Test
    public void getStatus_returnInProgress_subtaskIsInProgress() {        // e. Подзадачи со статусом IN_PROGRESS
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskInProgress);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика не соответствует");
    }

    @Test
    public void getStartTimeAndDuration() {
        subtaskNew1.setStartTime(LocalDateTime.now());
        subtaskNew1.setDuration(Duration.ofMinutes(15));
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(15));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);

        assertNotNull(subtaskNew1.getStartTime(), "время начала подзадачи не задано");
        assertNotNull(subtaskNew1.getDuration(), "продолжительность подзадачи не задана");
        assertNotNull(epic.getStartTime(), "время начала эпика не задано");
        assertNotNull(epic.getDuration(), "продолжительность эпика не задана");
        assertNotNull(epic.getEndTime(), "время конца эпика не задано");
        assertNotNull(task1.getStartTime(), "время начала задачи не задано");
        assertNotNull(task1.getDuration(), "продолжительность задачи не задана");
    }
}