package ru.yandex.praktikum.ivanov.kanban.testManagers;

import com.google.gson.Gson;
import ru.yandex.praktikum.ivanov.kanban.server.HttpTaskServer;
import ru.yandex.praktikum.ivanov.kanban.server.KVServer;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpServer = new HttpTaskServer();
        httpServer.start();

        Task task1 = new Task("task1", "descriptionTask1", Status.NEW);
        Epic epic1 = new Epic("epic1", "description1", new ArrayList<>());
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", Status.NEW, epic1);
    }
}
