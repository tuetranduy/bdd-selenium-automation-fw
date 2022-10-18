package org.unsplash.exercise.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Keys;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.dtos.CollectionDto;
import org.unsplash.exercise.dtos.PhotoDto;
import org.unsplash.exercise.pages.HomePage;
import org.unsplash.exercise.utils.AssertionUtils;
import org.unsplash.exercise.utils.FileUtils;
import org.unsplash.exercise.utils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomePageSteps {
    private PhotoDto photoDto;
    private CollectionDto collectionDto;

    public HomePageSteps(PhotoDto photoDto, CollectionDto collectionDto) {
        this.photoDto = photoDto;
        this.collectionDto = collectionDto;
    }

    @Given("I click the first photo on home page")
    public void clickFirstPhoto() {
        LoggingManager.logGiven(getClass(), "I click the first photo on home page");

        HomePage homePage = new HomePage();

        int firstImageIndex = 0;
        homePage.clickPhotoByIndex(firstImageIndex);
    }

    @And("I hover on icon user at the top left corner")
    public void hoverUserIcon() {
        LoggingManager.logAnd(getClass(), "I hover on icon user at the top left corner");

        HomePage homePage = new HomePage();

        homePage.hoverUserAvatar();
    }

    @When("I click the Follow button")
    public void clickFollowButton() {
        LoggingManager.logWhen(getClass(), "I click the Follow button");

        HomePage homePage = new HomePage();

        homePage.followUser();
    }

    @Then("I verify that the Follow button text turn into Following")
    public void verifyUserIsFollowedSuccessfully() {
        LoggingManager.logThen(getClass(), "I verify that the Follow button text turn into Following");

        HomePage homePage = new HomePage();

        AssertionUtils.assertTrue(getClass(), homePage.isFollowed(), "User should be followed");
    }

    @And("^I like (.*) random photos$")
    public void likeRandomPhotos(int times) {
        LoggingManager.logAnd(getClass(), "I like " + times + " random photos");

        HomePage homePage = new HomePage();
        Random rand = new Random();

        while (times != 0) {
            homePage.clickPhotoByIndex(rand.nextInt(20));
            homePage.clickLikeButton().clickCloseButton();
            times--;
        }
    }

    @When("I go to Profile > Likes section")
    public void goToLikesSection() {
        LoggingManager.logWhen(getClass(), "I go to Profile > Likes section");

        HomePage homePage = new HomePage();

        // need additional refresh because Like number in the UI isn't update accordingly
        homePage.goToProfilePage().clickLikesLink().refresh();
    }

    @And("^I add (.*) random photos to the newly created collection$")
    public void addRandomPhotosToCollection(int times) {
        LoggingManager.logAnd(getClass(), "I add " + times + " random photos to the newly created collection");

        HomePage homePage = new HomePage();
        Random rand = new Random();

        List<String> photoIds = new ArrayList<>();

        while (times != 0) {
            homePage.clickPhotoByIndex(rand.nextInt(10));

            String currentPhotoId = homePage.getCurrentPhotoId();

            homePage.clickAddToCollectionButton()
                    .chooseCollection(collectionDto.getName())
                    .sendKeys(Keys.ESCAPE)
                    .sendKeys(Keys.ESCAPE);
            times--;

            photoIds.add(currentPhotoId);
        }

        photoDto.setIds(photoIds);
    }

    @Given("I open a random photo")
    public void openRandomPhoto() {
        LoggingManager.logGiven(getClass(), "I open a random photo");

        HomePage homePage = new HomePage();

        homePage.clickPhotoByIndex(0);
    }

    @When("I download this photo")
    public void downloadPhoto() {
        LoggingManager.logWhen(getClass(), "I download this photo");

        HomePage homePage = new HomePage();

        photoDto.setId(homePage.getCurrentPhotoId());

        String downloadLink = homePage.getDownloadLink();
        homePage.downloadPhoto(downloadLink);
    }

    @Then("I notice that the image is downloadable and the correct image has been saved")
    public void verifyImageIsSavedSuccessfully() {
        LoggingManager.logThen(getClass(), "I notice that the image is downloadable and the correct image has been saved");

        boolean result = FileUtils.isFileDownloaded(PropertyUtils.getBrowserDownloadsDirectory(), photoDto.getId());

        AssertionUtils.assertTrue(getClass(), result, "Photo should be downloaded");
    }
}
