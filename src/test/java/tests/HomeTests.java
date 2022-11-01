package tests;

import com.google.common.collect.ImmutableMap;
import helper.Actions;
import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
    final String letters = GeneratePasswordPage.letters;
    final String symbols = GeneratePasswordPage.symbols;
    final String digits = GeneratePasswordPage.digits;
    final String exclude = GeneratePasswordPage.exclude;
    private static Integer[] password = new GeneratePassword().getRandomPassword();
    String[] nameSwitch = new String[] {digits, letters, symbols};
    String[] symbolsExamples = new String[] {"!", "@", "#", "$", "%", "^", "`", "&", "*", "(", ")", "-", "+", "\"",
            "№", ",", ":", ".", ";", "'", "|", "/", "<", ">", "±", "~", "\\", "]", "[", "{", "}", "_", "?", "="};
    String[] similarCharacters = new String[] {"o", "O", "0", "1", "|"};
    final int xLength8 = 130;
    final int xLength20 = 345;
    final int xLength4 = 50;
    int y = 360;
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
        Home home = new Home(driver, wait);
        home.goToHowToUse();
        Assertions.assertTrue(driver.getPageSource().contains("How to enable"));
        home.goBack();
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToHowtoUseItAndBackBySwipe() throws InterruptedException{
        Home home = new Home(driver, wait);
        home.goToHowToUse();
        Assertions.assertTrue(driver.getPageSource().contains("How to enable"));
        new Actions(driver).dragFromTo(10, y, 300, y);
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
        String startValue = generatePasswordPage.getSwitchValue(nameSwitch);
        generatePasswordPage.click(nameSwitch);
        String endValue = generatePasswordPage.getSwitchValue(nameSwitch);
        Assertions.assertFalse(startValue.equals(endValue));
    }
    @ParameterizedTest
    @ValueSource( strings = {"On", "Off"} )
    public void testNotEnabledGeneratePasswordButton(String action) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnSwitch(digits);
        String startEnable = generatePasswordPage.getEnabledGeneratePasswordButton();
        for(String name : nameSwitch) {
            generatePasswordPage.turnOffSwitch(name);
        }
        generatePasswordPage.turnSwitch(action, exclude);
        String endEnable = generatePasswordPage.getEnabledGeneratePasswordButton();
        Assertions.assertFalse(startEnable.equals(endEnable));
    }
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols} )
    public void testShouldDefinePasswordType(String desiredSwitch) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("You have not generated a password yet"));
        generatePasswordPage.turnOnOneSwitch(desiredSwitch, nameSwitch);
        generatePasswordPage.clickGeneratePassword();
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
            if (!contains) {
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
    public void testPasswordConsistOf(String desiredSwitch) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("You have not generated a password yet"));
        generatePasswordPage.turnOnOneSwitch(desiredSwitch, nameSwitch);
        generatePasswordPage.clickGeneratePassword();
        String parsedPassword = generatePasswordPage.getGeneratedPassword();
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
    @ValueSource( strings = {"On", "Off"} )
    public void testShouldExcludeSimilarCharacters(String action) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnThreeSwitch(nameSwitch);
        Actions actions = new Actions(driver);
        actions.dragFromTo(xLength8,y,xLength20,y);
        generatePasswordPage.turnSwitch(action, exclude);
        for (int i = 0; i < 5; i++) {
            generatePasswordPage.clickGeneratePassword();
            String parsedPassword = generatePasswordPage.getGeneratedPassword();
            if (action.equals("excludeTurnOn")) {
                Assertions.assertFalse(containsSimilarCharacters(parsedPassword));
            } else {
                int count = 1;
                if (containsSimilarCharacters(parsedPassword)) {
                    break;
                } else {
                    count++;
                }
                Assertions.assertTrue(count < 5);
            }
        }
        actions.dragFromTo(xLength20,y,xLength8,y);
    }
    @ParameterizedTest
    @ValueSource( ints = {xLength20, xLength4} )
    public void testTheLengthValueInTheTextShouldChange(int length) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertEquals(8,generatePasswordPage.getPasswordLength());
        Actions actions = new Actions(driver);
        actions.dragFromTo(xLength8,y,length,y);
        if (length == xLength20) {
            Assertions.assertEquals(20,generatePasswordPage.getPasswordLength());
        } else {
            Assertions.assertEquals(4, generatePasswordPage.getPasswordLength());
        }
        actions.dragFromTo(length,y,xLength8,y);
    }
    @ParameterizedTest
    @ValueSource( ints = {xLength20, xLength4} )
    public void testPasswordLengthShouldChange(int length) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.clickGeneratePassword();
        Assertions.assertEquals(8,generatePasswordPage.getGeneratedPassword().length());
        Actions actions = new Actions(driver);
        actions.dragFromTo(xLength8,y,length,y);
        generatePasswordPage.clickGeneratePassword();
        actions.dragFromTo(length,y,xLength8,y);
        if (length == xLength20) {
            Assertions.assertEquals(20,generatePasswordPage.getGeneratedPassword().length());
        } else {
            Assertions.assertEquals(4, generatePasswordPage.getGeneratedPassword().length());
        }
    }
    @Test
    public void testShouldGenerateStrongPassword() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Actions actions = new Actions(driver);
        actions.dragFromTo(xLength8,y,xLength20,y);
        generatePasswordPage.turnOnThreeSwitch(nameSwitch);
        generatePasswordPage.clickGeneratePassword();
        actions.dragFromTo(xLength20,y,xLength8,y);
        Assertions.assertTrue(driver.getPageSource().contains("Strong password"));
    }
    @Test
    public void testGoToGeneratePasswordPageAndBack() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
        generatePasswordPage.goBack();
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToGeneratePasswordPageAndBackBySwipe() {
        new Home(driver, wait).addPassword();
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
        new Actions(driver).dragFromTo(10, y, 300, y);
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToCreateAccountPageAndBack() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        CreateAccountPage createAccountPage = generatePasswordPage.savePassword();
        Assertions.assertTrue(driver.getPageSource().contains("Сreate account"));
        createAccountPage.goBack();
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
    }
    @Test
    public void testGoToCreateAccountPageAndBackBySwipe() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.savePassword();
        Assertions.assertTrue(driver.getPageSource().contains("Сreate account"));
        new Actions(driver).dragFromTo(10, y, 300, y);
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
    }
    @Test
    public void testSaveButtonShouldBeNotEnabled() {
        Assertions.assertThrows(TimeoutException.class, () -> new Home(driver, wait).addPassword().clickSave());
    }

}
