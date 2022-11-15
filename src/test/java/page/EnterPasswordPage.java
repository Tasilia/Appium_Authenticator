package page;

import helper.GeneratePassword;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EnterPasswordPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.cssSelector("[label='Enter Passcode']");
    By titleError = By.xpath("(//XCUIElementTypeOther[@name=\"Try again later\"])[2]");
    By faceIdButton = By.xpath("(//XCUIElementTypeOther[@name=\"0\"])[1]/XCUIElementTypeOther[3]");

    public EnterPasswordPage(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(titleError)).click();
        Assertions.assertTrue(driver.findElement(title).isDisplayed());
    }

    public void enterPassword(Integer[] password) {
        new GeneratePassword().clickFourTimes(password, wait);
    }

    public void clickFaceId() {
        driver.findElement(faceIdButton).click();
    }
}
