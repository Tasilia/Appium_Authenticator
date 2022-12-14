package page;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Settings {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.cssSelector("[label = 'Settings']");
    By rateUsButton = By.cssSelector("[label = 'Rate us']");
    By rateUsButton2 = By.cssSelector("[label = 'Rate Us']");
    By shareButton = By.cssSelector("[label = 'Share']");
    By contactUsButton = By.cssSelector("[label = 'Contact us']");
    By privacyPolicyButton = By.cssSelector("[label = 'Privacy policy']");
    By termsOfUseButton = By.cssSelector("[label = 'Terms of use']");
    By upgradeButton = By.xpath("//XCUIElementTypeOther[@name='Upgrade to PRO Access To all features \nCancel anytime']");
    By notNowButton = By.cssSelector("[label = 'Not now']");

    public Settings(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.visibilityOfElementLocated(upgradeButton));
        Assertions.assertTrue(driver.findElement(title).isDisplayed());
        Assertions.assertTrue(driver.findElement(rateUsButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(shareButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(contactUsButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(privacyPolicyButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(termsOfUseButton).isDisplayed());
    }

    public void clickRateUsButton() {
        driver.findElement(rateUsButton).click();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("Do you like our App?"));
        Assertions.assertTrue(pageSource.contains("Your feedback is important for us!"));
    }

    public void clickShareButton() {
        driver.findElement(shareButton).click();
        Assertions.assertTrue(driver.findElement(shareButton).getAttribute("visible").equals("false"));
    }

    public void clickContactUsButton() {
        driver.findElement(contactUsButton).click();
        launchApp();
    }

    public void clickPrivacyPolicyButton() {
        driver.findElement(privacyPolicyButton).click();
        launchApp();
    }

    public void clickTermsOfUseButton() {
        driver.findElement(termsOfUseButton).click();
        launchApp();
    }

    public void launchApp() {
        try {
            Assertions.assertTrue(driver.getPageSource().contains("Safari"));
        } finally {
            driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                    "appstrain.test.authenticator"));
        }
    }

    public Paywall clickUpgradeButton() {
        driver.findElement(upgradeButton).click();
        return new Paywall(driver, wait);
    }

    public void clickNotNowButton() {
        driver.findElement(notNowButton).click();
        Assertions.assertTrue(driver.findElement(upgradeButton).getAttribute("visible").equals("true"));
    }

    public void confirmRateUs() {
        driver.findElement(rateUsButton2).click();
        Assertions.assertTrue(driver.findElement(upgradeButton).getAttribute("visible").equals("true"));
    }
}
