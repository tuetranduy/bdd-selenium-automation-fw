package org.unsplash.exercise;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.IOException;

public class ApiManager {
    private final String baseUrl;
    private OkHttpClient client;
    private String authToken = "Bearer " + PropertyUtils.getProperty("api.access_token");

    public ApiManager(String baseUrl, OkHttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
    }

    public Response get(String path) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .addHeader("Authorization", authToken)
                .build();

        return client.newCall(request).execute();
    }

    public Response post(String path, RequestBody postData) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .addHeader("Authorization", authToken)
                .post(postData)
                .build();

        return client.newCall(request).execute();
    }

    public Response delete(String path) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .addHeader("Authorization", authToken)
                .delete()
                .build();

        return client.newCall(request).execute();
    }

    public Response delete(String path, RequestBody postData) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .addHeader("Authorization", authToken)
                .delete(postData)
                .build();

        return client.newCall(request).execute();
    }
}
