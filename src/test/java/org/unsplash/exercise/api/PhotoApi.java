package org.unsplash.exercise.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import org.unsplash.exercise.ApiManager;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoApi {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = PropertyUtils.getProperty("url.api");
    ApiManager apiManager = new ApiManager(baseUrl, client);

    public List<String> getLikedPhotoIds(String username) throws IOException {
        String resultString = apiManager.get("users/" + username + "/likes").body().string();
        JsonArray listImages = JsonParser.parseString(resultString).getAsJsonArray();

        List<String> ids = new ArrayList<>();
        listImages.forEach(image -> ids.add(image.getAsJsonObject().get("id").getAsString()));

        return ids;
    }

    public void deleteLikedPhotos(List<String> photoIds) {
        photoIds.forEach(photoId -> {
            try {
                apiManager.delete("photos/" + photoId + "/like");
            } catch (IOException exception) {
                LoggingManager.logError(getClass(), "Unable to delete photo with id: " + photoId, exception);
            }
        });
    }

}
