package org.unsplash.exercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.unsplash.exercise.LoggingManager;
import org.unsplash.exercise.WebDriverManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.unsplash.exercise.WebDriverManager.getWebDriver;

public class HomePage extends BasePage {

    @FindBy(xpath = "//a[@itemprop='contentUrl']")
    private List<WebElement> listImages;

    @FindBy(xpath = "//header//a[contains(@href, '/@')]//img")
    private WebElement userAvatarIcon;

    @FindBy(xpath = "//button[@title='Follow' or @title='Following']")
    private WebElement followBtn;

    @FindBy(xpath = "//*[name()='svg' and contains(.,'An X shape')]//parent::button")
    private WebElement closeButton;

    @FindBy(xpath = "//header//button[@title='Like']")
    private WebElement likeButton;

    @FindAll({
            @FindBy(xpath = "//button[@title='Your personal menu button']"),
            @FindBy(id = "user-dropdown")
    })
    private WebElement personalMenu;

    @FindBy(xpath = "//a[contains(.,'View profile')]")
    private WebElement viewProfileLink;

    @FindBy(xpath = "//a[@data-test='user-nav-link-likes']")
    private WebElement likesLink;

    @FindBy(xpath = "//header//button[@title='Add to collection']")
    private WebElement addToCollectionBtn;

    @FindBy(xpath = "//h3[text()='Add to Collection']")
    private WebElement addToCollectionText;

    @FindBy(xpath = "//a[@href='/logout']")
    private WebElement logOutLink;

    @FindBy(xpath = "//h1[text()=\"The internet’s source for visuals.\"]")
    private WebElement pageTitle;

    @FindBy(xpath = "//header//a[contains(.,'Download')]")
    private WebElement downloadButton;

    By getCollectionLocator(String collectionName) {
        return By.xpath("//button[contains(.,'" + collectionName + "')]");
    }

    public boolean isFollowed() {
        return getAttributeValue(followBtn, "title", "Unable to get attribute for Follow button").equalsIgnoreCase("following");
    }

    public HomePage hoverUserAvatar() {
        hoverOver(userAvatarIcon, "Unable to hover to user avatar");

        return this;
    }

    public HomePage clickPhotoByIndex(int index) {
        click(listImages.get(index), "Unable to click to first photo");

        return this;
    }

    public HomePage followUser() {

        // if current image's author is followed, skip and go to next image
        int imageIndex = 0;
        while (isFollowed()) {
            clickCloseButton();
            click(listImages.get(imageIndex), "Unable to click to photo");
            hoverOver(userAvatarIcon, "Unable to hover to user avatar");
            imageIndex++;
        }

        click(followBtn, "Unable to follow a user");

        return this;
    }

    public ProfilePage goToProfilePage() {
        click(personalMenu, "Unable to open Personal Menu");
        click(viewProfileLink, "Unable to click Profile link");

        return new ProfilePage();
    }

    public HomePage clickLikeButton() {
        click(likeButton, "Unable to like an image");

        return this;
    }

    public HomePage clickCloseButton() {
        click(closeButton, "Unable to close image");

        return this;
    }

    public HomePage clickAddToCollectionButton() {
        click(addToCollectionBtn, "Unable to click Add to Collection button");

        return this;
    }

    public HomePage chooseCollection(String collectionName) {
        waitUntilVisible(addToCollectionText, "Add to collection popup should visible");
        click(getCollectionLocator(collectionName), "Unable to choose collection " + collectionName);

        return this;
    }

    public String getDownloadLink() {
        waitUntilVisible(By.xpath("//div[@data-test='photos-route']"), "Photo pop-up should visible");

        return getAttributeValue(downloadButton, "href", "Unable to get download link");
    }

    public HomePage downloadPhoto(String downloadLink) {
        WebDriverManager.getWebDriver().navigate().to(downloadLink);

        return this;
    }

    public String getCurrentUserName() {
        click(personalMenu, "Unable to open menu");
        return getText(logOutLink, "Unable to get text from Logout link").split("@")[1];
    }

    public String getCurrentPhotoId() {
        try {
            URI currentPhotoUrl = new URI(getWebDriver().getCurrentUrl());
            String path = currentPhotoUrl.getPath();

            return path.substring(path.lastIndexOf('/') + 1);

        } catch (URISyntaxException exception) {
            LoggingManager.logError(getClass(), "Unable to get photo id", exception);

            return "";
        }
    }

    public int getNumberOfPresentPhoto() {
        return WebDriverManager.getWebDriver().findElements(By.xpath("//figure[@itemprop='image']")).size();
    }
}
