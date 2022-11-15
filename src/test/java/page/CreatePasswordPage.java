package page;

import helper.GeneratePassword;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreatePasswordPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By number4Button = By.cssSelector("[label='4']");
    By titleOne = By.cssSelector("[label='Create a password']");
    By titleTwo = By.cssSelector("[label='Before adding a new password, you need to register" +
            " with our product to keep your data secure.']");
    By titleThree = By.cssSelector("[label='Enter Passcode']");
    By titleRepeatPassword = By.cssSelector("[label=' Repeat password']");
    By deleteButton = By.xpath("//XCUIElementTypeOther[@name=\"0\"][1]/" +
            "XCUIElementTypeOther[3]");

    public CreatePasswordPage(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(titleOne));
        Assertions.assertTrue(driver.findElement(titleOne).isDisplayed());
        Assertions.assertTrue(driver.findElement(titleTwo).isDisplayed());
        Assertions.assertTrue(driver.findElement(titleThree).isDisplayed());
    }

    public void deleteNumber() {
        wait.until(ExpectedConditions.elementToBeClickable(number4Button)).click();
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }

    public void createPassword(Integer[] password) {
        new GeneratePassword().clickFourTimes(password, wait);
        wait.until(ExpectedConditions.elementToBeClickable(number4Button));
        Assertions.assertTrue(driver.findElement(titleRepeatPassword).isDisplayed());
    }

    public Paywall createAndRepeatPassword(Integer[] password) {
        createPassword(password);
        new GeneratePassword().clickFourTimes(password, wait);
        return new Paywall(driver, wait);
    }

    public void createAndRepeatWrongPassword(Integer[] password, Integer[] wrongPassword) {
        deleteNumber();
        createPassword(password);
        new GeneratePassword().clickFourTimes(wrongPassword, wait);
    }
}
