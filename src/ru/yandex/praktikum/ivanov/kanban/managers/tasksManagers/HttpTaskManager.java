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
        load();
    }

    @Override
    protected void save() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();

        try {
            client.put(KEY_TASKS, gson.toJson(getTaskList()));
            for (Epic epic: getEpicList()) {
                for (Subtask subtask: epic.getEpicTaskList()) {
                    epic.getEpicTaskListID().add(subtask.getId());
                }
                epic.getEpicTaskList().clear();
            }
            client.put(KEY_EPICS, gson.toJson(getEpicList()));
            client.put(KEY_SUBTASKS, gson.toJson(getSubtaskList()));
            client.put(KEY_HISTORY, gson.toJson(getHistoryObject().getHistory()));

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка сохранения на сервер");
        }
    }

    public void load() {
        Gson gson = new Gson();
        String loadedString;
        if ((loadedString = client.load(KEY_TASKS)) != null) {
            JsonArray jsonTasksArray = JsonParser.parseString(loadedString).getAsJsonArray();
            for (int i = 0; i < jsonTasksArray.size(); i++) {
                Task task = gson.fromJson(jsonTasksArray.get(i), Task.class);
                getTaskMap().put(task.getId(), task);
            }
        }

        if ((loadedString = client.load(KEY_EPICS)) != null) {
            JsonArray jsonEpicsArray = JsonParser.parseString(loadedString).getAsJsonArray();
            for (int i = 0; i < jsonEpicsArray.size(); i++) {
                Epic epic = gson.fromJson(jsonEpicsArray.get(i), Epic.class);
                getEpicMap().put(epic.getId(), epic);
            }
        }

        if ((loadedString = client.load(KEY_SUBTASKS)) != null) {
            JsonArray jsonSubtasksArray = JsonParser.parseString(loadedString).getAsJsonArray();
            for (int i = 0; i < jsonSubtasksArray.size(); i++) {
                Subtask subtask = gson.fromJson(jsonSubtasksArray.get(i), Subtask.class);
                int epicId = subtask.getSubtasksEpicId();
                subtask.setEpic(getEpicMap().get(epicId));
                getSubtaskMap().put(subtask.getId(), subtask);
                subtask.getEpic().getEpicTaskList().add(subtask);
            }
        }

        if ((loadedString = client.load(KEY_SUBTASKS)) != null) {
            JsonArray jsonHistoryArray = JsonParser.parseString(loadedString).getAsJsonArray();
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

}