package main;

import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
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
        manager.createTask(task1);
        manager.createTask(task2);

        manager.createEpic(epic1);
        manager.createSubtask(subtask11);
        manager.createSubtask(subtask12);

        manager.createEpic(epic2);
        manager.createSubtask(subtask21);

/*        ТЕСТ СПРИНТ 3
        System.out.println(manager.getTaskList());
        manager.deleteTask(2);
        System.out.println(manager.getTaskList());

        System.out.println(manager.getEpicList());
        manager.deleteEpic(6);
        System.out.println(manager.getEpicList());

        System.out.println(manager.getSubtaskList());
        manager.deleteSubtask(5);
        System.out.println(manager.getSubtaskList());*/

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getEpicById(6);
        manager.getSubtaskById(7);
        manager.getEpicById(6);
        manager.getSubtaskById(5);
        manager.getSubtaskById(4);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getTaskById(1);

        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getId());
        }
    }
}
