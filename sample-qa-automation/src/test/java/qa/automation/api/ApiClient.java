package qa.automation.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import qa.automation.config.Config;
import okhttp3.*;
import qa.automation.reporting.Reporter;

import java.time.Duration;

public class ApiClient {
    private final OkHttpClient client;

    public ApiClient() {
        client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(30))
                .build();
    }

    public String get(String url) {
        try {
            Request.Builder rb = new Request.Builder().url(url);
            String bearer = Config.apiBearer();
            if (bearer != null && !bearer.isBlank()) rb.addHeader("Authorization", "Bearer " + bearer);

            Reporter.info("API GET: " + url);
            Response res = client.newCall(rb.build()).execute();
            String body = res.body() != null ? res.body().string() : "";
            Reporter.info("API Status: " + res.code());
            try {
                JsonObject pretty = JsonParser.parseString(body).getAsJsonObject();
                Reporter.info("API Body: <pre>" + pretty.toString() + "</pre>");
            } catch (Exception ex) {
                Reporter.info("API Body: " + body);
            }
            if (!res.isSuccessful()) throw new RuntimeException("API failed: " + res.code());
            return body;
        } catch (Exception e) {
            Reporter.fail("API error: " + e.getMessage());
            return null;
        }
    }

    public Response post(String url, String jsonBody) {
        try {
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + Config.apiBearer())
                    .build();
            Reporter.info("API POST: " + url);
            Reporter.info("API Body: " + body);
            Response response = client.newCall(request).execute();
            Reporter.info("API Status: " + response.code());
            return client.newCall(request).execute();

        } catch (Exception e) {
            Reporter.fail("API POST error: " + e.getMessage());
            return null;
        }

    }

    public String put(String url, String jsonBody) {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", "Bearer " + Config.apiBearer())
                .build();

        Reporter.info("API PUT: " + url);
        Reporter.info("API Body: " + body);

        try (Response res = client.newCall(request).execute()) {

            String responseData = (res.body() != null) ? res.body().string() : "";

            if (!res.isSuccessful()) {
                throw new RuntimeException("PUT failed with code: " + res.code() + " Body: " + responseData);
            }
            Reporter.info("API Status: " + res.code());
            return responseData;
        } catch (Exception e) {
            Reporter.fail("API PUT error: " + e.getMessage());
            return null;
        }
    }

    public String delete(String url) {
        try {
            Request.Builder rb = new Request.Builder()
                    .url(url)
                    .delete();

            String bearer = Config.apiBearer();
            if (bearer != null && !bearer.isBlank()) rb.addHeader("Authorization", "Bearer " + bearer);

            Reporter.info("API DELETE: " + url);
            Response res = client.newCall(rb.build()).execute();

            String body = res.body() != null ? res.body().string() : "";
            Reporter.info("API Status: " + res.code());


            if (!res.isSuccessful()) {

                if (body.toLowerCase().contains("not found for deletion.")) {
                    Reporter.info("Soft Warning: Attempted to delete a track that doesn't exist.");

                    return body;
                }
                throw new RuntimeException("API Delete failed with status " + res.code() + ": " + body);
            }
            return body;
        } catch (Exception e) {
            Reporter.fail("API DELETE error: " + e.getMessage());
            return null;
        }
    }
}