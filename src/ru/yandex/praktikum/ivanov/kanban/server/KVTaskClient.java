package ru.yandex.praktikum.ivanov.kanban.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    private URI url;
    public String API_TOKEN;
    HttpClient client;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        getToken(url);
    }

    private void getToken(String url) throws IOException, InterruptedException {
        this.url = URI.create(url);
        URI registeredURL = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(registeredURL).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                     // запрос на получение TОКЕНА
        API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {                                 // POST /save/<ключ>?API_TOKEN=
        URI postUrl = URI.create(url.toString() + "/save/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(postUrl)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                     // запрос на сохранение задачи

        if (response.statusCode() == 200) {
            System.out.println("Данные успешно записаны на сервер");
        } else {
            System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
        }
    }

    public String load(String key) {                                           // GET /load/<ключ>?API_TOKEN=
        try {
            URI loadUrl = URI.create(url.toString() + "/load/" + key + "?API_TOKEN=" + API_TOKEN);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(loadUrl)
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                 // запрос на получение задачи

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
