package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.util.ArrayList;

public class StringManager {

    public static String toString(Epic epic) {                      // сохранение задачи в строку
        return epic.getId() + "," + TasksTypes.EPIC + "," +
                epic.getName() + "," +
                epic.getStatus() + "," +
                epic.getDescription() + "\n";
    }

    public static String toString(Subtask subtask) {
        return subtask.getId() + "," +
                TasksTypes.SUBTASK + "," +
                subtask.getName() + "," +
                subtask.getStatus() + "," +
                subtask.getDescription() + "," +
                subtask.getEpic().getId() + "\n";
    }

    public static String toString(Task task) {
        return task.getId() + "," +
                TasksTypes.TASK + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "\n";
    }

    public static Epic fromStringToEpic(String[] taskArray, FileBackedTaskManager manager) {
        Epic loadedEpic = new Epic(taskArray[2], taskArray[4], new ArrayList<>());
        manager.getEpicMap().put(Integer.parseInt(taskArray[0]), loadedEpic);
        loadedEpic.setId(Integer.parseInt(taskArray[0]));
        return loadedEpic;
    }

    public static Subtask fromStringToSubtask(String[] taskArray, FileBackedTaskManager manager) {
        Subtask loadedSubtask = new Subtask(taskArray[2],
                taskArray[4],
                Status.valueOf(taskArray[3]),
                manager.getEpicMap().get(Integer.parseInt(taskArray[5])));
        loadedSubtask.getEpic().getEpicTaskList().add(loadedSubtask);
        loadedSubtask.setId(Integer.parseInt(taskArray[0]));
        return loadedSubtask;
    }

    public static Task fromStringToTask(String[] taskArray) {
        Task loadedTask = new Task(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]));
        loadedTask.setId(Integer.parseInt(taskArray[0]));
        return loadedTask;
    }

}
