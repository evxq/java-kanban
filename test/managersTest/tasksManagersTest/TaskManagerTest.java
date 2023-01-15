package managersTest.tasksManagersTest;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.Epic;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Subtask;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager taskManager = Managers.getDefault();

    Task task1 = new Task("task1",
            "descriptionTask1",
            Status.NEW);
    Task task2 = new Task("task2",
            "descriptionTask2",
            Status.DONE);
    Epic epic = new Epic("epic1", "description1", new ArrayList<>());
    Subtask subtaskNew1 = new Subtask("subtaskNew1",
            "descriptionSubtaskNew1",
            Status.NEW, epic);
    Subtask subtaskNew2 = new Subtask("subtaskNew2",
            "descriptionSubtaskNew2",
                        Status.NEW, epic);

    @Test
    void getTaskList_returnTaskList() {                                                 // a. Со стандартным поведением
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals(2, taskManager.getTaskList().size(), "не соответствует");
    }

    @Test
    void getTaskList_returnTaskList_listIsEmpty() {                                     // b. С пустым списком задач
        assertEquals(0, taskManager.getTaskList().size(), "не соответствует");
    }

    @Test
    void getSubtaskList_returnSubtaskList() {                                           // a. Со стандартным поведением
        taskManager.createSubtask(subtaskNew1);
        taskManager.createSubtask(subtaskNew2);

        assertEquals(2, taskManager.getSubtaskList().size(), "не соответствует");
    }

    @Test
    void getSubtaskList_returnSubtaskList_listIsEmpty() {                               // b. С пустым списком задач
        assertEquals(0, taskManager.getSubtaskList().size(), "не соответствует");
    }

    @Test
    void getEpicList_returnEpicList() {                                                 // a. Со стандартным поведением.
        taskManager.createEpic(epic);

        assertEquals(1, taskManager.getEpicList().size(), "не соответствует");
    }

    @Test
    void getEpicList_returnEpicList_listIsEmpty() {                                     // b. С пустым списком задач.
        assertEquals(0, taskManager.getEpicList().size(), "не соответствует");
    }

    @Test
    void deleteAllTasks() {                                                             // a. Со стандартным поведением.
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getTaskList().size(), "не соответствует");
    }

    @Test
    void deleteAllTasks_taskListIsEmpty() {                                             // b. С пустым списком задач.
        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getTaskList().size(), "не соответствует");
    }

    @Test
    void deleteAllSubtasks() {                                                          // a. Со стандартным поведением
        taskManager.createSubtask(subtaskNew1);
        taskManager.createSubtask(subtaskNew2);
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getSubtaskList().size(), "не соответствует");
    }

    @Test
    void deleteAllSubtasks_subtaskListIsEmpty() {                                       // b. С пустым списком задач
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getSubtaskList().size(), "не соответствует");
    }

    @Test
    void deleteAllEpics() {                                                             // a. Со стандартным поведением
        taskManager.createEpic(epic);
        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getEpicList().size(), "не соответствует");
    }

    @Test
    void deleteAllEpics_epicListIsEmpty() {                                             // b. С пустым списком задач
        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getEpicList().size(), "не соответствует");
    }

    @Test
    void getTaskById_returnTask() {                                                     // a. Со стандартным поведением
        taskManager.createTask(task1);
        int taskId = task1.getId();

        assertEquals(task1, taskManager.getTaskById(taskId), "задачи не совпадают" );
    }

    @Test
    void getTaskById_returnExp_idIsWrong() {                                            //  c. С неверным идентификатором задачи
        assertNull(taskManager.getTaskById(-1));

        Integer n = null;
        NullPointerException nullId = assertThrows(
                NullPointerException.class,
                () -> taskManager.getTaskById(n)
        );

        assertNull(nullId.getMessage());
    }

    @Test
    void getSubtaskById_returnSubtask() {                                               // a. Со стандартным поведением
        taskManager.createSubtask(subtaskNew1);
        int taskId = subtaskNew1.getId();

        assertEquals(subtaskNew1, taskManager.getSubtaskById(taskId), "задачи не совпадают" );
    }

    @Test
    void getSubtaskById_returnExp_idIsWrong() {                                         // c. С неверным идентификатором задачи
        assertNull(taskManager.getSubtaskById(-1));

        Integer n = null;
        NullPointerException nullId = assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubtaskById(n)
        );

        assertNull(nullId.getMessage());
    }

    @Test
    void getEpicById_returnEpic() {                                                     // a. Со стандартным поведением
        taskManager.createEpic(epic);
        int taskId = epic.getId();

        assertEquals(epic, taskManager.getEpicById(taskId), "задачи не совпадают" );
    }

    @Test
    void getEpicById_returnExp_idIsWrong() {                                            // c. С неверным идентификатором задачи
        NullPointerException wrongId = assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpicById(-1)
        );
        assertNull(wrongId.getMessage());

        Integer n = null;
        NullPointerException nullId = assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpicById(n)
        );
        assertNull(nullId.getMessage());
    }

    @Test
    void createTask_returnTaskAndTaskList() {
        taskManager.createTask(task1);
        int taskId = task1.getId();
        Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "задача не найдена");
        assertEquals(task1, savedTask, "Задачи не совпадают");

        List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(1, taskManager.getTaskList().size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void createSubtask_returnSubtaskAndSubtaskList() {
        taskManager.createSubtask(subtaskNew1);
        int taskId = subtaskNew1.getId();
        Subtask savedSubtask = taskManager.getSubtaskById(taskId);

        assertNotNull(savedSubtask, "задача не найдена");
        assertEquals(subtaskNew1, savedSubtask, "Задачи не совпадают");

        List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи на возвращаются");
        assertEquals(1, taskManager.getSubtaskList().size(), "Неверное количество задач");
        assertEquals(subtaskNew1, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    void createEpic_returnEpicAndEpicList() {
        taskManager.createEpic(epic);
        int taskId = epic.getId();
        Epic savedEpic = taskManager.getEpicById(taskId);

        assertNotNull(savedEpic, "задача не найдена");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи на возвращаются");
        assertEquals(1, taskManager.getEpicList().size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    void updateTask_returnUpdatedTask() {
        taskManager.createTask(task1);
        Task updatedTask = new Task("updateName", "updateDescript", Status.DONE);
        updatedTask.setId(task1.getId());
        taskManager.updateTask(updatedTask);

        assertEquals(updatedTask, taskManager.getTaskList().get(0), "задачи не совпадают");
    }

    @Test
    void updateSubtask_returnUpdatedSubtask() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        Subtask updatedSubtask = new Subtask("updateName", "updateDescript", Status.DONE, epic);
        updatedSubtask.setId(subtaskNew1.getId());
        taskManager.updateSubtask(updatedSubtask);

        assertEquals(updatedSubtask, taskManager.getSubtaskList().get(0), "задачи не совпадают");
    }

    @Test
    void updateEpic_returnUpdatedEpic() {
        taskManager.createEpic(epic);
        Epic updatedEpic = new Epic("updateName", "updateDescript", new ArrayList<>());
        updatedEpic.setId(epic.getId());
        taskManager.updateEpic(updatedEpic);

        assertEquals(updatedEpic, taskManager.getEpicList().get(0), "задачи не совпадают");
    }

    @Test
    void deleteTask_fromTaskListAndHistory() {
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());
        taskManager.deleteTask(task1.getId());

        assertEquals(0 ,taskManager.getTaskList().size(), "задача не удалилась");
        assertEquals(0, taskManager.getHistory().size(), "задача не удалилась из истории");
    }

    @Test
    void deleteSubtask_fromSubtaskListAndHistory() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        taskManager.getSubtaskById(subtaskNew1.getId());
        taskManager.deleteSubtask(subtaskNew1.getId());

        assertEquals(0 ,epic.getEpicTaskList().size(), "задача не удалилась из эпика");
        assertEquals(0 ,taskManager.getSubtaskList().size(), "задача не удалилась");
        assertEquals(0, taskManager.getHistory().size(), "задача не удалилась из истории");
    }

    @Test
    void deleteEpic_fromEpicListAndHistory() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        taskManager.getEpicById(epic.getId());
        taskManager.deleteEpic(epic.getId());

        assertEquals(0 ,taskManager.getEpicList().size(), "задача не удалилась");
        assertEquals(0 ,taskManager.getSubtaskList().size(), "подзадача не удалилась");
        assertEquals(0, taskManager.getHistory().size(), "задача не удалилась из истории");
    }

    @Test
    void getEpicSubtasks_returnEpicSubtaskList() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskNew1);
        int id = epic.getId();

        assertNotNull(taskManager.getEpicSubtasks(id), "список подзадач не возвращается");
        assertEquals(1, taskManager.getEpicSubtasks(id).size(), "неверное количество подзадач");
    }

    @Test
    void getHistory_returnHistoryList() {
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        assertNotNull(taskManager.getHistory(), "история пустая");
        assertEquals(1, taskManager.getHistory().size(), "история не соответствует");
    }
}