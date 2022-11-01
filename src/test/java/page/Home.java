package page;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

public class Home {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.xpath("//XCUIElementTypeStaticText[@name=\"Home\"]");
    By howToUseItButton = By.cssSelector("[label='How to use it']");
    By homeButton = By.xpath("//XCUIElementTypeOther[@name=\"Home\"])[2]");
    By secretFolderButton = By.xpath("//XCUIElementTypeOther[@name=\"Secret folder\"]");
    By settingsButton = By.xpath("//XCUIElementTypeOther[@name=\"Settings\"]");
    By addPasswordButton = By.xpath("//XCUIElementTypeOther[@name=\"Add password\"]");
    By autoFillSwitch = By.xpath("(//XCUIElementTypeOther[@name=\"Password AutoFill off\"])[1]/XCUIElementTypeSwitch"); //XCUIElementTypeOther[@name="Password AutoFill off"])[1]/XCUIElementTypeSwitch
    By titleAutoFillOff = By.xpath("//XCUIElementTypeStaticText[@name=\"Password AutoFill off\"]");
    By passcodeField = By.cssSelector("[label = 'Passcode field']");
    By passwordOptionsButton = By.cssSelector("[label = 'Password Options']");
    By authenticatorButton = By.cssSelector("[label='Authenticator']");
    By titleNotGeneratedAPassword = By.xpath("//XCUIElementTypeStaticText[@name=\"You have not generated a password yet\"]");
    //XCUIElementTypeStaticText[@name="You have not generated a password yet"]
    By titleYouCanAddAPassword = By.xpath("//XCUIElementTypeStaticText[@name=\"You can add a password and save it\"]");
    By backButton = By.cssSelector("[label='Back']");
    //XCUIElementTypeStaticText[@name="You can add a password and save it"]
    public Home(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(title));
        Assertions.assertTrue(driver.findElement(addPasswordButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(howToUseItButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(secretFolderButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(settingsButton).isDisplayed());


        Assertions.assertTrue(driver.findElement(titleNotGeneratedAPassword).isDisplayed());
        Assertions.assertTrue(driver.findElement(titleYouCanAddAPassword).isDisplayed());
    }
    public void autoFillOn(){
        driver.findElement(autoFillSwitch).click();
        wait.until(ExpectedConditions.elementToBeClickable(passcodeField)).sendKeys("q"+ Keys.ENTER);
        wait.until(ExpectedConditions.elementToBeClickable(passwordOptionsButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(authenticatorButton)).click();
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
    }

    public void goToHowToUse(){
        driver.findElement(howToUseItButton).click();
    }

    public GeneratePasswordPage addPassword(){
        driver.findElement(addPasswordButton).click();
        return new GeneratePasswordPage(driver, wait);
    }

    public void goToSecretFolder(){
        driver.findElement(secretFolderButton).click();
    }
    public void goToSettings(){
        driver.findElement(settingsButton).click();
    }
    public void goBack() {
        driver.findElement(backButton).click();
    }
}
