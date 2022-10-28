package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;

public class Onboarding {
    AppiumDriver driver;
    WebDriverWait wait;
    By cssContinueButton = By.cssSelector("[label='Continue']");

    public Onboarding(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
    }
    public CreatePasswordPage passTheOnboarding(){
        wait.until(ExpectedConditions.elementToBeClickable(cssContinueButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(cssContinueButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(cssContinueButton)).click();
        return new CreatePasswordPage(driver, wait);
    }
}
