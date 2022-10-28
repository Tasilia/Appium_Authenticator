package page;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.xpath("//XCUIElementTypeStaticText[@name=\"Home\"]");
    By howToUseItButton = By.cssSelector("[label='How to use it']");
    //By howToUseItButton = By.xpath("//XCUIElementTypeOther[@name=\"How to use it\"][2]");
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
    //XCUIElementTypeStaticText[@name="You can add a password and save it"]
    public Home(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(title));
        Assert.assertTrue(driver.findElement(titleNotGeneratedAPassword).isDisplayed());
        Assert.assertTrue(driver.findElement(titleYouCanAddAPassword).isDisplayed());
        Assert.assertTrue(driver.findElement(titleAutoFillOff).isDisplayed());
    }
    public void autoFillOn(){
        driver.findElement(autoFillSwitch).click();
        wait.until(ExpectedConditions.elementToBeClickable(passcodeField)).sendKeys("q"+ Keys.ENTER);
        //driver.findElement(passcodeField).sendKeys("q");
        wait.until(ExpectedConditions.elementToBeClickable(passwordOptionsButton)).click();
        //driver.findElement(passwordOptionsButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(authenticatorButton)).click();
        //driver.findElement(authenticatorButton).click();
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
    }

    public void goToHowToUse(){
        driver.findElement(howToUseItButton).click();
    }

    public void addPassword(){
        driver.findElement(addPasswordButton).click();
    }

    public void goToSecretFolder(){
        driver.findElement(secretFolderButton).click();
    }
    public void goToSettings(){
        driver.findElement(settingsButton).click();
    }
}
