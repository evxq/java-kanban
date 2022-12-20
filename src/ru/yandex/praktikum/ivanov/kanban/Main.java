package ru.yandex.praktikum.ivanov.kanban;

import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

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

        taskManager.createTask(task1);          // 1
        taskManager.createTask(task2);          // 2

        taskManager.createEpic(epic1);          // 3
        taskManager.createSubtask(subtask11);   // 4
        taskManager.createSubtask(subtask12);   // 5
        taskManager.createSubtask(subtask13);   // 6

        taskManager.createEpic(epic2);          // 7
        taskManager.createSubtask(subtask21);   // 8

        // вызываем задачи, заполняем историю менеджера
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);
        taskManager.getEpicById(7);
        taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(8);

        for (Task task : taskManager.getHistory()) {
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }

        taskManager.deleteEpic(3);
        System.out.println();

        for (Task task : taskManager.getHistory()) {
            System.out.print(task.getId() + " ");
            System.out.println(task);
        }
    }
}
