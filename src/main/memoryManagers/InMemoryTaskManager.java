package main.memoryManagers;

import main.Managers;
import main.historyManagers.HistoryManager;
import main.tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    int id;
    private HashMap<Integer, Task> taskMap = new HashMap<>();                     // 1. Возможность хранить задачи всех типов
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HistoryManager historyObject;

    public InMemoryTaskManager() {
        historyObject = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTaskList() {                                // 2.1 Получение списка всех Task
        return new ArrayList(taskMap.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {                          // 2.1 Получение списка всех Subtask
        return new ArrayList(subtaskMap.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {                                // 2.1 Получение списка всех Epic
        return new ArrayList(epicMap.values());
    }

    @Override
    public void deleteAllTasks() {                                        // 2.2 Удаление всех Task
        taskMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {                                     // 2.2 Удаление всех Subtask
        subtaskMap.clear();
    }

    @Override
    public void deleteAllEpics() {                                        // 2.2 Удаление всех Epic
        epicMap.clear();
    }

    @Override
    public Task getTaskById(int id) {                                     // 2.3 Получение Task по идентификатору
        historyObject.add(taskMap.get(id));                               // Добавить в список просмотренных задач
        return taskMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {                               // 2.3 Получение Subtask по идентификатору
        historyObject.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {                                     // 2.3 Получение Epic по идентификатору
        historyObject.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Task createTask(Task task) {                                   // 2.4 Создание Task. Сам объект должен передаваться в качестве параметра
        id++;
        task.setId(id);
        taskMap.put(id, task);                                            // добавить в таблицу Task
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {                       // 2.4 Создание Subtask. Сам объект должен передаваться в качестве параметра
        id++;
        subtask.setId(id);
        subtask.getEpic().getEpicTaskList().add(subtask);                 // добавить в соотвествующий список Epic
        subtask.getEpic().checkEpicStatus();                              // проверить статус соответствующего Epic
        subtaskMap.put(id, subtask);                                      // добавить в таблицу Subtask
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {                                   // 2.4 Создание Epic. Сам объект должен передаваться в качестве параметра
        id++;
        epic.setId(id);
        epicMap.put(id, epic);                                            // добавить в таблицу Epic
        epic.setStatus(Status.NEW);                                       // для нового эпика ставится статус NEW
        return epic;
    }

    @Override
    public void updateTask(Task task) {                                   // 2.5 Обновление Task. Новая версия Task с верным идентификатором передаётся в виде параметра
        if (task.getId() == null) {
                System.out.println("Неверный ID");
        } else if (!taskMap.containsKey(task.getId())) {
            System.out.println("Неверный ID");
        } else {
            taskMap.put(task.getId(), task);
        }
    }

    @Override
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

    @Override
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

    @Override
    public void deleteTask(int id) {                                      // 2.6 Удаление Task по идентификатору
        taskMap.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {                                   // 2.6 Удаление Subtask по идентификатору
        subtaskMap.get(id).getEpic().getEpicTaskList().remove(subtaskMap.get(id));
        subtaskMap.remove(id);
    }

    @Override
    public void deleteEpic(int id) {                                      // 2.6 Удаление Epic по идентификатору
        for (Subtask subtask : epicMap.get(id).getEpicTaskList()) {
            subtaskMap.remove(subtask.getId());
        }
        epicMap.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {                   // 3.1 Получение списка всех подзадач определённого эпика
        return epicMap.get(id).getEpicTaskList();
    }

    @Override
    public List<Task> getHistory() {
        return historyObject.getHistory();
    }
}