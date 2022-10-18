package org.unsplash.exercise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PhotoDto {

    @Getter
    @Setter
    private List<String> ids;

    @Getter
    @Setter
    private String id;

    public void removePhotoFromCollection(String photoId) {
        this.ids.removeIf(id -> id.equals(photoId));
    }
}
