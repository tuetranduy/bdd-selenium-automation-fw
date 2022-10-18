package org.unsplash.exercise.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.unsplash.exercise.ApiManager;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionApi {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = PropertyUtils.getProperty("url.api");
    ApiManager apiManager = new ApiManager(baseUrl, client);

    public String createCollection(String collectionName) throws IOException {
        RequestBody postData = new FormBody.Builder()
                .add("title", collectionName)
                .add("private", "true")
                .build();

        String resultString = apiManager.post("collections", postData).body().string();

        return JsonParser.parseString(resultString).getAsJsonObject().get("id").getAsString();
    }

    public List<String> getCollectionPhotos(String collectionId) throws IOException {
        String resultString = apiManager.get("collections/" + collectionId + "/photos").body().string();

        JsonArray listImages = JsonParser.parseString(resultString).getAsJsonArray();
        List<String> ids = new ArrayList<>();
        listImages.forEach(image -> ids.add(image.getAsJsonObject().get("id").getAsString()));

        return ids;
    }

    public void removePhotoFromCollection(String collectionId, String photoId) {
        RequestBody postData = new FormBody.Builder()
                .add("photo_id", photoId)
                .build();
        try {
            apiManager.delete("collections/" + collectionId + "/remove", postData);
        } catch (IOException exception) {
            LoggingManager.logError(getClass(), "Unable to delete photo with id: " + photoId, exception);
        }
    }

    public void deleteCollection(String collectionId) {
        try {
            apiManager.delete("collections/" + collectionId);
        } catch (IOException exception) {
            LoggingManager.logError(getClass(), "Unable to delete collection with id: " + collectionId, exception);
        }
    }

    public String getCollectionId(String collectionTitle, String username) throws IOException {
        String resultString = apiManager.get("users/" + username + "/collections").body().string();

        JsonArray listCollections = JsonParser.parseString(resultString).getAsJsonArray();
        JsonElement toBeRemovedCollection = null;

        for (int i = 0; i < listCollections.size(); i++) {
            if (listCollections.get(i).getAsJsonObject().get("title").getAsString().equals(collectionTitle)) {
                toBeRemovedCollection = listCollections.get(i);
                break;
            }
        }

        if (toBeRemovedCollection != null) {
            return toBeRemovedCollection.getAsJsonObject().get("id").getAsString();
        }

        return null;
    }
}
