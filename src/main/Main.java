package main;

import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;

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

        Epic epic1 = new Epic("Приготовить обед", "Приготовить первое и второе", new ArrayList<>());
            Subtask subtask11 = new Subtask("Сварить суп",
                                         "Подготовить и нарезать все ингридиенты. Сварить в кастрюле",
                                          Status.DONE, epic1);
            Subtask subtask12 = new Subtask("Приготовить второе блюдо",
                                         "Сварить рис и пожарить котлету",
                                             Status.NEW, epic1);

        Epic epic2 = new Epic("Выучить Java", "Выучить язык Java до уровня Junior", new ArrayList<>());
            Subtask subtask21 = new Subtask("Записаться на курсы",
                                         "Записаться на курсы Yandex Practicum",
                                             Status.IN_PROGRESS, epic2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask11);
        taskManager.createSubtask(subtask12);

        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask21);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(7);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);

        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getId());
        }
    }
}
