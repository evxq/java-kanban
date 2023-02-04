package ru.yandex.praktikum.ivanov.kanban.testManagers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
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
        Task task2 = new Task("task2", "descriptionTask2", Status.DONE);

        Gson gson = new Gson();
        System.out.println(gson.toJson(task1));
        System.out.println(gson.toJson(task2));
    }
}
