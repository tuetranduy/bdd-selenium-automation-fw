package org.unsplash.exercise.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.api.CollectionApi;
import org.unsplash.exercise.api.PhotoApi;
import org.unsplash.exercise.dtos.CollectionDto;
import org.unsplash.exercise.dtos.PhotoDto;
import org.unsplash.exercise.pages.HomePage;

import java.io.IOException;
import java.util.List;

public class ApiSteps {

    private CollectionDto collectionDto;
    private PhotoDto photoDto;

    public ApiSteps(CollectionDto collectionDto, PhotoDto photoDto) {
        this.collectionDto = collectionDto;
        this.photoDto = photoDto;
    }

    @Given("Clean up liked photo")
    public void cleanUpLikedPhoto() {
        LoggingManager.logGiven(getClass(), "Cleaning up liked photo");

        PhotoApi photoApi = new PhotoApi();
        HomePage homePage = new HomePage();

        try {
            List<String> likedPhotoIds = photoApi.getLikedPhotoIds(homePage.getCurrentUserName());
            photoApi.deleteLikedPhotos(likedPhotoIds);
        } catch (IOException exception) {
            LoggingManager.logError(getClass(), "Unable to get liked photos", exception);
        }
    }

    @Given("I create a private collection")
    public void createPhotoCollection() {
        LoggingManager.logGiven(getClass(), "I create a private collection");

        CollectionApi collectionApi = new CollectionApi();
        HomePage homePage = new HomePage();

        collectionDto.setName("Automation Collection");

        try {
            // remove collection before create new collection
            String toBeRemovedCollectionId = collectionApi.getCollectionId(collectionDto.getName(), homePage.getCurrentUserName());
            collectionApi.deleteCollection(toBeRemovedCollectionId);

            String collectionId = collectionApi.createCollection(collectionDto.getName());
            collectionDto.setId(collectionId);
        } catch (IOException exception) {
            LoggingManager.logError(getClass(), "Unable to create collection", exception);
        }
    }

    @And("^I remove (.*) photo from the newly created collection$")
    public void removePhotoFromCollection(int times) {
        LoggingManager.logAnd(getClass(), "I remove " + times + " photo from the newly created collection");

        CollectionApi collectionApi = new CollectionApi();

        while (times != 0) {

            String toBeRemovedPhotoId = photoDto.getIds().get(0);
            photoDto.removePhotoFromCollection(toBeRemovedPhotoId);

            collectionApi.removePhotoFromCollection(collectionDto.getId(), toBeRemovedPhotoId);
            times--;
        }

    }
}
