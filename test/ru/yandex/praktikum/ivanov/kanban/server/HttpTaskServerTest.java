package ru.yandex.praktikum.ivanov.kanban.server;

import com.google.gson.*;
import ru.yandex.praktikum.ivanov.kanban.managers.Managers;
import ru.yandex.praktikum.ivanov.kanban.managers.tasksManagers.TaskManager;
import ru.yandex.praktikum.ivanov.kanban.tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class HttpTaskServerTest {

    private KVServer kvServer;
    private HttpTaskServer taskServer;
    private TaskManager taskManager;

    private final String path = "http://localhost:8080";

    Task task10 = new Task("task1", "descriptionTask1", Status.NEW);
    Task task20 = new Task("task2", "descriptionTask2", Status.NEW);
    Epic epic10 = new Epic("epic10", "description10", new ArrayList<>());
    Subtask subtaskNew10 = new Subtask("subtaskNew1", "descriptionSubtaskNew1", Status.NEW, epic10);
    Epic epic20 = new Epic("epic20", "description20", new ArrayList<>());
    Subtask subtaskNew20 = new Subtask("subtaskNew20", "descriptionSubtaskNew20", Status.NEW, epic20);

    Gson gson = new Gson();
    String taskJson10 = gson.toJson(task10);
    String taskJson20 = gson.toJson(task20);
    String epicJson10 = gson.toJson(epic10);
    String epicJson20 = gson.toJson(epic20);
    String subJson10 = gson.toJson(subtaskNew10);
    String subJson20 = gson.toJson(subtaskNew20);

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer();
        taskServer.start();
    }

    @AfterEach
    public void closeAll() {
        kvServer.stop(0);
        taskServer.stop(0);
    }

    @Test   // #1
    public void getTaskList_returnTaskListSize() throws URISyntaxException, IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder().uri(regURI)              // запрос добавления таск_20
                .POST(BodyPublishers.ofString(taskJson20))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");               // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson20))         // запрос добавления эпика_20
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))          // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(subJson20))          // запрос добавления сабтаска_20
                .uri(regURI)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        regURI = new URI(path + "/tasks/task/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения тасков
                .GET()
                .build();
        HttpResponse<String> responseTasks = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпиков
                .GET()
                .build();
        HttpResponse<String> responseEpics = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтасков
                .GET()
                .build();
        HttpResponse<String> responseSubs = client.send(request, handler);

        JsonElement jsonElementTask = JsonParser.parseString(responseTasks.body());
        JsonElement jsonElementEpic = JsonParser.parseString(responseEpics.body());
        JsonElement jsonElementSubtask = JsonParser.parseString(responseSubs.body());
        JsonArray jsonArrayTask = jsonElementTask.getAsJsonArray();
        JsonArray jsonArrayEpic = jsonElementEpic.getAsJsonArray();
        JsonArray jsonArraySubtask = jsonElementSubtask.getAsJsonArray();

        Assertions.assertEquals(2, jsonArrayTask.size());
        Assertions.assertEquals(2, jsonArrayEpic.size());
        Assertions.assertEquals(2, jsonArraySubtask.size());
        Assertions.assertEquals(200, responseTasks.statusCode());
        Assertions.assertEquals(200, responseEpics.statusCode());
        Assertions.assertEquals(200, responseSubs.statusCode());
    }

    @Test   // #2
    public void getTaskList_returnEmptyTaskList() throws URISyntaxException, IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)              // запрос получения тасков
                .GET()
                .build();
        HttpResponse<String> responseTasks = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпиков
                .GET()
                .build();
        HttpResponse<String> responseEpics = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтасков
                .GET()
                .build();
        HttpResponse<String> responseSubs = client.send(request, handler);

        Assertions.assertEquals("[]", responseTasks.body());
        Assertions.assertEquals("[]", responseEpics.body());
        Assertions.assertEquals("[]", responseSubs.body());
        Assertions.assertEquals(200, responseTasks.statusCode());
        Assertions.assertEquals(200, responseEpics.statusCode());
        Assertions.assertEquals(200, responseSubs.statusCode());
    }

    @Test   // #3 #4
    public void getTasksById_IncorrectId_ShouldReturnName() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");               // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))          // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения таск_10
                .GET()
                .build();
        HttpResponse<String> responseTask = client.send(request, handler);

        regURI = new URI(path + "/tasks/task/?id=10");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения некорректного таска
                .GET()
                .build();
        HttpResponse<String> responseWrongTask = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпика_10
                .GET()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/?id=20");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения некорректного эпика
                .GET()
                .build();
        HttpResponse<String> responseWrongEpic = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтаска_10
                .GET()
                .build();
        HttpResponse<String> responseSub = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/?id=30");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения некорректного сабтаска
                .GET()
                .build();
        HttpResponse<String> responseWrongSub = client.send(request, handler);

        JsonElement jsonElementTask = JsonParser.parseString(responseTask.body());
        JsonElement jsonElementEpic = JsonParser.parseString(responseEpic.body());
        JsonElement jsonElementSubtask = JsonParser.parseString(responseSub.body());

        String jsonNameTask = jsonElementTask.getAsJsonObject().get("name").toString();
        String jsonNameEpic = jsonElementEpic.getAsJsonObject().get("name").toString();
        String jsonNameSubtask = jsonElementSubtask.getAsJsonObject().get("name").toString();

        Assertions.assertEquals("\"task1\"", jsonNameTask);
        Assertions.assertEquals("\"epic10\"", jsonNameEpic);
        Assertions.assertEquals("\"subtaskNew1\"", jsonNameSubtask);
        Assertions.assertEquals("Task с таким ID не существует", responseWrongTask.body());
        Assertions.assertEquals("Epic с таким ID не существует", responseWrongEpic.body());
        Assertions.assertEquals("Subtask с таким ID не существует", responseWrongSub.body());

        Assertions.assertEquals(200, responseTask.statusCode());
        Assertions.assertEquals(200, responseEpic.statusCode());
        Assertions.assertEquals(200, responseSub.statusCode());
        Assertions.assertEquals(400, responseWrongTask.statusCode());
        Assertions.assertEquals(400, responseWrongEpic.statusCode());
        Assertions.assertEquals(400, responseWrongSub.statusCode());
    }

    @Test   // #5
    public void getEpicSubtasks_ShouldReturnName() throws URISyntaxException, IOException, InterruptedException {
        Subtask subtaskNew30 = new Subtask("subtaskNew30", "descriptionSubtaskNew30", Status.NEW, epic10);
        String subJson30 = gson.toJson(subtaskNew30);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/epic/");                // запрос добавления эпика_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))                // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson30))                // запрос добавления сабтаска_30
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/epic/?id=1");
        request = HttpRequest.newBuilder().uri(regURI)                   // запрос получения списка сабтасков для эпика
                .GET()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);      // ЗДЕСЬ ОШИБКА

        JsonElement jsonElementEpicTaskList = JsonParser.parseString(responseEpic.body());
        JsonArray jsonArrayEpicTaskList = jsonElementEpicTaskList.getAsJsonArray();

        Assertions.assertEquals(2, jsonArrayEpicTaskList.size());
        Assertions.assertEquals(200, responseEpic.statusCode());
    }

    @Test   // #6
    public void getEpicSubtasks_WithoutSubtasks_ShouldReturnEmptySubtaskList() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/epic/");                // запрос добавления эпика_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/epic/?id=1");
        request = HttpRequest.newBuilder().uri(regURI)                   // запрос получения списка сабтасков для эпика
                .GET()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);

        Assertions.assertEquals("[]", responseEpic.body());
        Assertions.assertEquals(200, responseEpic.statusCode());
    }

    @Test   // #7
    public void getEpicSubtasks_WithoutEpics_ShouldReturnEmptyResponse() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)                   // запрос получения списка сабтасков для эпика
                .GET()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);

        Assertions.assertEquals("Список эпиков пуст", responseEpic.body());
        Assertions.assertEquals(400, responseEpic.statusCode());
    }

    @Test   // #8
    public void getEpicSubtasks_IncorrectEpicId_ShouldReturnIncorrectResponse() throws URISyntaxException, IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/epic/");                // запрос добавления эпика_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))                // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/epic/?id=10");
        request = HttpRequest.newBuilder().uri(regURI)                   // запрос получения списка сабтасков для эпика
                .GET()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);

        Assertions.assertEquals("Эпика с таким Id не существует", responseEpic.body());
        Assertions.assertEquals(400, responseEpic.statusCode());
    }

    @Test   // #9
    public void getHistory_ShouldReturnHistorySize() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");                // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))           // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения таск_10
                .GET()
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпика_10
                .GET()
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтаска_10
                .GET()
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/history/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения истории
                .GET()
                .build();
        HttpResponse<String> responseHistory = client.send(request, handler);

        JsonElement jsonElementHistory = JsonParser.parseString(responseHistory.body());
        JsonArray jsonArrayHistory = jsonElementHistory.getAsJsonArray();

        Assertions.assertEquals(3, jsonArrayHistory.size());
        Assertions.assertEquals(200, responseHistory.statusCode());
    }

    @Test   // #10
    public void getHistory_ShouldReturnEmptyHistory() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");                // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))           // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/history/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения истории
                .GET()
                .build();
        HttpResponse<String> responseHistory = client.send(request, handler);

        Assertions.assertEquals("[]", responseHistory.body());
        Assertions.assertEquals(200, responseHistory.statusCode());
    }

    @Test   // #11
    public void getPrioritizedTasks_ShouldReturnPrioritizedList() throws URISyntaxException, IOException, InterruptedException {
        task10.setStartTime(LocalDateTime.now().plus(Duration.ofMinutes(30)));
        task10.setDuration(Duration.ofMinutes(15));
        subtaskNew10.setStartTime(LocalDateTime.now());
        subtaskNew10.setDuration(Duration.ofMinutes(15));

        String taskJsonTime10 = gson.toJson(task10);
        String taskEpicTime10 = gson.toJson(epic10);
        String subJsonTime10 = gson.toJson(subtaskNew10);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJsonTime10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");               // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskEpicTime10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос добавления сабтаска_10
                .POST(BodyPublishers.ofString(subJsonTime10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения приоритетного списка
                .GET()
                .build();
        HttpResponse<String> responsePriority = client.send(request, handler);

        JsonElement jsonElementPriority = JsonParser.parseString(responsePriority.body());
        JsonArray jsonArrayPriority = jsonElementPriority.getAsJsonArray();

        Assertions.assertEquals(2, jsonArrayPriority.size());
        Assertions.assertEquals("\"subtaskNew1\"", jsonArrayPriority.get(0).getAsJsonObject().get("name").toString());
        Assertions.assertEquals("\"task1\"", jsonArrayPriority.get(1).getAsJsonObject().get("name").toString());
        Assertions.assertEquals(200, responsePriority.statusCode());
    }

    @Test   // #12
    public void getPrioritizedTasks_ShouldReturnEmptyPrioritizedList() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)              // запрос получения приоритетного списка
                .GET()
                .build();
        HttpResponse<String> responsePriority = client.send(request, handler);

        Assertions.assertEquals("[]", responsePriority.body());
        Assertions.assertEquals(200, responsePriority.statusCode());
    }

    @Test   // #13
    public void deleteAllTasks_ShouldReturnEmptyTaskList() throws URISyntaxException, IOException, InterruptedException  {     // УДАЛЕНИЕ ВСЕХ ЗАДАЧ
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder().uri(regURI)              // запрос добавления таск_20
                .POST(BodyPublishers.ofString(taskJson20))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");               // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson20))         // запрос добавления эпика_20
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(subJson10))          // запрос добавления сабтаска_10
                .build();
        client.send(request, handler);

        request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(subJson20))          // запрос добавления сабтаска_20
                .uri(regURI)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        regURI = new URI(path + "/tasks/task/");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления тасков
                .DELETE()
                .build();
        HttpResponse<String> responseDelTasks = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления эпиков
                .DELETE()
                .build();
        HttpResponse<String> responseDelEpics = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления сабтасков
                .DELETE()
                .build();
        HttpResponse<String> responseDelSubs = client.send(request, handler);

        regURI = new URI(path + "/tasks/task/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения тасков
                .GET()
                .build();
        HttpResponse<String> responseEmptyTasks = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпиков
                .GET()
                .build();
        HttpResponse<String> responseEmptyEpics = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтасков
                .GET()
                .build();
        HttpResponse<String> responseEmptySubs = client.send(request, handler);

        Assertions.assertEquals(200, responseDelTasks.statusCode());
        Assertions.assertEquals(200, responseDelEpics.statusCode());
        Assertions.assertEquals(200, responseDelSubs.statusCode());
        Assertions.assertEquals("[]", responseEmptyEpics.body());
        Assertions.assertEquals("[]", responseEmptyTasks.body());
        Assertions.assertEquals("[]", responseEmptySubs.body());
    }

    @Test   // #14 #15
    public void deleteTasksById_IncorrectId_ShouldReturnEmptyTaskList() throws URISyntaxException, IOException, InterruptedException {     // УДАЛЕНИЕ ЗАДАЧ ПО ID
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        URI regURI = new URI(path + "/tasks/task/");           // запрос добавления таск_10
        HttpRequest request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(taskJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");               // запрос добавления эпика_10
        request = HttpRequest.newBuilder().uri(regURI)
                .POST(BodyPublishers.ofString(epicJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос добавления сабтаска_10
                .POST(BodyPublishers.ofString(subJson10))
                .build();
        client.send(request, handler);

        regURI = new URI(path + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления таск_10 id=1
                .DELETE()
                .build();
        HttpResponse<String> responseTask = client.send(request, handler);

        regURI = new URI(path + "/tasks/task/?id=10");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления некорректного таска
                .DELETE()
                .build();
        HttpResponse<String> responseWrongTask = client.send(request, handler);

        regURI = new URI(path + "/tasks/task/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения тасков
                .GET()
                .build();
        HttpResponse<String> responseEmptyTasks = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления эпика_10 id=2
                .DELETE()
                .build();
        HttpResponse<String> responseEpic = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/?id=20");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления некорректного эпика
                .DELETE()
                .build();
        HttpResponse<String> responseWrongEpic = client.send(request, handler);

        regURI = new URI(path + "/tasks/epic/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения эпиков
                .GET()
                .build();
        HttpResponse<String> responseEmptyEpics = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления сабтаска_10 id=3
                .DELETE()
                .build();
        HttpResponse<String> responseSub = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/?id=30");
        request = HttpRequest.newBuilder().uri(regURI)             // запрос удаления некорректного сабтаска
                .DELETE()
                .build();
        HttpResponse<String> responseWrongSub = client.send(request, handler);

        regURI = new URI(path + "/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(regURI)              // запрос получения сабтасков
                .GET()
                .build();
        HttpResponse<String> responseEmptySub = client.send(request, handler);

        Assertions.assertEquals(200, responseTask.statusCode());
        Assertions.assertEquals("[]", responseEmptyTasks.body());
        Assertions.assertEquals("Task с таким id не существует", responseWrongTask.body());
        Assertions.assertEquals(400, responseWrongTask.statusCode());

        Assertions.assertEquals(200, responseEpic.statusCode());
        Assertions.assertEquals("[]", responseEmptyEpics.body());
        Assertions.assertEquals("Epic с таким id не существует", responseWrongEpic.body());
        Assertions.assertEquals(400, responseWrongEpic.statusCode());

        Assertions.assertEquals(400, responseSub.statusCode());         // сабтаск удален вместе с соответствующим эпиком
        Assertions.assertEquals("Subtask с таким id не существует", responseSub.body());
        Assertions.assertEquals("[]", responseEmptySub.body());
        Assertions.assertEquals("Subtask с таким id не существует", responseWrongSub.body());
        Assertions.assertEquals(400, responseWrongSub.statusCode());
    }

}