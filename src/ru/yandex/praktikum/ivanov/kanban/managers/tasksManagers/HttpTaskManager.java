package ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers;

import com.google.gson.*;
import ru.yandex.praktikum.ivanov.kanban.server.KVTaskClient;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.IOException;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTaskManager {
    private KVTaskClient client;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        client = new KVTaskClient(url);
    }

    public String getToken() {
        return client.API_TOKEN;
    }

    @Override
    protected void save() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gsonTask = gsonBuilder.create();

        ArrayList<String> managerList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            for (Task task : getTaskList()) {
                String jsonTask = gsonTask.toJson(task);
                managerList.add(jsonTask);
            }

            for (Epic epic : getEpicList()) {
                if (epic.getEpicTaskList().isEmpty()) {
                    String jsonEpic = gsonTask.toJson(epic);
                    managerList.add(jsonEpic);
                } else {
                    for (Subtask subtask: epic.getEpicTaskList()) {
                        epic.getEpicIDTaskList().add(subtask.getId());
                    }
                    epic.getEpicTaskList().clear();
                    String jsonEpic = gsonTask.toJson(epic);
                    managerList.add(jsonEpic);
                }
            }

            for (Subtask subtask : getSubtaskList()) {
                subtask.setEpic(null);
                String jsonSubtask = gsonTask.toJson(subtask);
                managerList.add(jsonSubtask);
            }

            ArrayList<Integer> historyList = new ArrayList<>();
            for (Task task: getHistoryObject().getHistory()) {
                historyList.add(task.getId());
            }
            String jsonHistory = gsonTask.toJson("customList:" + historyList);

            managerList.add(jsonHistory);
            String jsonManager = gson.toJson(managerList);
            client.put(client.API_TOKEN, jsonManager);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка сохранения на сервер");
        }
    }

    public void load() {
        String input = client.load(client.API_TOKEN);
        String[] dividedJson = input.split("\",\"\"customList:");

        Gson gson = new Gson();
        String taskArray = dividedJson[0] + "]";
        JsonElement jsonElement = JsonParser.parseString(taskArray);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).toString().contains("subtaskType")) {
                Subtask subtask = gson.fromJson(jsonArray.get(i), Subtask.class);
                int epicId = subtask.getSubtasksEpicId();
                subtask.setEpic(getEpicMap().get(epicId));                      // вызов эпика из мапы по Id
                getSubtaskMap().put(subtask.getId(), subtask);
                subtask.getEpic().getEpicTaskList().add(subtask);               // взять эпик и добавить в него сабтаск
            }
            else if (jsonArray.get(i).toString().contains("epicType")) {
                Epic epic = gson.fromJson(jsonArray.get(i), Epic.class);
                getEpicMap().put(epic.getId(), epic);
            }
            else {
                Task task = gson.fromJson(jsonArray.get(i), Task.class);
                getTaskMap().put(task.getId(), task);
            }
        }

        for (char historyId: dividedJson[1].toCharArray()) {
            try {
                int i = Integer.parseInt(String.valueOf(historyId));
                if (getTaskMap().containsKey(i)) {
                    getHistoryObject().add(getTaskMap().get(i));
                } else if (getEpicMap().containsKey(i)) {
                    getHistoryObject().add(getEpicMap().get(i));
                } else if (getSubtaskMap().containsKey(i)) {
                    getHistoryObject().add(getSubtaskMap().get(i));
                }
            } catch (NumberFormatException e) {}
        }
    }

}