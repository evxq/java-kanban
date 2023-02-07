package ru.yandex.praktikum.ivanov.kanban.server;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;


public class HttpTaskServer {

    private static final int PORT = 8080;

    private final HttpServer server;
    private final TaskManager httpManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new RequestHandler());
        httpManager = Managers.getDefault();
    }

    public void start() {
        server.start();
    }

    public void stop(int i) {
        server.stop(i);
    }

    class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().toString();
            System.out.println(path);
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    handleGetTasks(exchange, path);
                    break;

                case "POST":
                    handlePostTasks(exchange, path);
                    break;

                case "DELETE":
                    handleDeleteTasks(exchange, path);
                    break;

                default:
                    writeResponse(exchange,"Такого эндпоинта не существует", 404);
            }
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private void handleDeleteTasks(HttpExchange exchange, String path) throws IOException {
            int id;
            if (path.contains("/tasks/task/?id=")) {
                try {
                    String pathId = path.split("id=")[1];
                    id = Integer.parseInt(pathId);
                    httpManager.deleteTask(id);
                    writeResponse(exchange, "Удален Task id=" + id, 200);
                } catch (NullPointerException e) {
                    writeResponse(exchange, "Task с таким id не существует", 400);
                }
            } else if (path.contains("/tasks/subtask/?id=")) {
                try {
                    String pathId = path.split("id=")[1];
                    id = Integer.parseInt(pathId);
                    httpManager.deleteSubtask(id);
                    writeResponse(exchange, "Удален Subtask id=" + id, 200);
                } catch (NullPointerException e) {
                    writeResponse(exchange, "Subtask с таким id не существует", 400);
                }
            } else if (path.contains("/tasks/epic/?id=")) {
                try {
                    String pathId = path.split("id=")[1];
                    id = Integer.parseInt(pathId);
                    httpManager.deleteEpic(id);
                    writeResponse(exchange, "Удален Epic id=" + id, 200);
                } catch (NullPointerException e) {
                    writeResponse(exchange, "Epic с таким id не существует", 400);
                }
            } else if (path.equals("/tasks/task/")) {
                httpManager.deleteAllTasks();
                writeResponse(exchange, "Удалены все задачи", 200);
            } else if (path.equals("/tasks/subtask/")) {
                httpManager.deleteAllSubtasks();
                writeResponse(exchange, "Удалены все подзадачи", 200);
            } else if (path.equals("/tasks/epic/")) {
                httpManager.deleteAllEpics();
                writeResponse(exchange, "Удалены все эпики", 200);
            } else {
                writeResponse(exchange, "Путь не соответствует шаблону", 400);
            }
        }

        private void handlePostTasks(HttpExchange exchange, String path) throws IOException {
            Gson gson = new Gson();
            try {
                InputStream stream = exchange.getRequestBody();
                String jsonBody = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

                if (Pattern.matches("/tasks/epic/\\d+", path)) {
                    Epic epic = gson.fromJson(jsonBody, Epic.class);
                    httpManager.updateEpic(epic);
                    writeResponse(exchange,"Обновлен Epic id=" + epic.getId(), 200);
                }
                else if (path.contains("/tasks/epic/")) {                                            // POST /tasks/task/ Body: {..}
                    Epic epic = gson.fromJson(jsonBody, Epic.class);
                    /*if (epic.getId() != null) {
                        httpManager.updateEpic(epic);
                        writeResponse(exchange,"Обновлен Epic id=" + epic.getId(), 200);
                    } else {*/
                        Epic newEpic = httpManager.createEpic(epic);
                        writeResponse(exchange,"Создан Epic id=" + newEpic.getId(), 200);
//                    }
                }
                else if (Pattern.matches("/tasks/subtask/\\d+", path)) {
                    Subtask subtask = gson.fromJson(jsonBody, Subtask.class);
                    httpManager.updateSubtask(subtask);
                    writeResponse(exchange,"Обновлен Subtask id=" + subtask.getId(), 200);
                }
                else if (path.contains("/tasks/subtask/")) {                                       // POST /tasks/task/ Body: {..}
                    Subtask subtask = gson.fromJson(jsonBody, Subtask.class);
                    /*if (subtask.getId() != null) {
                        httpManager.updateSubtask(subtask);
                        writeResponse(exchange,"Обновлен Subtask id=" + subtask.getId(), 200);
                    } else {*/
                        Subtask newSubtask = httpManager.createSubtask(subtask);
                        writeResponse(exchange,"Создан Subtask id=" + newSubtask.getId(), 200);
//                    }
                }
                else if (Pattern.matches("/tasks/task/\\d+", path)) {
                    Task task = gson.fromJson(jsonBody, Task.class);
                    httpManager.updateTask(task);
                    writeResponse(exchange,"Обновлен Task id=" + task.getId(), 200);
                }
                else if (path.contains("/tasks/task/")) {                                       // POST /tasks/task/ Body: {..}
                    Task task = gson.fromJson(jsonBody, Task.class);
                    /*if (task.getId() != null) {
                        httpManager.updateTask(task);
                        writeResponse(exchange,"Обновлен Task id=" + task.getId(), 200);
                    } else {*/
                        Task newTask = httpManager.createTask(task);
                        writeResponse(exchange,"Создан Task id=" + newTask.getId(), 200);
//                    }
                }
                else {
                    writeResponse(exchange,"Путь не соответствует шаблону", 400);
                }
            } catch (JsonSyntaxException j) {
                writeResponse(exchange,"Получен некорректный JSON", 400);
            }
        }

        private void handleGetTasks(HttpExchange exchange, String path) throws IOException {
            Gson gson = new Gson();

            if (path.equals("/tasks/task/")) {                                                    // GET /tasks/task/
                String taskListJson = gson.toJson(httpManager.getTaskList());
                writeResponse(exchange, taskListJson, 200);
            }
            else if (path.equals("/tasks/subtask/")) {                                            // GET /tasks/subtask/
                String subtaskListJson = gson.toJson(httpManager.getSubtaskList());
                writeResponse(exchange, subtaskListJson, 200);
            }
            else if (path.equals("/tasks/epic/")) {                                               // GET /tasks/epic/
                String epicListJson = gson.toJson(httpManager.getEpicList());
                writeResponse(exchange, epicListJson, 200);
            }
            else if (path.contains("/tasks/task/?id=")) {                                         // GET /tasks/task/?id=
                String pathId = path.split("id=")[1];
                int id = Integer.parseInt(pathId);
                Task task = httpManager.getTaskById(id);
                if (task == null) {
                    writeResponse(exchange, "Task с таким ID не существует", 400);
                } else {
                    String taskJson = gson.toJson(task);
                    writeResponse(exchange, taskJson, 200);
                }
            }
            else if (path.contains("/tasks/subtask/?id=")) {                                      // GET /tasks/subtask/?id=
                String pathId = path.split("id=")[1];
                int id = Integer.parseInt(pathId);
                Subtask subtask = httpManager.getSubtaskById(id);
                if (subtask == null) {
                    writeResponse(exchange, "Subtask с таким ID не существует", 400);
                } else {
                    String subtaskJson = gson.toJson(subtask);
                    writeResponse(exchange, subtaskJson, 200);
                }
            }
            else if (path.contains("/tasks/epic/?id=")) {                                         // GET /tasks/epic/?id=
                String pathId = path.split("id=")[1];
                int id = Integer.parseInt(pathId);
                Epic epic = httpManager.getEpicById(id);
                if (epic == null) {
                    writeResponse(exchange, "Epic с таким ID не существует", 400);
                } else {
                    String epicJson = gson.toJson(epic);
                    writeResponse(exchange, epicJson, 200);
                }
            }
            else if (path.contains("/tasks/subtask/epic/?id=")) {                                 // GET /tasks/subtask/epic/?id=
                String pathId = path.split("id=")[1];
                int id = Integer.parseInt(pathId);
                if (httpManager.getEpicList().isEmpty()) {
                    writeResponse(exchange, "Список эпиков пуст", 400);
                } else {
                    for (Subtask sub : httpManager.getSubtaskList()) {
                        if (sub.getSubtasksEpicId() == id) {
                            for (Epic epic : httpManager.getEpicList()) {
                                if (epic.getId() == id) {
                                    epic.getEpicTaskList().add(sub);
                                }
                            }
                        }
                        sub.setEpic(null);
                    }
                    ArrayList<Subtask> subtasksArray = httpManager.getEpicSubtasks(id);
                    if (subtasksArray == null) {
                        writeResponse(exchange, "Эпика с таким Id не существует", 400);
                    } else {
                        String epicSubtasksJson = gson.toJson(subtasksArray);
                        writeResponse(exchange, epicSubtasksJson, 200);
                    }
                }
            }
            else if (path.equals("/tasks/history/")) {                                          // GET /tasks/history
                String history = gson.toJson(httpManager.getHistory());
                writeResponse(exchange, history, 200);
            }
            else if (path.equals("/tasks/")) {                                                  // GET /tasks/
                    String priority = gson.toJson(httpManager.getPrioritizedTasks());
                writeResponse(exchange, priority, 200);
            }
            else {
                writeResponse(exchange,"Путь не соответствует шаблону", 400);
            }
        }

    }
}
