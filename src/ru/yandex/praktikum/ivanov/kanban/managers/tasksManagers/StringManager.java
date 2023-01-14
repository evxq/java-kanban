package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StringManager {

    public static String toString(Epic epic) {                      // сохранение задачи в строку
        return epic.getId() + "," + TasksTypes.EPIC + "," +
                epic.getName() + "," +
                epic.getStatus() + "," +
                epic.getDescription() + "," +
                epic.getDuration() + "," +
                epic.getStartTime() + "\n";
    }

    public static String toString(Subtask subtask) {
        return subtask.getId() + "," +
                TasksTypes.SUBTASK + "," +
                subtask.getName() + "," +
                subtask.getStatus() + "," +
                subtask.getDescription() + "," +
                subtask.getDuration() + "," +
                subtask.getStartTime() + "," +
                subtask.getEpic().getId() + "\n";
    }

    public static String toString(Task task) {
        return task.getId() + "," +
                TasksTypes.TASK + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getDuration() + "," +
                task.getStartTime() + "\n";
    }

    public static Epic fromStringToEpic(String[] taskArray, FileBackedTaskManager manager) {            // создание Epic из строки
        Epic loadedEpic = new Epic(taskArray[2], taskArray[4], new ArrayList<>());
        manager.getEpicMap().put(Integer.parseInt(taskArray[0]), loadedEpic);
        loadedEpic.setId(Integer.parseInt(taskArray[0]));
        if (taskArray[5].equals("null")) {
            loadedEpic.setDuration(null);
        } else {
            if (taskArray[5].contains("H")) {
                String[] s = taskArray[5].split("H");
                int hours = Integer.parseInt(s[0].split("PT")[1]);
                int minutes = Integer.parseInt( s[1].split("M")[0] );
                int result = 60 * hours + minutes;
                loadedEpic.setDuration(Duration.ofMinutes(result));
            } else {
                loadedEpic.setDuration(Duration.ofMinutes(Integer.parseInt(taskArray[5].split("PT")[1].split("M")[0])));
            }
        }
        if (taskArray[6].equals("null")) {
            loadedEpic.setStartTime(null);
        } else {
            loadedEpic.setStartTime(LocalDateTime.parse(taskArray[6]));
        }
        return loadedEpic;
    }

    public static Subtask fromStringToSubtask(String[] taskArray, FileBackedTaskManager manager) {      // создание Subtask из строки
        Subtask loadedSubtask = new Subtask(taskArray[2],
                taskArray[4],
                Status.valueOf(taskArray[3]),
                manager.getEpicMap().get(Integer.parseInt(taskArray[7])));
        loadedSubtask.getEpic().getEpicTaskList().add(loadedSubtask);
        loadedSubtask.setId(Integer.parseInt(taskArray[0]));
        if (taskArray[5].equals("null")) {
            loadedSubtask.setDuration(null);
        } else {
            if (taskArray[5].contains("H")) {
                String[] s = taskArray[5].split("H");
                int hours = Integer.parseInt(s[0].split("PT")[0]);
                int minutes = Integer.parseInt( s[1].split("M")[0] );
                int result = 60 * hours + minutes;
                loadedSubtask.setDuration(Duration.ofMinutes(result));
            } else {
                loadedSubtask.setDuration(Duration.ofMinutes(Integer.parseInt(taskArray[5].split("PT")[1].split("M")[0])));
            }
        }
        if (taskArray[6].equals("null")) {
            loadedSubtask.setStartTime(null);
        } else {
            loadedSubtask.setStartTime(LocalDateTime.parse(taskArray[6]));
        }
        return loadedSubtask;
    }

    public static Task fromStringToTask(String[] taskArray) {                                           // создание Task из строки
        Task loadedTask = new Task(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]));
        loadedTask.setId(Integer.parseInt(taskArray[0]));
        if (taskArray[5].equals("null")) {
            loadedTask.setDuration(null);
        } else {
            if (taskArray[5].contains("H")) {
                String[] s = taskArray[5].split("H");
                int hours = Integer.parseInt(s[0].split("PT")[0]);
                int minutes = Integer.parseInt( s[1].split("M")[0] );
                int result = 60 * hours + minutes;
                loadedTask.setDuration(Duration.ofMinutes(result));
            } else {
                loadedTask.setDuration(Duration.ofMinutes(Long.parseLong(taskArray[5].split("PT")[1].split("M")[0])));       // распарсить строку в дюрацию
            }
        }
        if (taskArray[6].equals("null")) {
            loadedTask.setStartTime(null);
        } else {
            loadedTask.setStartTime(LocalDateTime.parse(taskArray[6]));
        }
        return loadedTask;
    }
}
