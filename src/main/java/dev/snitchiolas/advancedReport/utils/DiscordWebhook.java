package dev.snitchiolas.advancedReport.utils;

import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    public static void sendWebhook(String url, String content) {
        try {
            URL webhookUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) webhookUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JsonObject json = new JsonObject();
            json.addProperty("content", content);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.toString().getBytes());
                os.flush();
            }

            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
