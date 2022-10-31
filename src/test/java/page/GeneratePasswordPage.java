package page;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

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
    public static final String letters = "letters";
    public static final String symbols = "symbols";
    public static final String digits = "digits";
    public static final String exclude = "exclude";
    public static final String save = "save";
    public static final String generate = "generate";
    HashMap<String, By> mapSwitch = new HashMap<String, By>() {{
        put(digits, digitsSwitch);
        put(letters, lettersSwitch);
        put(exclude, excludeSwitch);
        put(symbols, symbolsSwitch);
        put(save, savePasswordButton);
        put(generate, generatePasswordButton);
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
    public String getAttribute(String need, String attribute) {
        return driver.findElement(mapSwitch.get(need)).getAttribute(attribute);
    }
    public void click(String need) {
        driver.findElement(mapSwitch.get(need)).click();
    }
    public void turnOnSwitch(String desiredSwitch) {
        if (getAttribute(desiredSwitch, "value").equals("0")) {
            click(desiredSwitch);
        }
    }
    public void turnOffSwitch(String desiredSwitch) {
        if (getAttribute(desiredSwitch, "value").equals("1")) {
            click(desiredSwitch);
        }
    }
//    public void turnSwitch(String desiredSwitch, String action) {
//        if (action.equals("on")) {
//            if (getAttribute(desiredSwitch, "value").equals("0")) {
//                click(desiredSwitch);
//            }
//        } else {
//            if (getAttribute(desiredSwitch, "value").equals("1")) {
//                click(desiredSwitch);
//            }
//        }
//    }
    public void turnOnOneSwitch(String desiredSwitch, String[] nameSwitch) {
        for (String name : nameSwitch) {
            if (name.equals(desiredSwitch)) {
                turnOnSwitch(name);
            } else {
                turnOffSwitch(name);
            }
        }
    }
}
