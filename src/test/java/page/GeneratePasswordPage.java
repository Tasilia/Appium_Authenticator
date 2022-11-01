package page;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

public class GeneratePasswordPage {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.cssSelector("[label='Generate password']");
    By savePasswordButton = By.cssSelector("[label='Save password']");
    By textOptions = By.cssSelector("[label='OPTIONS']");
    By generatePasswordButton = By.xpath("(//XCUIElementTypeOther[@name=\"Generate password\"])[2]");
    String textDigits = "Digits";
    String textLetters = "Letters";
    String textSymbols = "Symbols";
    String textExclude = "Exclude similar characters";
    By digitsSwitch = By.xpath("//XCUIElementTypeOther[@name=\"Digits (e.g. 123)\"]/XCUIElementTypeSwitch");
    By lettersSwitch = By.xpath("//XCUIElementTypeOther[@name=\"Letters (e.g. Aa)\"]/XCUIElementTypeSwitch");
    By symbolsSwitch = By.xpath("//XCUIElementTypeOther[@name=\"Symbols (e.g. @#!)\"]/XCUIElementTypeSwitch");
    By excludeSwitch = By.xpath("//XCUIElementTypeOther[@name=\"Exclude similar characters\"]/XCUIElementTypeSwitch");
    By textPasswordLength = By.xpath("//XCUIElementTypeStaticText[contains(@name,\"Length\")]");
    By passwordPath = By.xpath("//XCUIElementTypeOther[contains(@name,\"password\")]//preceding::XCUIElementTypeStaticText");
    By backButton = By.cssSelector("[label='Back']");
    public static final String letters = "letters";
    public static final String symbols = "symbols";
    public static final String digits = "digits";
    public static final String exclude = "exclude";
    HashMap<String, By> mapSwitch = new HashMap<String, By>() {{
        put(digits, digitsSwitch);
        put(letters, lettersSwitch);
        put(exclude, excludeSwitch);
        put(symbols, symbolsSwitch);
    }};

    public GeneratePasswordPage(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        Assertions.assertTrue(driver.findElement(title).isDisplayed());
        Assertions.assertTrue(driver.findElement(savePasswordButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(textOptions).isDisplayed());
        Assertions.assertTrue(driver.findElement(generatePasswordButton).isDisplayed());
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource.contains(textDigits));
        Assertions.assertTrue(pageSource.contains(textLetters));
        Assertions.assertTrue(pageSource.contains(textSymbols));
        Assertions.assertTrue(pageSource.contains(textExclude));
    }
    public String getSwitchValue(String need) {
        return driver.findElement(mapSwitch.get(need)).getAttribute("value");
    }
    public String getEnabledGeneratePasswordButton() {
        return driver.findElement(generatePasswordButton).getAttribute("enabled");
    }
    public void click(String desiredSwitch){
        driver.findElement(mapSwitch.get(desiredSwitch)).click();
    }
    public void turnOnSwitch(String desiredSwitch) {
        if (getSwitchValue(desiredSwitch).equals("0")) {
            click(desiredSwitch);
        }
    }
    public void turnOffSwitch(String desiredSwitch) {
        if (getSwitchValue(desiredSwitch).equals("1")) {
            click(desiredSwitch);
        }
    }
    public void turnOnOneSwitch(String desiredSwitch, String[] nameSwitch) {
        for (String name : nameSwitch) {
            if (name.equals(desiredSwitch)) {
                turnOnSwitch(name);
            } else {
                turnOffSwitch(name);
            }
        }
    }
    public Integer getPasswordLength() {
        String passwordLength = driver.findElement(textPasswordLength).getAttribute("name");
        return Integer.parseInt(passwordLength.substring(8));
    }
    public String getGeneratedPassword() {
        List parser = driver.findElements(passwordPath);
        WebElement passwordWebElement = (WebElement)parser.get(3);
        return passwordWebElement.getAttribute("name");
    }
    public void turnSwitch(String action, String desiredSwitch) {
        if (action.equals("On")) {
            turnOnSwitch(desiredSwitch);
        } else {
            turnOffSwitch(desiredSwitch);
        }
    }
    public void turnOnThreeSwitch(String[] nameSwitch) {
        for (String name : nameSwitch) {
            turnOnSwitch(name);
        }
    }
    public void clickGeneratePassword(){
        driver.findElement(generatePasswordButton).click();
    }
    public CreateAccountPage clickSave(){
        driver.findElement(savePasswordButton).click();
        return new CreateAccountPage(driver, wait);
    }
    public void goBack() {
        driver.findElement(backButton).click();
    }
    public CreateAccountPage savePassword(){
        turnOnSwitch(digits);
        clickGeneratePassword();
        return clickSave();
    }
}
