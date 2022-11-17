package main;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTaskList();                                // 2.1 Получение списка всех Task

    ArrayList<Subtask> getSubtaskList();                          // 2.1 Получение списка всех Subtask

    ArrayList<Epic> getEpicList();                                // 2.1 Получение списка всех Epic

    void deleteAllTasks();                                        // 2.2 Удаление всех Task

    void deleteAllSubtasks();                                     // 2.2 Удаление всех Subtask

    void deleteAllEpics();                                        // 2.2 Удаление всех Epic

    Task getTaskById(int id);                                     // 2.3 Получение Task по идентификатору

    Subtask getSubtaskById(int id);                               // 2.3 Получение Subtask по идентификатору

    Epic getEpicById(int id);                                     // 2.3 Получение Epic по идентификатору

    Task createTask(Task task);                                   // 2.4 Создание Task. Сам объект должен передаваться в качестве параметра

    Subtask createSubtask(Subtask subtask);                       // 2.4 Создание Subtask. Сам объект должен передаваться в качестве параметра

    Epic createEpic(Epic epic);                                   // 2.4 Создание Epic. Сам объект должен передаваться в качестве параметра

    void updateTask(Task task);                                   // 2.5 Обновление Task. Новая версия Task с верным идентификатором передаётся в виде параметра

    void updateSubtask(Subtask subtask);                          // 2.5 Обновление Subtask. Новая версия Subtask с верным идентификатором передаётся в виде параметра

    void updateEpic(Epic epic);                                   // 2.5 Обновление Epic. Новая версия Epic с верным идентификатором передаётся в виде параметра

    void deleteTask(int id);                                      // 2.6 Удаление Task по идентификатору

    void deleteSubtask(int id);                                   // 2.6 Удаление Subtask по идентификатору

    void deleteEpic(int id);                                      // 2.6 Удаление Epic по идентификатору

    ArrayList<Subtask> getEpicSubtasks(int id);                   // 3.1 Получение списка всех подзадач определённого эпика

    List<Task> getHistory();
}
