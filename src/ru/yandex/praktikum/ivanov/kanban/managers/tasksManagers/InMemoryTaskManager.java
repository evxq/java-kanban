package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import ru.yandex.praktikum.ivanov.kanban.exceptions.ValidationTaskException;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.HistoryManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> taskMap = new HashMap<>();                     // 1. Возможность хранить задачи всех типов
    private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyObject;

    public InMemoryTaskManager() {
        historyObject = Managers.getDefaultHistory();
    }

    protected HistoryManager getHistoryObject() {                            // <ТЗ-6>
        return historyObject;
    }

    protected HashMap<Integer, Task> getTaskMap() {                          // <ТЗ-6>
        return taskMap;
    }

    protected HashMap<Integer, Subtask> getSubtaskMap() {                    // <ТЗ-6>
        return subtaskMap;
    }

    protected HashMap<Integer, Epic> getEpicMap() {                          // <ТЗ-6>
        return epicMap;
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
        if (taskMap.get(id) != null) {
            historyObject.add(taskMap.get(id));                           // Добавить в список историю_просмотров
            return taskMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {                               // 2.3 Получение Subtask по идентификатору
        if (subtaskMap.get(id) != null) {
            historyObject.add(subtaskMap.get(id));
            return subtaskMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {                                     // 2.3 Получение Epic по идентификатору
        if (epicMap.get(id) != null) {
            historyObject.add(epicMap.get(id));
            return epicMap.get(id);
        }
        return null;
    }

    private void checkTasksIntersection(Task task) {                       // <ТЗ-7> проверка пересечения задач
        if (task.getStartTime() == null) {
            return;
        }
        for (Task anotherTask: getPrioritizedTasks()) {
            String errorText = task.getName() + " пересекается по времени с " + anotherTask.getName() + ". Задача " + task.getName() + " не может быть создана/изменена";
            if (anotherTask.getStartTime() != null && anotherTask.getEndTime() != null) {
                // ---------------  anotherTask
                //     >>>>>>>      task
                if (task.getStartTime().isAfter(anotherTask.getStartTime()) && task.getStartTime().isBefore(anotherTask.getEndTime())) {
                    throw new ValidationTaskException(errorText);
                }
                //    -------  anotherTask
                // >>>>>>      task
                else if (anotherTask.getStartTime().isAfter(task.getStartTime()) && anotherTask.getStartTime().isBefore(task.getEndTime())) {
                    throw new ValidationTaskException(errorText);
                }
                // -------      anotherTask
                //    >>>>>>>>  task
                else if (task.getStartTime().isAfter(anotherTask.getStartTime()) && task.getStartTime().isBefore(anotherTask.getEndTime())) {
                    throw new ValidationTaskException(errorText);
                }
            }
        }
    }

    @Override
    public Task createTask(Task task) {                                   // 2.4 Создание Task. Сам объект должен передаваться в качестве параметра
        try {
            checkTasksIntersection(task);
            id++;
            task.setId(id);
            taskMap.put(id, task);                                        // добавить в таблицу Task
            return task;
        } catch (ValidationTaskException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void calculateEpicStartTime(Subtask subtask) {
        if (subtask.getStartTime() != null &&
                (subtask.getEpic().getStartTime() == null || subtask.getStartTime().isBefore(subtask.getEpic().getStartTime())) ) {    // <ТЗ-7> проверка и установка startTime Epic'a
            subtask.getEpic().setStartTime(subtask.getStartTime());
        }

        if (subtask.getEndTime() != null &&
                (subtask.getEpic().getEndTime() == null || subtask.getEndTime().isAfter(subtask.getEpic().getEndTime())) ) {           // <ТЗ-7> проверка и установка endTime Epic'a
            subtask.getEpic().setEndTime(subtask.getEndTime());
        }

        if (subtask.getEpic().getStartTime() != null && subtask.getEpic().getEndTime() != null) {                                      // <ТЗ-7> перерасчет дюрации Epic'a
            subtask.getEpic().setDuration(Duration.between(subtask.getEpic().getStartTime(), subtask.getEpic().getEndTime()));
        }
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {                       // 2.4 Создание Subtask. Сам объект должен передаваться в качестве параметра
        try {
            checkTasksIntersection(subtask);
            id++;
            subtask.setId(id);
            if (subtask.getEpic().getId() == null) {
                for (Epic epic : epicMap.values()) {
                    if (epic.getName().equals(subtask.getEpic().getName())) {
                        subtask.setEpic(epic);
                    }
                }
            }
            subtask.getEpic().getEpicTaskList().add(subtask);                 // добавить в соотвествующий список Epic
            subtask.setSubtasksEpicId(subtask.getEpic().getId());             //
            subtask.getEpic().checkEpicStatus();                              // проверить статус соответствующего Epic
            subtaskMap.put(id, subtask);                                      // добавить в таблицу Subtask
            calculateEpicStartTime(subtask);

            return subtask;
        } catch (ValidationTaskException e) {
            System.out.println(e.getMessage());
        }
        return null;
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
        try {
            checkTasksIntersection(task);
            if (task.getId() == null) {
                    System.out.println("Неверный ID");
            } else if (!taskMap.containsKey(task.getId())) {
                System.out.println("Неверный ID");
            } else {
                taskMap.put(task.getId(), task);
            }
        } catch (ValidationTaskException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {                          // 2.5 Обновление Subtask. Новая версия Subtask с верным идентификатором передаётся в виде параметра
        try {
            checkTasksIntersection(subtask);
            if (subtask.getId() == null) {
                System.out.println("Неверный ID");
            } else if (!subtaskMap.containsKey(subtask.getId())) {
                System.out.println("Неверный ID");
            } else {
                subtask.getEpic().checkEpicStatus();                          // Когда меняется статус подзадачи в эпике, необходимо проверить статус эпика
                subtaskMap.put(subtask.getId(), subtask);
                calculateEpicStartTime(subtask);
            }
        } catch (ValidationTaskException e) {
            System.out.println(e.getMessage());
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
        historyObject.remove(id);                                         // <ТЗ-5> удаление Task из истории_просмотров
    }

    @Override
    public void deleteSubtask(int id) {                                   // 2.6 Удаление Subtask по идентификатору
        int epicId = subtaskMap.get(id).getSubtasksEpicId();
        if (!epicMap.isEmpty()) {
            epicMap.get(epicId).getEpicTaskList().remove(subtaskMap.get(id));               // удаление Subtask из списка Epic
            epicMap.get(epicId).checkEpicStatus();                                          // проверить статус соответствующего Epic
        }
        subtaskMap.remove(id);
        historyObject.remove(id);                                         // <ТЗ-5> удаление Subtask из истории_просмотров
    }

    @Override
    public void deleteEpic(int id) {                                      // 2.6 Удаление Epic по идентификатору
        historyObject.remove(id);                                         // <ТЗ-5> удаление Epic'а из истории_просмотров
        for (Subtask subtask : epicMap.get(id).getEpicTaskList()) {
            historyObject.remove(subtask.getId());                        // <ТЗ-5> удаление всех подзадач Epic'а из истории_просмотров
        }
        for (Subtask subtask : epicMap.get(id).getEpicTaskList()) {       // удаление всех Subtask данного Epic
            subtaskMap.remove(subtask.getId());
        }
        for (Subtask subtask: subtaskMap.values()) {                      // удаление сабтаска по упоминаню id эпика
            if (subtask.getSubtasksEpicId() == id) {
                subtaskMap.remove(subtask.getId());
            }
        }
        epicMap.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {                   // 3.1 Получение списка всех подзадач определённого эпика
        if (!epicMap.containsKey(id)) {
            return null;
        }
        if (epicMap.get(id).getEpicTaskList().isEmpty()) {                        // добавление сабтасков в эпик по его списку id сабтасков
            for (Integer i: epicMap.get(id).getEpicIDTaskList()) {
                epicMap.get(id).getEpicTaskList().add(subtaskMap.get(i));
            }
        }
        return epicMap.get(id).getEpicTaskList();
    }

    @Override
    public List<Task> getHistory() {
        return historyObject.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Comparator<Task> startTimeComparator = (task1, task2) -> {
            if (task1.getStartTime() == null && task2.getStartTime() == null) {
                return 1;
            } else if (task1.getStartTime() == null && task2.getStartTime() != null) {
                return 1;
            } else if (task2.getStartTime() == null && task1.getStartTime() != null) {
                return -1;
            } else if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return -1;
            } else {
                return 1;
            }
        };
        Set<Task> prioritizedSet = new TreeSet<>(startTimeComparator);
        prioritizedSet.addAll(getTaskList());
        prioritizedSet.addAll(getSubtaskList());
        return prioritizedSet;
    }
}