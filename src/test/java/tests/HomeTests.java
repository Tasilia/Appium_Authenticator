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
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

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
    String[] numbers = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    final int xLength8 = 130;
    final int xLength20 = 345;
    final int xLength4 = 50;
    int y = 360;
    String link = "link";
    String account = "account";
    Actions actions;
    @BeforeEach
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(false, true));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
        actions = new Actions(driver);
        new EnterPasswordPage(driver, wait).enterPassword(password);
    }
    @BeforeAll
    public static void setPassword() throws MalformedURLException{
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        IOSDriver driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(true, false));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
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
    public void testGoToHowtoUseItAndBackBySwipe() {
        new Home(driver, wait).goToHowToUse();
        Assertions.assertTrue(driver.getPageSource().contains("How to enable"));
        actions.dragFromTo(10, y, 300, y);
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToHowtoUseItAndScroll() throws InterruptedException{
        new Home(driver, wait).goToHowToUse();
        int startY = driver.findElement(By.cssSelector("[label='How to enable']")).getLocation().getY();
        actions.scroll("down");
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
        for(String name : nameSwitch) {
            generatePasswordPage.turnOffSwitch(name);
        }
        generatePasswordPage.turnSwitch(action, exclude);
        String enable = generatePasswordPage.getEnabledGeneratePasswordButton();
        Assertions.assertTrue(enable.equals("false"));
    }
    public GeneratePasswordPage generatePassword(String desiredSwitch){
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnOneSwitch(desiredSwitch, nameSwitch);
        generatePasswordPage.clickGeneratePassword();
        return generatePasswordPage;
    }
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols} )
    public void testShouldDefinePasswordType(String desiredSwitch) {
        generatePassword(desiredSwitch);
        if (desiredSwitch.equals(nameSwitch[1])) {
            Assertions.assertTrue(driver.getPageSource().contains("Insecure password"));
        } else {
            Assertions.assertTrue(driver.getPageSource().contains("Dangerous password"));
        }
    }
    public Boolean containsCharacter(String password, String[] symbols) {
        Boolean result = false;
        for (String symbol : symbols) {
            if (password.contains(symbol)) {
                result = true;
                break;
            }
        }
        return result;
    }
    public Boolean isSymbols(String password) {
        Boolean containsSymbols = true;
        for (int i = 0; i < password.length(); i++) {
            String a = password.substring(i, i+1);
            containsSymbols = containsCharacter(a, symbolsExamples);
            if (!containsSymbols) {
                break;
            }
        }
        return containsSymbols;
    }
    @ParameterizedTest
    @ValueSource( strings = {digits, letters, symbols} )
    public void testPasswordConsistOf(String desiredSwitch) {
        GeneratePasswordPage generatePasswordPage = generatePassword(desiredSwitch);
        String parsedPassword = generatePasswordPage.getGeneratedPassword();
        if (desiredSwitch.equals(nameSwitch[0])) {
            Assertions.assertTrue(NumberUtils.isDigits(parsedPassword));
        } else if (desiredSwitch.equals(nameSwitch[1])) {
            Assertions.assertFalse((containsCharacter(parsedPassword, numbers)) &&
                    (containsCharacter(parsedPassword, symbolsExamples)));
        } else {
            Assertions.assertTrue(isSymbols(parsedPassword));
        }
    }

    @ParameterizedTest
    @ValueSource( strings = {"On", "Off"} )
    public void testShouldExcludeSimilarCharacters(String action) {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnThreeSwitch(nameSwitch);
        actions.dragFromTo(xLength8,y,xLength20,y);
        generatePasswordPage.turnSwitch(action, exclude);
        for (int i = 0; i < 5; i++) {
            generatePasswordPage.clickGeneratePassword();
            String parsedPassword = generatePasswordPage.getGeneratedPassword();
            if (action.equals("excludeTurnOn")) {
                Assertions.assertFalse(containsCharacter(parsedPassword, similarCharacters));
            } else {
                int count = 1;
                if (containsCharacter(parsedPassword, similarCharacters)) {
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
        actions.dragFromTo(10, y, 300, y);
        Assertions.assertTrue(driver.getPageSource().contains("Home"));
    }
    @Test
    public void testGoToCreateAccountPageAndBack() {
        CreateAccountPage createAccountPage = createAndSavePassword();
        Assertions.assertTrue(driver.getPageSource().contains("Сreate account"));
        createAccountPage.goBack();
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
    }
    @Test
    public void testGoToCreateAccountPageAndBackBySwipe() {
        createAndSavePassword();
        Assertions.assertTrue(driver.getPageSource().contains("Сreate account"));
        actions.dragFromTo(10, y, 300, y);
        Assertions.assertTrue(driver.getPageSource().contains("Generate password"));
    }
    @Test
    public void testSaveButtonShouldBeNotEnabled() {
        Assertions.assertThrows(TimeoutException.class, () -> new Home(driver, wait).addPassword().clickSave());
    }
    @Test
    public void testCreateAccountButtonShouldBeNotEnableWithoutLink() {
        CreateAccountPage createAccountPage = createAndSavePassword();
        createAccountPage.enterFieldAccount(account);
        Assertions.assertTrue(createAccountPage.getCreateAccountButtonEnabled().equals("false"));
    }
    @Test
    public void testCreateAccountButtonShouldBeNotEnableWithoutAccount() {
        CreateAccountPage createAccountPage = createAndSavePassword();
        createAccountPage.enterFieldLink(link);
        Assertions.assertTrue(createAccountPage.getCreateAccountButtonEnabled().equals("false"));
    }
    @Test
    public void testCreateAccountButtonShouldBeNotEnableWithoutPassword() {
        CreateAccountPage createAccountPage = createAndSavePassword();
        createAccountPage.deletePassword();
        createAccountPage.enterFieldLink(link);
        createAccountPage.enterFieldAccount(account);
        Assertions.assertTrue(createAccountPage.getCreateAccountButtonEnabled().equals("false"));
    }
    @Test
    public void testPasswordsShouldBeTheSame() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnSwitch(digits);
        generatePasswordPage.clickGeneratePassword();
        String expected = generatePasswordPage.getGeneratedPassword();
        CreateAccountPage createAccountPage = generatePasswordPage.clickSave();
        String actual = createAccountPage.getPassword();
        Assertions.assertTrue(expected.equals(actual));
    }
    public String createAccountAngGetPassword() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        generatePasswordPage.turnOnSwitch(digits);
        generatePasswordPage.clickGeneratePassword();
        String expectedPassword = generatePasswordPage.getGeneratedPassword();
        CreateAccountPage createAccountPage = generatePasswordPage.clickSave();
        createAccountPage.createAccount(link, account);
        return expectedPassword;
    }
    @Test
    public void testShouldCreateAndDeleteAccount() {
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("You have not generated a password yet") &&
                pageSource.contains("You can add a password and save it"));
        String expectedPassword = createAccountAngGetPassword();
        pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("Search") && pageSource.contains(link) &&
                pageSource.contains(account) && pageSource.contains(expectedPassword));
        homeWithAccount.deletePassword(link, account, expectedPassword);
        pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("You have not generated a password yet") &&
                pageSource.contains("You can add a password and save it"));
    }
    @Test
    public void testShouldCopyPassword() {
        String expectedPassword = createAccountAngGetPassword();
        homeWithAccount.clickCopy(link, account, expectedPassword);
        Assertions.assertTrue(driver.getPageSource().contains("Password copied"));
        homeWithAccount.deletePassword(link, account, expectedPassword);
    }
    @Test
    public void testShouldNotDeletePassword() {
        String expectedPassword = createAccountAngGetPassword();
        homeWithAccount.clickDeletePassword(link, account, expectedPassword);
        homeWithAccount.clickCancel();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("Search") && pageSource.contains(link) &&
                pageSource.contains(account) && pageSource.contains(expectedPassword));
        homeWithAccount.deletePassword(link, account, expectedPassword);
    }
    public CreateAccountPage createAndSavePassword() {
        GeneratePasswordPage generatePasswordPage = new Home(driver, wait).addPassword();
        return generatePasswordPage.createAndSavePassword();
    }
    public void createAccount(String link, String account) {
        createAndSavePassword().createAccount(link, account);
    }
    @Test
    public void testShouldCreateNoMoreThanThreeAccountsWithoutASubscription() {
        createAccount(link, account);
        createAccount(link, account);
        createAccount(link, account);
        Home home = new Home(driver, wait);
        home.clickAddPassword();
        Assertions.assertTrue(driver.getPageSource().contains("The action 'NAVIGATE' with payload"));
        driver.findElement(By.cssSelector("[label = 'Dismiss (ESC)']")).click();
        home.clearAll();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("You have not generated a password yet") &&
                pageSource.contains("You can add a password and save it"));
    }
    @Test
    public void testShouldNotDeleteAllPasswords() {
        createAccount(link, account);
        Home home = new Home(driver, wait);
        home.clickClearAll();
        home.clickCancel();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains("Search") && pageSource.contains(link) &&
                pageSource.contains(account));
        home.clearAll();
    }
}