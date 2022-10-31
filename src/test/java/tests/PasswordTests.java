package tests;

import com.google.common.collect.ImmutableMap;
import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testNotEnabledGeneratePasswordButton() {
        //GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        GeneratePasswordPage generatePasswordPage = new GeneratePasswordPage(driver, wait);
        //System.out.println(driver.findElement(By.xpath("//XCUIElementTypeOther[contains(@name,\"Dangerous password\")]//preceding::XCUIElementTypeStaticText")).getAttribute("name"));
//        List c = driver.findElements(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText"));
//        for(Object abc : c) {
//            WebElement i = (WebElement) abc;
//            System.out.println(i.getAttribute("name"));
//        }
        List a = driver.findElements(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText"));
        WebElement b = (WebElement)a.get(3);
        String c = b.getAttribute("name");
        //System.out.println("\n"+isSymbols(c));
        Assertions.assertTrue(containsSymbols(c));
        //XCUIElementTypeStaticText[contains(@name,"Dangerous password" or "Insecure password" or "Strong password")]
        //XCUIElementTypeStaticText[@name=[contains(text(),"Dangerous password")]]
    }
    final String letters = "letters";
    final String symbols = "symbols";
    final String digits = "digits";
    final String exclude = "exclude";
    final String save = "save";
    final String generate = "generate";
    String[] nameSwitch = new String[] {digits, letters, symbols};
    String[] symbolsExamples = new String[] {"!", "@", "#", "$", "%", "^", "`", "&", "*", "(", ")", "-", "+", "\"",
            "№", ",", ":", ".", ";", "'", "|", "/", "<", ">", "±", "~", "\\", "]", "[", "{", "}", "_", "?", "="};
    public Boolean containsSymbols(String password) {
        Boolean result = false;
        for (String a : symbolsExamples) {
            if (password.contains(a)) {
                result = true;
                break;
            }
        }
        return result;
    }
    public Boolean isSymbols(String password) {
        Boolean contains = true;
        int length = password.length();
        for (int i = 0; i < length; i++) {
            String a = password.substring(i, i+1);
            contains = containsSymbols(a);
            if (contains == false) {
                break;
            }
        }
        return contains;
    }
    public Boolean containsDigits(String password) {
        Boolean result = false;
        for (int i = 0; i < 10; i++) {
            if (password.contains(Integer.toString(i))) {
                result = true;
                break;
            }
        }
        return result;
    }
    String[] similarCharacters = new String[] {"o", "O", "0", "1", "|"};
    public Boolean containsSimilarCharacters(String password) {
        Boolean result = false;
        for (String character : similarCharacters) {
            if (password.contains(character)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @ParameterizedTest
    @ValueSource( strings = {"excludeTurnOn", "excludeTurnOff"} )
    public void test(String action) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("You have not generated a password yet"));
        for (String name : nameSwitch) {
            generatePasswordPage.turnOnSwitch(name);
        }
        if (action.equals("excludeTurnOn")) {
            generatePasswordPage.turnOnSwitch(exclude);
        } else {
            generatePasswordPage.turnOffSwitch(exclude);
        }
        for (int i = 0; i < 5; i++) {
            generatePasswordPage.click(generate);
            List parser = driver.findElements(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText"));
            WebElement passwordWebElement = (WebElement)parser.get(3);
            String parsedPassword = passwordWebElement.getAttribute("name");
            if (action.equals("excludeTurnOn")) {
                Assertions.assertFalse(containsSimilarCharacters(parsedPassword));
            } else {
                int count = 1;
                if (containsSimilarCharacters(parsedPassword)) {
                    break;
                } else {
                    count++;
                    System.out.println(count);
                }
                Assertions.assertTrue(count < 5);
            }
        }

        //String c = driver.findElement(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText[-1]")).getAttribute("name");

        //XCUIElementTypeStaticText[contains(@name,"Dangerous password" or "Insecure password" or "Strong password")]
        //XCUIElementTypeStaticText[@name=[contains(text(),"Dangerous password")]]
    }
}
