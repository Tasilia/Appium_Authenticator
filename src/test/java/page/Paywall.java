package page;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Paywall {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.cssSelector("[label='Premium access']");
    By closeButton = By.xpath("(//XCUIElementTypeOther[@name=\"Restore\"])[1]/XCUIElementTypeOther[2]");
    By xpathRestoreButton = By.xpath("(//XCUIElementTypeOther[@name=\"Restore\"])[2]");
    By xpathProductButton = By.xpath("(//XCUIElementTypeOther[@name=\"1 year $1.99/year (1 weeks trial)\"])[2]");
    public Paywall(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeOther" +
                "[contains(@name, 'Premium')]")));
        Assertions.assertTrue(driver.findElement(title).isDisplayed());
    }
    public Home closePaywall() {
        driver.findElement(closeButton).click();
        return new Home(driver, wait);
    }
}
