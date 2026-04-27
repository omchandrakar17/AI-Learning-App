package ui.services;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AIVideoService {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String generateVideo(String prompt) {
        try {
            // Encode the prompt
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);

            // Pollinations Video Endpoint
            // Note: Video generation takes longer than images (usually 10-20 seconds)
            String url = "https://image.pollinations.ai/prompt/" + encodedPrompt + "?width=1024&height=1024&model=flux&video=true";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            System.out.println("Generating video... please wait (this takes longer than images)");

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                return "Video API Error: " + response.statusCode();
            }

            // Save as .mp4
            byte[] videoBytes = response.body();
            String filePath = "generated_video.mp4";
            Files.write(Path.of(filePath), videoBytes);

            return "Video saved successfully at: " + filePath;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }
}