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

public class AIImageService {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String generateImage(String prompt) {
        try {
            // Encode the prompt so spaces and special characters don't break the URL
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);

            // We use Pollinations.ai - it is free, fast, and needs no API key
            String url = "image url " + encodedPrompt + "?width=1024&height=1024&nologo=true&model=flux";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and get the image bytes
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                return "API Error: " + response.statusCode();
            }

            // Save the image
            byte[] imageBytes = response.body();
            String filePath = "generated_image.png";
            Files.write(Path.of(filePath), imageBytes);

            return "Image saved successfully at: " + filePath;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }
}