package ru.yandex.praktikum.ivanov.kanban.managers.memoryManagers;

import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.HistoryManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.Epic;
import ru.yandex.praktikum.ivanov.kanban.tasks.Status;
import ru.yandex.praktikum.ivanov.kanban.tasks.Subtask;
import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileToSave;

    public FileBackedTaskManager(File file) {
        super();
        this.fileToSave = file;
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\ev\\dev\\java-kanban\\src\\ru\\yandex\\praktikum\\ivanov\\kanban\\save.csv");
        FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(file);

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

        for (Task task : fileTaskManager.getHistory()) {                     // проверка содержания менеджера истории
            System.out.print(task.getId() + " ");
        }
        System.out.println();

        FileBackedTaskManager loadedTaskManager = loadFromFile(file);        // создаем новый менеджер из файла

        for (Task task : loadedTaskManager.getHistory()) {                   // проверка содержания истории загруженного менеджера
            System.out.print(task.getId() + " ");
        }
    }

    public String toString(Task task) {                                      // сохранение задачи в строку
        if (task instanceof Epic) {
            return task.getId() + "," +
                   TasksTypes.EPIC + "," +
                   task.getName() + "," +
                   task.getStatus() + "," +
                   task.getDescription() + "\n";
        }
        if (task instanceof Subtask) {
            return task.getId() + "," +
                   TasksTypes.SUBTASK + "," +
                   task.getName() + "," +
                   task.getStatus() + "," +
                   task.getDescription() + "," +
                   ((Subtask) task).getEpic().getId() + "\n";
        }
        return task.getId() + "," +
               TasksTypes.TASK + "," +
               task.getName() + "," +
               task.getStatus() + "," +
               task.getDescription() + "\n";
    }

    public Task fromString(String value) {                                   // создание задачи из строки
        String[] taskArray = value.split(",");
        if (taskArray[1].equals("EPIC")) {
            Epic loadedEpic = new Epic(taskArray[2], taskArray[4], new ArrayList<>());
            getEpicMap().put(Integer.parseInt(taskArray[0]), loadedEpic);
            loadedEpic.setId(Integer.parseInt(taskArray[0]));
            return loadedEpic;
        }
        if (taskArray[1].equals("SUBTASK")) {
            Subtask loadedSubtask = new Subtask(taskArray[2],
                                                taskArray[4],
                                                Status.valueOf(taskArray[3]),
                                                getEpicMap().get(Integer.parseInt(taskArray[5])));
            loadedSubtask.getEpic().getEpicTaskList().add(loadedSubtask);
            loadedSubtask.setId(Integer.parseInt(taskArray[0]));
            return loadedSubtask;
        }
        Task loadedTask = new Task(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]));
        loadedTask.setId(Integer.parseInt(taskArray[0]));
        return loadedTask;
    }

    public static String historyToString(HistoryManager manager) {           // сохранение менеджера истории в строку
        String historyString = "";
        for (Task task: manager.getHistory()) {
            historyString += task.getId() + ",";
        }
        if (historyString.length() > 1) {
            historyString = historyString.substring(0, historyString.length() - 1);
        }
        return historyString;
    }

    public static List<Integer> historyFromString(String value) {            // восстановление списка истории из строки
        List<Integer> historyList = new ArrayList<>();
        String[] hisArr = value.split(",");
        for (String s: hisArr) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    public void save() {                                                     // СОХРАНЕНИЕ В ФАЙЛ
        Map<Integer, Task> tree = new TreeMap<>(super.getTaskMap());
        tree.putAll(super.getSubtaskMap());
        tree.putAll(super.getEpicMap());
        try (FileWriter writer = new FileWriter(fileToSave)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task: tree.values()) {
                writer.write(toString(task));
            }
            writer.write("\n");
            writer.write(historyToString(getHistoryObject()));
        } catch (IOException e) {
            System.out.println("Ошибка сохранения файла");
            e.printStackTrace();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {            // СОЗДАНИЕ МЕНЕДЖЕРА ИЗ ФАЙЛА
        FileBackedTaskManager newManager = new FileBackedTaskManager(file);
        Map<Integer, Task> loadedTaskMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = newManager.fromString(line);
                if (task instanceof Epic) {
                    newManager.getEpicMap().put(task.getId(), (Epic) task);
                    loadedTaskMap.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    newManager.getSubtaskMap().put(task.getId(), (Subtask) task);
                    loadedTaskMap.put(task.getId(), (Subtask) task);
                } else {
                    newManager.getTaskMap().put(task.getId(), task);
                    loadedTaskMap.put(task.getId(), task);
                }
            }
            for (Integer id: historyFromString(reader.readLine())) {
                newManager.getHistoryObject().add(loadedTaskMap.get(id));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
            e.printStackTrace();
        }
        return newManager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }
}
