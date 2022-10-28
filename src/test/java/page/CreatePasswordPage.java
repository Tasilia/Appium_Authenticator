package page;

import helper.GeneratePassword;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreatePasswordPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By cssNumber4Button = By.cssSelector("[label='4']");
    By cssTitleOne = By.cssSelector("[label='Create a password']");
    By cssTitleTwo = By.cssSelector("[label='Before adding a new password, you need to register" +
            " with our product to keep your data secure.']");
    By cssTitleThree = By.cssSelector("[label='Enter Passcode']");
    By cssTitleRepeatPassword = By.cssSelector("[label=' Repeat password']");
    By cssTitleErrorPassword = By.cssSelector("[label='Passwords are not identical']");
    By xpathDeleteButton = By.xpath("//XCUIElementTypeOther[@name=\"0\"][1]/" +
            "XCUIElementTypeOther[3]");

    public CreatePasswordPage(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(cssTitleOne));
        Assert.assertTrue(driver.findElement(cssTitleOne).isDisplayed());
        Assert.assertTrue(driver.findElement(cssTitleTwo).isDisplayed());
        Assert.assertTrue(driver.findElement(cssTitleThree).isDisplayed());
    }
    public void deleteNumber() {
        wait.until(ExpectedConditions.elementToBeClickable(cssNumber4Button)).click();
        wait.until(ExpectedConditions.elementToBeClickable(xpathDeleteButton)).click();
    }
    public void createPassword(Integer[] password) {
        new GeneratePassword().clickFourTimes(password, wait);
        wait.until(ExpectedConditions.elementToBeClickable(cssNumber4Button));
        Assert.assertTrue(driver.findElement(cssTitleRepeatPassword).isDisplayed());
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
