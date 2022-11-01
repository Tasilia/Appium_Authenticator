package page;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateAccountPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.xpath("//XCUIElementTypeStaticText[@name=\"Сreate account\"]");
    By textLink = By.cssSelector("[label = 'LINK']");
    By textAccount = By.cssSelector("[label = 'ACCOUNT']");
    By textPassword = By.cssSelector("[label = 'PASSWORD']");
    By fieldLink = By.cssSelector("[value = 'Enter link']");
    By fieldAccount = By.cssSelector("[value = 'Enter account name']");
    By fieldPassword = By.xpath("(//XCUIElementTypeOther[@name=\"Back Сreate account LINK Enter link " +
            "ACCOUNT Enter account name PASSWORD Сreate account\"])[17]/XCUIElementTypeOther[4]/" +
            "XCUIElementTypeTextField");
    By createAccountButton = By.xpath("(//XCUIElementTypeOther[@name=\"Сreate account\"])[2]");
    By backButton = By.xpath("(//XCUIElementTypeOther[@name=\"Back\"])[3]");

    public CreateAccountPage(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.visibilityOfElementLocated(title));
        Assertions.assertTrue(driver.findElement(textLink).isDisplayed());
        Assertions.assertTrue(driver.findElement(textAccount).isDisplayed());
        Assertions.assertTrue(driver.findElement(textPassword).isDisplayed());
        Assertions.assertTrue(driver.findElement(fieldAccount).isDisplayed());
        Assertions.assertTrue(driver.findElement(fieldLink).isDisplayed());
        Assertions.assertTrue(driver.findElement(fieldPassword).isDisplayed());
        Assertions.assertTrue(driver.findElement(createAccountButton).isDisplayed());
    }

    public void goBack() {
        driver.findElement(backButton).click();
    }
}
