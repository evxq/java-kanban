package main;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int id;
    HashMap<Integer, Task> taskMap = new HashMap<>();                     // 1. Возможность хранить задачи всех типов
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();

    public ArrayList<Task> getTaskList() {                                // 2.1 Получение списка всех Task
        return new ArrayList(taskMap.values());
    }

    public ArrayList<Subtask> getSubtaskList() {                          // 2.1 Получение списка всех Subtask
        return new ArrayList(subtaskMap.values());
    }

    public ArrayList<Epic> getEpicList() {                                // 2.1 Получение списка всех Epic
        return new ArrayList(epicMap.values());
    }

    public void deleteAllTasks() {                                        // 2.2 Удаление всех Task
        taskMap.clear();
    }

    public void deleteAllSubtasks() {                                     // 2.2 Удаление всех Subtask
        subtaskMap.clear();
    }

    public void deleteAllEpics() {                                        // 2.2 Удаление всех Epic
        epicMap.clear();
    }

    public Task getTaskById(int id) {                                     // 2.3 Получение Task по идентификатору
        return taskMap.get(id);
    }

    public Subtask getSubtaskById(int id) {                               // 2.3 Получение Subtask по идентификатору
        return subtaskMap.get(id);
    }

    public Epic getEpicById(int id) {                                     // 2.3 Получение Epic по идентификатору
        return epicMap.get(id);
    }

    public Task createTask(Task task) {                                   // 2.4 Создание Task. Сам объект должен передаваться в качестве параметра
        id++;
        task.setId(id);
        taskMap.put(id, task);                                            // добавить в таблицу Task
        return task;
    }

    public Subtask createSubtask(Subtask subtask) {                       // 2.4 Создание Subtask. Сам объект должен передаваться в качестве параметра
        id++;
        subtask.setId(id);
        subtask.getEpic().getEpicTaskList().add(subtask);                 // добавить в соотвествующий список Epic
        subtask.getEpic().checkEpicStatus();                              // проверить статус соответствующего Epic
        subtaskMap.put(id, subtask);                                      // добавить в таблицу Subtask
        return subtask;
    }

    public Epic createEpic(Epic epic) {                                   // 2.4 Создание Epic. Сам объект должен передаваться в качестве параметра
        id++;
        epic.setId(id);
        epicMap.put(id, epic);                                            // добавить в таблицу Epic
        epic.setStatus("NEW");                                            // для нового эпика ставится статус NEW
        return epic;
    }

    public void updateTask(Task task) {                                   // 2.5 Обновление Task. Новая версия Task с верным идентификатором передаётся в виде параметра
        if (task.getId() == null) {
                System.out.println("Неверный ID");
        } else if (!taskMap.containsKey(task.getId())) {
            System.out.println("Неверный ID");
        } else {
            taskMap.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {                          // 2.5 Обновление Subtask. Новая версия Subtask с верным идентификатором передаётся в виде параметра
        if (subtask.getId() == null) {
            System.out.println("Неверный ID");
        } else if (!subtaskMap.containsKey(subtask.getId())) {
            System.out.println("Неверный ID");
        } else {
            subtask.getEpic().checkEpicStatus();                          // Когда меняется статус подзадачи в эпике, необходимо проверить статус эпика
            subtaskMap.put(subtask.getId(), subtask);
        }
    }

    public void updateEpic(Epic epic) {                                   // 2.5 Обновление Epic. Новая версия Epic с верным идентификатором передаётся в виде параметра
        if (epic.getId() == null) {
            System.out.println("Неверный ID");
        } else if (!epicMap.containsKey(epic.getId())) {
            System.out.println("Неверный ID");
        } else {
            epic.checkEpicStatus();
            epicMap.put(epic.getId(), epic);
        }
    }

    public void deleteTask(int id) {                                      // 2.6 Удаление Task по идентификатору
        taskMap.remove(id);
    }

    public void deleteSubtask(int id) {                                   // 2.6 Удаление Subtask по идентификатору
        subtaskMap.get(id).getEpic().getEpicTaskList().remove(subtaskMap.get(id));
        subtaskMap.remove(id);
    }

    public void deleteEpic(int id) {                                      // 2.6 Удаление Epic по идентификатору
        for (Subtask subtask : epicMap.get(id).getEpicTaskList()) {
            subtaskMap.remove(subtask.getId());
        }
        epicMap.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {                   // 3.1 Получение списка всех подзадач определённого эпика
        return epicMap.get(id).getEpicTaskList();
    }

}