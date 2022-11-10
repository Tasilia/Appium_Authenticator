package page;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateAccountPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.xpath("//XCUIElementTypeStaticText[@name=\"Сreate account\"]");
    By textLink = By.cssSelector("[label = 'LINK']");
    By textAccount = By.cssSelector("[label = 'ACCOUNT']");
    By textPassword = By.cssSelector("[label = 'PASSWORD']");
    By fieldLink = By.xpath("//XCUIElementTypeTextField");
    //(//XCUIElementTypeOther[@name="Back Сreate account LINK ACCOUNT PASSWORD Enter password Сreate account"])[17]/XCUIElementTypeOther[2]/XCUIElementTypeTextField
    By fieldAccount = By.xpath("//XCUIElementTypeTextField//following::XCUIElementTypeTextField[1]");
    //(//XCUIElementTypeOther[@name="Back Сreate account LINK ACCOUNT PASSWORD Enter password Сreate account"])[17]/XCUIElementTypeOther[3]/XCUIElementTypeTextField
    By fieldPassword = By.xpath("//XCUIElementTypeTextField//following::XCUIElementTypeTextField[2]");
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
    public void enterFieldLink(String link) {
        driver.findElement(fieldLink).sendKeys(link);
        driver.findElement(fieldLink).sendKeys(Keys.ENTER);
    }
    public void enterFieldAccount(String account) {
        driver.findElement(fieldAccount).sendKeys(account);
        driver.findElement(fieldAccount).sendKeys(Keys.ENTER);
    }
    public void enterFieldPassword(String password) {
        deletePassword();
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldPassword).sendKeys(Keys.ENTER);
    }
    public void deletePassword() {
        driver.findElement(fieldPassword).clear();
        driver.findElement(fieldPassword).sendKeys(Keys.ENTER);
    }
    public Home createAccount(String link, String account) {
        enterFieldLink(link);
        enterFieldAccount(account);
        driver.findElement(createAccountButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label = 'Dismiss (ESC)']"))).click();
        return new Home(driver, wait);
    }
    public String getCreateAccountButtonEnabled() {
        return driver.findElement(createAccountButton).getAttribute("enabled");
    }
    public String getPassword() {
        return driver.findElement(fieldPassword).getAttribute("value");
    }
}
