package org.unsplash.exercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;
import org.unsplash.exercise.api.CollectionApi;
import org.unsplash.exercise.utils.PropertyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ProfilePage extends BasePage {

    @FindBy(id = "user_username")
    private WebElement userNameInput;
    @FindBy(xpath = "//input[@value='Update account']")
    private WebElement updateAccountBtn;
    @FindBy(id = "user_first_name")
    private WebElement firstNameInput;
    @FindBy(id = "user_last_name")
    private WebElement lastNameInput;
    @FindBy(xpath = "//a[@data-test='user-nav-link-likes']")
    private WebElement likesLink;
    @FindBy(xpath = "//a[@data-test='user-nav-link-collections']")
    private WebElement collectionsLink;
    @FindBy(xpath = "//a[@data-test='user-nav-link-likes']//span//span")
    private WebElement likeNumberText;
    @FindBy(xpath = "//figure[@itemprop='image']")
    private List<WebElement> listLikedImages;

    By getEditProfileButtonLocator() {
        return By.xpath("//a[@href='" + PropertyUtils.getProperty("url") + "account']");
    }

    By getCollectionLinkLocator(String collectionName) {
        return By.xpath("//div[text()='" + collectionName + "']");
    }

    By getUserFullNameLocator(String fullName) {
        return By.xpath("//div[text()='" + fullName + "']");
    }

    public ProfilePage clickEditProfileButton() {
        click(getEditProfileButtonLocator(), "Unable to click Edit Profile button");

        return this;
    }

    public ProfilePage setRandomUserName() {
        Random rand = new Random();

        // create random username for each test iteration
        String randomUserName = "tuetranduy_" + rand.nextInt(999);
        setText(userNameInput, randomUserName, "unable to set username field");

        return this;
    }

    public String getCurrentUserName() {
        return getAttributeValue(userNameInput, "value", "Unable to get current username");
    }

    public ProfilePage clickUpdateProfile() {
        click(updateAccountBtn, "Unable to click Update Account button");

        return this;
    }

    public int getLikeNumber() {
        return Integer.parseInt(getText(likeNumberText, "Unable to get like number"));
    }

    public ProfilePage clickLikesLink() {
        click(likesLink, "Unable to click Likes link");

        return this;
    }

    public ProfilePage clickCollectionsLink() {
        waitUntilVisible(By.xpath("//div[contains(text(), 'Download free')]"), "Page is not fully loaded");
        click(collectionsLink, "Unable to click Collections link");

        return this;
    }

    public ProfilePage goToCollection(String collectionName) {
        click(getCollectionLinkLocator(collectionName), "Unable to go to collection " + collectionName);

        return this;
    }

    public String getUserFirstName() {
        return getAttributeValue(firstNameInput, "value", "Unable to get first name value");
    }

    public String getUserLastName() {
        return getAttributeValue(lastNameInput, "value", "Unable to get last name value");
    }

    public boolean verifyUserFullName(String fullName) {
        return WebDriverManager.getWebDriver().findElements(getUserFullNameLocator(fullName)).size() > 0;
    }

    public int getTotalLikedImages() {
        return listLikedImages.size();
    }

    public List<String> getPhotosFromCollection(String collectionId) {
        CollectionApi collectionApi = new CollectionApi();

        try {
            return collectionApi.getCollectionPhotos(collectionId);
        } catch (IOException exception) {
            LoggingManager.logError(getClass(), "Unable to get images of collection", exception);
        }

        return null;
    }
}
