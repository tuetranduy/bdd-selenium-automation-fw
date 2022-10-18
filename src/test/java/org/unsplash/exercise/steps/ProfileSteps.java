package org.unsplash.exercise.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;
import org.unsplash.exercise.dtos.CollectionDto;
import org.unsplash.exercise.dtos.PhotoDto;
import org.unsplash.exercise.dtos.UserDto;
import org.unsplash.exercise.pages.HomePage;
import org.unsplash.exercise.pages.ProfilePage;
import org.unsplash.exercise.utils.AssertionUtils;
import org.unsplash.exercise.utils.PropertyUtils;

import java.util.List;

public class ProfileSteps {

    private UserDto userDto;
    private CollectionDto collectionDto;
    private PhotoDto photoDto;

    public ProfileSteps(UserDto userDto, CollectionDto collectionDto, PhotoDto photoDto) {
        this.userDto = userDto;
        this.collectionDto = collectionDto;
        this.photoDto = photoDto;
    }

    @Given("^I go to the Profile page(?:| again)$")
    public void goToProfilePage() {
        LoggingManager.logGiven(getClass(), "I go to the Profile page");

        HomePage homePage = new HomePage();

        homePage.goToProfilePage();
    }

    @When("I click Edit tags link")
    public void clickEditProfile() {
        LoggingManager.logWhen(getClass(), "I click Edit tags link");

        ProfilePage profilePage = new ProfilePage();

        profilePage.clickEditProfileButton();

        // set user initial data
        userDto.setFirstName(profilePage.getUserFirstName());
        userDto.setLastName(profilePage.getUserLastName());
        userDto.setUsername(profilePage.getCurrentUserName());
    }


    @And("I edit the username field")
    public void editUserNameField() {
        LoggingManager.logAnd(getClass(), "I edit the username field");

        ProfilePage profilePage = new ProfilePage();

        profilePage.setRandomUserName();
    }

    @And("I click the Update Account button")
    public void updateAccountInformation() {
        LoggingManager.logAnd(getClass(), "I click the Update Account button");

        ProfilePage profilePage = new ProfilePage();

        profilePage.clickUpdateProfile();
        // set username after updated
        userDto.setUsername(profilePage.getCurrentUserName());
    }

    @Then("I observe that it will take me to the Profile page")
    public void verifyUserIsInProfilePage() {
        LoggingManager.logThen(getClass(), "I observe that it will take me to the Profile page");

        String currentUrl = WebDriverManager.getWebDriver().getCurrentUrl();
        String expectedUrl = PropertyUtils.getProperty("url") + "@" + userDto.getUsername();

        AssertionUtils.assertTrue(getClass(), currentUrl.equalsIgnoreCase(expectedUrl), "URL should match");
    }

    @And("My full name is displayed correctly")
    public void myFullNameIsDisplayedCorrectly() {
        LoggingManager.logAnd(getClass(), "I verify that My full name is displayed correctly");

        ProfilePage profilePage = new ProfilePage();

        AssertionUtils.assertTrue(getClass(), profilePage.verifyUserFullName(userDto.getUserFullName()), "User Full Name should be displayed");
    }

    @Then("^I see the number of likes is (.*)$")
    public void verifyLikeNumber(int likes) {
        LoggingManager.logThen(getClass(), "I see the number of likes is " + likes);

        ProfilePage profilePage = new ProfilePage();

        AssertionUtils.assertTrue(getClass(), profilePage.getLikeNumber() == likes, "Like number should be equal");
    }

    @And("^(.*) photos appear in Likes section$")
    public void photosAppearInLikesSection(int numberOfPhotos) {
        LoggingManager.logAnd(getClass(), numberOfPhotos + " photos appear in Likes section");

        ProfilePage profilePage = new ProfilePage();

        AssertionUtils.assertTrue(getClass(), profilePage.getTotalLikedImages() == numberOfPhotos, "Number of liked photo should be equal");
    }

    @When("I go to created collection")
    public void goToCollectionByUrl() {
        LoggingManager.logWhen(getClass(), "I go to created collection");

        WebDriverManager.getWebDriver().get("https://unsplash.com/collections/" + collectionDto.getId());
    }

    @Then("^I notice that the photo has been removed successfully from the collection$")
    public void verifyPhotoIsRemovedFromCollection() {
        LoggingManager.logThen(getClass(), "I notice that the photo has been removed successfully from the collection");

        ProfilePage profilePage = new ProfilePage();

        List<String> expectedPhotos = photoDto.getIds();
        List<String> actualPhotos = profilePage.getPhotosFromCollection(collectionDto.getId());

        AssertionUtils.assertTrue(getClass(), expectedPhotos.equals(actualPhotos), "Photo should be removed");
    }

    @And("^there is only (.*) remaining photo in the collection$")
    public void verifyRemainingPhotoInCollection(int remainingPhoto) {
        LoggingManager.logAnd(getClass(), "I verify there is only " + remainingPhoto + " remaining photo in the collection");
        HomePage homePage = new HomePage();

        AssertionUtils.assertTrue(getClass(), homePage.getNumberOfPresentPhoto() == remainingPhoto, "Number of photo should be equal");
    }
}
