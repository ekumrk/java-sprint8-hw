package web;

import exceptions.LoadException;
import exceptions.RegistrationAPIException;
import exceptions.SaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final HttpClient httpclient;
    private final String URL;
    private final String token;

    public KVTaskClient(String url) {
        httpclient = HttpClient.newHttpClient();
        this.URL = url;
        this.token = registerApiToken(URL);
    }

    private String registerApiToken(String URL) {
        try {
            URI url = URI.create(String.format("%s/register", URL));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url)
                    .build();
            HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RegistrationAPIException("Ошибка при регистрации API_TOKEN! Код ошибки: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RegistrationAPIException("Ошибка при регистрации API_TOKEN");
        }
    }

    public void saveToServer(String key, String json) {
        try {
            URI url = URI.create(String.format("%s/save/%s/?API_TOKEN=%s", URL, key, token));
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(url)
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            httpclient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new SaveException("Ошибка при загрузке данных на Сервер");
        }
    }

    public String load(String key) {
        try {
            URI url = URI.create(String.format("%s/load/%s/?API_TOKEN=%s", URL, key, token));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url)
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new LoadException("Ощибка при загрузке данных из Сервера");
        }
    }
}