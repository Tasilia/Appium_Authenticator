package tests;

import com.google.common.collect.ImmutableMap;
import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class PasswordTests {
    IOSDriver driver;
    WebDriverWait wait;
    Integer[] password = new GeneratePassword().getRandomPassword();
    Integer[] wrongPassword = new GeneratePassword().getRandomPassword();
    @BeforeEach
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(true, false));
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    public EnterPasswordPage setPassword(){
        new Onboarding(driver, wait).passTheOnboarding().createAndRepeatPassword(password).closePaywall();
        driver.terminateApp("appstrain.test.authenticator");
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
        return new EnterPasswordPage(driver, wait);
    }
    @Test
    public void testEnterCorrectPassword() {
        setPassword().enterPassword(password);
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testCreateAndRepeatWrongPassword() {
        new Onboarding(driver, wait).passTheOnboarding().createAndRepeatWrongPassword(password, wrongPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label=4]")));
        Assertions.assertTrue(driver.getPageSource().contains("Passwords are not identical"));
    }
    @Test
    public void testEnterWrongPassword() {
        setPassword().enterPassword(wrongPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label=4]")));
        Assertions.assertTrue(driver.getPageSource().contains("Wrong password"));
    }
    @Test
    public void testSwipeOnboarding() throws InterruptedException{
        Onboarding onboarding = new Onboarding(driver, wait);
        onboarding.doubleSwipe("left");
        onboarding.doubleSwipe("right");
        onboarding.doubleSwipe("left");
        onboarding.clickContinue();
        Assertions.assertTrue(driver.getPageSource().contains("Create a password"));
    }
    @Test
    public void testFaceId() {
        setPassword().clickFaceId();
        Assertions.assertTrue(driver.getPageSource().contains("Error"));
    }
}
