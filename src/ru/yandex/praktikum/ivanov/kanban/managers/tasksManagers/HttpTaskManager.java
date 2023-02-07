package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import com.google.gson.*;
import ru.yandex.praktikum.ivanov.kanban.server.KVTaskClient;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.IOException;

public class HttpTaskManager extends FileBackedTaskManager {
    private final KVTaskClient client;
    private final static String KEY_TASKS = "tasks";
    private final static String KEY_EPICS = "epics";
    private final static String KEY_SUBTASKS = "subtasks";
    private final static String KEY_HISTORY = "history";

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        client = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gsonPlus = gsonBuilder.create();

        try {
            client.put(KEY_TASKS, gsonPlus.toJson(getTaskList()));
            for (Epic epic: getEpicList()) {
                for (Subtask subtask: epic.getEpicTaskList()) {
                    epic.getEpicIDTaskList().add(subtask.getId());
                }
                epic.getEpicTaskList().clear();
            }
            client.put(KEY_EPICS, gsonPlus.toJson(getEpicList()));
            client.put(KEY_SUBTASKS, gsonPlus.toJson(getSubtaskList()));
            client.put(KEY_HISTORY, gsonPlus.toJson(getHistoryObject().getHistory()));

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка сохранения на сервер");
        }
    }

    public void load() {
        Gson gson = new Gson();

        JsonArray jsonTasksArray = JsonParser.parseString(client.load(KEY_TASKS)).getAsJsonArray();
        for (int i = 0; i < jsonTasksArray.size(); i++) {
            Task task = gson.fromJson(jsonTasksArray.get(i), Task.class);
            getTaskMap().put(task.getId(), task);
        }

        JsonArray jsonEpicsArray = JsonParser.parseString(client.load(KEY_EPICS)).getAsJsonArray();
        for (int i = 0; i < jsonEpicsArray.size(); i++) {
            Epic epic = gson.fromJson(jsonEpicsArray.get(i), Epic.class);
            getEpicMap().put(epic.getId(), epic);
        }

        JsonArray jsonSubtasksArray = JsonParser.parseString(client.load(KEY_SUBTASKS)).getAsJsonArray();
        for (int i = 0; i < jsonSubtasksArray.size(); i++) {
            Subtask subtask = gson.fromJson(jsonSubtasksArray.get(i), Subtask.class);
            int epicId = subtask.getSubtasksEpicId();
            subtask.setEpic(getEpicMap().get(epicId));
            getSubtaskMap().put(subtask.getId(), subtask);
            subtask.getEpic().getEpicTaskList().add(subtask);
        }

        JsonArray jsonHistoryArray = JsonParser.parseString(client.load(KEY_HISTORY)).getAsJsonArray();
        for (int i = 0; i < jsonHistoryArray.size(); i++) {
            try {
                int historyId = Integer.parseInt(String.valueOf(jsonHistoryArray.get(i)));
                if (getTaskMap().containsKey(historyId)) {
                    getHistoryObject().add(getTaskMap().get(historyId));
                } else if (getEpicMap().containsKey(historyId)) {
                    getHistoryObject().add(getEpicMap().get(historyId));
                } else if (getSubtaskMap().containsKey(historyId)) {
                    getHistoryObject().add(getSubtaskMap().get(historyId));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

}