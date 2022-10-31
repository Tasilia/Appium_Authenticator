package page;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Onboarding {
    AppiumDriver driver;
    WebDriverWait wait;
    By continueButton = By.cssSelector("[label='Continue']");
    public Onboarding(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
    }
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }
    public CreatePasswordPage passTheOnboarding(){
        clickContinue();
        clickContinue();
        clickContinue();
        return new CreatePasswordPage(driver, wait);
    }
    public void swipe(String direction) throws InterruptedException{
        driver.executeScript("mobile:swipe", ImmutableMap.of("direction", direction));
        Thread.sleep(200);
    }
    public void doubleSwipe(String direction) throws InterruptedException{
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        swipe(direction);
        swipe(direction);
    }
}
