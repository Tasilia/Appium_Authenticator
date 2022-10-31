package tests;

import com.google.common.collect.ImmutableMap;
import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class HomeTests {
    IOSDriver driver;
    WebDriverWait wait;
    By backButton = By.cssSelector("[label='Back']");
    final String letters = GeneratePasswordPage.letters;
    final String symbols = GeneratePasswordPage.symbols;
    final String digits = GeneratePasswordPage.digits;
    final String exclude = GeneratePasswordPage.exclude;
    final String save = GeneratePasswordPage.save;
    final String generate = GeneratePasswordPage.generate;
    private static Integer[] password = new GeneratePassword().getRandomPassword();
    String[] nameSwitch = new String[] {digits, letters, symbols};
    String[] symbolsExamples = new String[] {"!", "@", "#", "$", "%", "^", "`", "&", "*", "(", ")", "-", "+", "\"",
            "№", ",", ":", ".", ";", "'", "|", "/", "<", ">", "±", "~", "\\", "]", "[", "{", "}", "_", "?", "="};
    String[] similarCharacters = new String[] {"o", "O", "0", "1", "|"};
    @BeforeEach
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(false, true));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
        new EnterPasswordPage(driver, wait).enterPassword(password);
    }
    @BeforeAll
    public static void setPassword() throws MalformedURLException{
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        IOSDriver driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(true, false));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        new Onboarding(driver, wait).passTheOnboarding().createAndRepeatPassword(password).closePaywall();
    }

    @AfterEach
    public void tearDown() {
        driver.terminateApp("appstrain.test.authenticator");
        driver.quit();
    }

    @Test
    public void testAutoFillOn() {
        Assertions.assertTrue(driver.getPageSource().contains("AutoFill off"));
        new Home(driver, wait).autoFillOn();
        Assertions.assertTrue(driver.getPageSource().contains("AutoFill on"));
    }

    @Test
    public void testGoToHowtoUseItAndBack() {
        new Home(driver, wait).goToHowToUse();
        Assertions.assertTrue(driver.getPageSource().contains("How to enable"));
        driver.findElement(backButton).click();
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToHowtoUseItAndScroll() throws InterruptedException{
        new Home(driver, wait).goToHowToUse();
        int startY = driver.findElement(By.cssSelector("[label='How to enable']")).getLocation().getY();
        driver.executeScript("mobile:scroll", ImmutableMap.of("direction", "down"));
        Thread.sleep(100);
        int endY = driver.findElement(By.cssSelector("[label='How to enable']")).getLocation().getY();
        Assertions.assertTrue(startY > endY);
    }
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols, exclude} )
    public void testSwitch(String nameSwitch) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        String startValue = generatePasswordPage.getAttribute(nameSwitch, "value");
        generatePasswordPage.click(nameSwitch);
        String endValue = generatePasswordPage.getAttribute(nameSwitch, "value");
        Assertions.assertFalse(startValue.equals(endValue));
    }
    @ParameterizedTest
    @ValueSource( strings = {"1", "0"} )
    public void testNotEnabledGeneratePasswordButton(String value) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        String startEnable = generatePasswordPage.getAttribute(generate, "enabled");
        if (generatePasswordPage.getAttribute(nameSwitch[0], "value").equals("0")) {
            generatePasswordPage.click(nameSwitch[0]);
        }
        for(String name : nameSwitch) {
            if (generatePasswordPage.getAttribute(name, "value").equals("1")) {
                generatePasswordPage.click(name);
            }
        }
        if (!(generatePasswordPage.getAttribute(exclude, "value").equals(value))) {
            generatePasswordPage.click(exclude);
        }
        String endEnable = generatePasswordPage.getAttribute(generate, "enabled");
        Assertions.assertFalse(startEnable.equals(endEnable));
    }
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols} )
    public void testShouldDefinePasswordType(String desiredSwitch) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("You have not generated a password yet"));
        generatePasswordPage.turnOnOneSwitch(desiredSwitch, nameSwitch);
        generatePasswordPage.click(generate);
        if (desiredSwitch.equals(nameSwitch[1])) {
            Assertions.assertTrue(driver.getPageSource().contains("Insecure password"));
        } else {
            Assertions.assertTrue(driver.getPageSource().contains("Dangerous password"));
        }
    }
    public Boolean containsSymbols(String password) {
        Boolean result = false;
        for (String symbol : symbolsExamples) {
            if (password.contains(symbol)) {
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
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols} )
    public void test(String desiredSwitch) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("You have not generated a password yet"));
        generatePasswordPage.turnOnOneSwitch(desiredSwitch, nameSwitch);
        generatePasswordPage.click(generate);
        List parser = driver.findElements(By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText"));
        WebElement passwordWebElement = (WebElement)parser.get(3);
        String parsedPassword = passwordWebElement.getAttribute("name");
        if (desiredSwitch.equals(nameSwitch[0])) {
            Assertions.assertTrue(NumberUtils.isDigits(parsedPassword));
        } else if (desiredSwitch.equals(nameSwitch[1])) {
            Assertions.assertFalse((containsDigits(parsedPassword)) && (containsSymbols(parsedPassword)));
        } else {
            Assertions.assertTrue(isSymbols(parsedPassword));
        }
    }
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
    public void testShouldExcludeSimilarCharacters(String action) {
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
    }

}
