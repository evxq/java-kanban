package ru.yandex.praktikum.ivanov.kanban.testManagers;

import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.FileBackedTaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.Epic;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Subtask;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileManagerTest {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\ev\\dev\\java-kanban\\src\\ru\\yandex\\praktikum\\ivanov\\kanban\\save.csv");
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.getDefaultFile(file);
        LocalDateTime time = LocalDateTime.now();

        Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
            task1.setStartTime(time);
            task1.setDuration(Duration.ofMinutes(15));
        Task task2 = new Task("task2", "descriptionTask2", Status.DONE);                            // plus 30

        Epic epic1 = new Epic("epic1", "descriptionEpic1", new ArrayList<>());
        Subtask subtask11 = new Subtask("subtask11", "descriptionSubtask11", Status.DONE, epic1);   // plus 60
            subtask11.setStartTime(time.plus(Duration.ofMinutes(60)));
            subtask11.setDuration(Duration.ofMinutes(15));
        Subtask subtask12 = new Subtask("subtask12", "descriptionSubtask12", Status.NEW, epic1);    // plus 90
            subtask12.setStartTime(time.plus(Duration.ofMinutes(90)));
            subtask12.setDuration(Duration.ofMinutes(15));
        Subtask subtask13 = new Subtask("subtask13", "descriptionSubtask13", Status.NEW, epic1);    // plus 120
            subtask13.setStartTime(time.plus(Duration.ofMinutes(120)));
            subtask13.setDuration(Duration.ofMinutes(15));

        Epic epic2 = new Epic("epic2", "descriptionEpic2", new ArrayList<>());
        Subtask subtask21 = new Subtask("subtask21", "descriptionSubtask21", Status.IN_PROGRESS, epic2);    // plus 150
            subtask21.setStartTime(time.plus(Duration.ofMinutes(95)));     // пересечение с subtask12
            subtask21.setDuration(Duration.ofMinutes(15));

        // создаем задачи в менеджере работы с файлом
        fileTaskManager.createTask(task1);                              // 1
        fileTaskManager.createTask(task2);                              // 2

        fileTaskManager.createEpic(epic1);                              // 3
        fileTaskManager.createSubtask(subtask11);                       // 4
        fileTaskManager.createSubtask(subtask12);                       // 5
        fileTaskManager.createSubtask(subtask13);                       // 6

        fileTaskManager.createEpic(epic2);                              // 7
        fileTaskManager.createSubtask(subtask21);                       // 8

        // вызываем задачи, заполняем историю менеджера, обновляем файл сохранения
        fileTaskManager.getTaskById(1);
        fileTaskManager.getTaskById(1);
        fileTaskManager.getTaskById(2);
        fileTaskManager.getTaskById(2);
        fileTaskManager.getEpicById(3);
        fileTaskManager.getEpicById(3);
        fileTaskManager.getSubtaskById(4);
        fileTaskManager.getSubtaskById(4);
        fileTaskManager.getSubtaskById(5);
        fileTaskManager.getSubtaskById(6);
        fileTaskManager.getSubtaskById(6);
        fileTaskManager.getEpicById(7);
        fileTaskManager.getEpicById(7);
        fileTaskManager.getSubtaskById(8);
        fileTaskManager.getSubtaskById(8);

        System.out.println("\nСодержание менеджера истории:");
        for (Task task : fileTaskManager.getHistory()) {
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Приоритет задач:");
        int i = 1;
        for (Task task: fileTaskManager.getPrioritizedTasks()) {
            System.out.println(i + ". " + task);
            i++;
        }

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);        // создаем новый менеджер из файла
        System.out.println("\nСодержание истории менеджера, загруженного из файла:");
        for (Task task : loadedTaskManager.getHistory()) {
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }
    }
}
