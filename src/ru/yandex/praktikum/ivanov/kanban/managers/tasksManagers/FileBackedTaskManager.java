package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import ru.yandex.praktikum.ivanov.kanban.managers.historyManagers.HistoryManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileToSave;

    private FileBackedTaskManager(File file) {
        super();
        this.fileToSave = file;
    }

    public static FileBackedTaskManager getDefaultFile(File file) {
        return new FileBackedTaskManager(file);
    }

    private static String historyToString(HistoryManager manager) {           // сохранение менеджера истории в строку
        String historyString = "";
        for (Task task: manager.getHistory()) {
            historyString += task.getId() + ",";
        }
        if (historyString.length() > 1) {
            historyString = historyString.substring(0, historyString.length() - 1);
        }
        return historyString;
    }

    private static List<Integer> historyFromString(String value) {            // восстановление списка истории из строки
        List<Integer> historyList = new ArrayList<>();
        String[] hisArr = value.split(",");
        for (String s: hisArr) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    private void save() {                                                     // СОХРАНЕНИЕ В ФАЙЛ
        try (FileWriter writer = new FileWriter(fileToSave)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task: super.getTaskList()) {
                writer.write(StringManager.toString(task));
            }
            for (Epic epic: super.getEpicList()) {
                writer.write(StringManager.toString(epic));
            }
            for (Subtask subtask: super.getSubtaskList()) {
                writer.write(StringManager.toString(subtask));
            }
            writer.write("\n");
            writer.write(historyToString(getHistoryObject()));
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка сохранения файла!");
            } catch (ManagerSaveException exp) {
                System.out.println(exp.getMessage());
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {            // СОЗДАНИЕ МЕНЕДЖЕРА ИЗ ФАЙЛА
        FileBackedTaskManager newManager = new FileBackedTaskManager(file);
        Map<Integer, Task> loadedTaskMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();

                // Это условие прерывает цикл после окончания списка задач
                // я так понял, что для того и введена пустая строка в файл, чтобы помочь отделять задачи от истории
                if (line.isBlank()) {
                    break;
                }
                String[] taskArray = line.split(",");
                switch (taskArray[1]) {
                    case "EPIC":
                        Epic epic = StringManager.fromStringToEpic(taskArray, newManager);
                        newManager.getEpicMap().put(epic.getId(), epic);
                        loadedTaskMap.put(epic.getId(), epic);
                        break;
                    case "SUBTASK":
                        Subtask subtask = StringManager.fromStringToSubtask(taskArray, newManager);
                        newManager.getSubtaskMap().put(subtask.getId(), subtask);
                        loadedTaskMap.put(subtask.getId(), subtask);
                        break;
                    case "TASK":
                        Task task = StringManager.fromStringToTask(taskArray);
                        newManager.getTaskMap().put(task.getId(), task);
                        loadedTaskMap.put(task.getId(), task);
                        break;
                }
            }

            for (Integer id: historyFromString(reader.readLine())) {
                newManager.getHistoryObject().add(loadedTaskMap.get(id));
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка при чтении файла!");
            } catch (ManagerSaveException exp) {
                System.out.println(exp.getMessage());
            }
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

    // я понял это так: методы вызова задач меняют историю просмотров
    // поэтому обновленную историю требуется пересохранять
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
