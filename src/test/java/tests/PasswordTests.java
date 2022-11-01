package tests;

import com.google.common.collect.ImmutableMap;
import helper.Actions;
import helper.GeneratePassword;
import helper.MyiOSDriver;
//import io.appium.java_client.TouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class PasswordTests {
    IOSDriver driver;
    WebDriverWait wait;
    @BeforeEach
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(false, true));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
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
    final int xLength8 = 130;
    final int xLength20 = 345;
    final int xLength4 = 50;
    int y = 360;

    final String letters = "letters";
    final String symbols = "symbols";
    final String digits = "digits";
    final String exclude = "exclude";
    String[] nameSwitch = new String[] {digits, letters, symbols};
    String[] symbolsExamples = new String[] {"!", "@", "#", "$", "%", "^", "`", "&", "*", "(", ")", "-", "+", "\"",
            "№", ",", ":", ".", ";", "'", "|", "/", "<", ">", "±", "~", "\\", "]", "[", "{", "}", "_", "?", "="};

    @ParameterizedTest
    @ValueSource( strings = {"excludeTurnOn", "excludeTurnOff"} )
    public void test(String action) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnSwitch(digits);
        //XCUIElementTypeOther[@name="Length: 8"]/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther
        int expected = generatePasswordPage.getPasswordLength();
        generatePasswordPage.clickGeneratePassword();
        int actual = generatePasswordPage.getGeneratedPassword().length();
        Assertions.assertEquals(expected, actual);
        //XCUIElementTypeOther[@name="Length: 8"]/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther



        //String c = driver.findElement(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText[-1]")).getAttribute("name");

        //XCUIElementTypeStaticText[contains(@name,"Dangerous password" or "Insecure password" or "Strong password")]
        //XCUIElementTypeStaticText[@name=[contains(text(),"Dangerous password")]]
    }
}
