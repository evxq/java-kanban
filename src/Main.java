import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Купить кроссовки",
                           "Выделить время в течение недели, чтобы сходить в магазин обуви",
                               "NEW");
        Task task2 = new Task("Повесить картину",
                           "Подобрать место на стене, забить гвоздь и повесить картину",
                               "DONE");

        Epic epic1 = new Epic("Приготовить обед", "Приготовить первое и второе", new ArrayList<>());
            Subtask subtask11 = new Subtask("Сварить суп",
                                         "Подготовить и нарезать все ингридиенты. Сварить в кастрюле",
                                             "DONE", epic1);
            Subtask subtask12 = new Subtask("Приготовить второе блюдо",
                                         "Сварить рис и пожарить котлету",
                                             "NEW", epic1);

        Epic epic2 = new Epic("Выучить Java", "Выучить язык Java до уровня Junior", new ArrayList<>());
            Subtask subtask21 = new Subtask("Записаться на курсы",
                                         "Записаться на курсы Yandex Practicum",
                                             "IN_PROGRESS", epic2);
        manager.createTask(task1);
        manager.createTask(task2);

        manager.createEpic(epic1);
        manager.createSubtask(subtask11);
        manager.createSubtask(subtask12);

        manager.createEpic(epic2);
        manager.createSubtask(subtask21);

        System.out.println(manager.getTaskList());
        manager.deleteTask(2);
        System.out.println(manager.getTaskList());

        System.out.println(manager.getSubtaskList());

        System.out.println(manager.getEpicList());
        manager.deleteEpic(6);
        System.out.println(manager.getEpicList());

    }
}
