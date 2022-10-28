package tests;

import com.google.common.collect.ImmutableMap;
import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    @Before
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(true, false));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    Integer[] password = new GeneratePassword().getRandomPassword();
    Integer[] wrongPassword = new GeneratePassword().getRandomPassword();

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
        Assert.assertTrue(driver.getPageSource().contains("Home"));
    }

    @Test
    public void testCreateAndRepeatWrongPassword() {
        new Onboarding(driver, wait).passTheOnboarding().createAndRepeatWrongPassword(password, wrongPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label=4]")));
        Assert.assertTrue(driver.getPageSource().contains("Passwords are not identical"));
    }
    @Test
    public void testEnterWrongPassword() {
        setPassword().enterPassword(wrongPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label=4]")));
        Assert.assertTrue(driver.getPageSource().contains("Wrong password"));
    }
}
