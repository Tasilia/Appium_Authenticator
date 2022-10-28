package page;

import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnboardingPageThree {
    AppiumDriver driver;
    WebDriverWait wait;
    By cssContinueButton = By.cssSelector("[label='Continue']");
    By xpathTitleOne = By.xpath("//XCUIElementTypeOther[contains(@name, 'Keep your')]");
    By xpathTitleTwo = By.xpath("//XCUIElementTypeOther[contains(@name, 'Secret folder')]");

    public OnboardingPageThree(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.visibilityOfElementLocated(cssContinueButton));
        //Assert.assertTrue(driver.findElement(xpathTitleOne).isDisplayed());
        //Assert.assertTrue(driver.findElement(xpathTitleTwo).isDisplayed());
    }
    public CreatePasswordPage clickContinue(){
        wait.until(ExpectedConditions.elementToBeClickable(cssContinueButton)).click();
        return new CreatePasswordPage(driver, wait);
    }
}
