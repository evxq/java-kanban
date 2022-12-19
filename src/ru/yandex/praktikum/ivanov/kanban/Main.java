package ru.yandex.praktikum.ivanov.kanban;

import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.FileBackedTaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        File file = new File("C:\\Users\\ev\\dev\\java-kanban\\src\\ru\\yandex\\praktikum\\ivanov\\kanban\\save.csv");
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.getDefaultFile(file);

        Task task1 = new Task("Купить кроссовки",
                          "Выделить время в течение недели, чтобы сходить в магазин обуви",
                                      Status.NEW);
        Task task2 = new Task("Повесить картину",
                          "Подобрать место на стене, забить гвоздь и повесить картину",
                                     Status.DONE);

        Epic epic1 = new Epic("Приготовить обед", "Приготовить блюда и накрыть на стол", new ArrayList<>());
            Subtask subtask11 = new Subtask("Сварить суп",
                                        "Подготовить и нарезать все ингридиенты. Сварить в кастрюле",
                                                   Status.DONE, epic1);
            Subtask subtask12 = new Subtask("Приготовить второе блюдо",
                                        "Сварить рис и пожарить котлету",
                                                   Status.NEW, epic1);
            Subtask subtask13 = new Subtask("Накрыть на стол",
                                        "Разложить блюда в тарелки",
                                                   Status.NEW, epic1);


        Epic epic2 = new Epic("Выучить Java", "Выучить язык Java до уровня Junior", new ArrayList<>());
            Subtask subtask21 = new Subtask("Записаться на курсы",
                                        "Записаться на курсы Yandex Practicum",
                                                   Status.IN_PROGRESS, epic2);

        // создаем задачи в менеджере работы с файлом
        fileTaskManager.createTask(task1);          // 1
        fileTaskManager.createTask(task2);          // 2

        fileTaskManager.createEpic(epic1);          // 3
        fileTaskManager.createSubtask(subtask11);   // 4
        fileTaskManager.createSubtask(subtask12);   // 5
        fileTaskManager.createSubtask(subtask13);   // 6

        fileTaskManager.createEpic(epic2);          // 7
        fileTaskManager.createSubtask(subtask21);   // 8

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

        for (Task task : fileTaskManager.getHistory()) {         // проверка содержания менеджера истории
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }
        System.out.println();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);        // создаем новый менеджер из файла

        for (Task task : loadedTaskManager.getHistory()) {       // проверка содержания истории загруженного менеджера
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }
    }
}
