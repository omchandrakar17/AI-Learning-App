package ui.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class AIChatService {

    private static final String API_KEY = "api key";

    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-preview:generateContent?key=" + API_KEY;

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getResponse(String userMessage) {

        try {

            // ✅ Gemini request format
            JSONObject body = new JSONObject();

            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();

            JSONArray parts = new JSONArray();
            parts.put(new JSONObject().put("text", userMessage));

            content.put("parts", parts);
            contents.put(content);

            body.put("contents", contents);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "API Error: " + response.body();
            }

            JSONObject json = new JSONObject(response.body());

            return json
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}