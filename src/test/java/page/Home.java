package page;

import com.google.common.collect.ImmutableMap;
import helper.Actions;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    AppiumDriver driver;
    WebDriverWait wait;
    By title = By.xpath("//XCUIElementTypeStaticText[@name=\"Home\"]");
    By howToUseItButton = By.cssSelector("[label='How to use it']");
    By homeButton = By.xpath("(//XCUIElementTypeOther[@name=\"Home\"])[2]");
    By secretFolderButton = By.xpath("//XCUIElementTypeOther[@name=\"Secret folder\"]");
    By settingsButton = By.xpath("//XCUIElementTypeOther[@name=\"Settings\"]");
    By addPasswordButton = By.xpath("//XCUIElementTypeOther[@name=\"Add password\"]");
    By addPasswordButtonWithPasswords = By.xpath("//XCUIElementTypeOther[@name=\"Add  password\"]");
    By autoFillSwitch = By.xpath("(//XCUIElementTypeOther[@name=\"Password AutoFill off\"])[1]/" +
            "XCUIElementTypeSwitch");
    By passcodeField = By.cssSelector("[label = 'Passcode field']");
    By passwordOptionsButton = By.cssSelector("[label = 'Password Options']");
    By authenticatorButton = By.cssSelector("[label='Authenticator']");
    By backButton = By.cssSelector("[label='Back']");
    By clearAllButton = By.cssSelector("[label = 'Clear all']");
    public Home(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        wait.until(ExpectedConditions.presenceOfElementLocated(title));
        try {
            Assertions.assertTrue(driver.findElement(addPasswordButton).isDisplayed());
        }
        catch (NoSuchElementException e) {
            Assertions.assertTrue(driver.findElement(addPasswordButtonWithPasswords).isDisplayed());
        }
        Assertions.assertTrue(driver.findElement(howToUseItButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(secretFolderButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(settingsButton).isDisplayed());
        Assertions.assertTrue(driver.findElement(homeButton).isDisplayed());
    }
    public void autoFillOn(){
        driver.findElement(autoFillSwitch).click();
        wait.until(ExpectedConditions.elementToBeClickable(passcodeField)).sendKeys("q"+ Keys.ENTER);
        wait.until(ExpectedConditions.elementToBeClickable(passwordOptionsButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(authenticatorButton)).click();
        driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId",
                "appstrain.test.authenticator"));
    }

    public void goToHowToUse(){
        driver.findElement(howToUseItButton).click();
    }

    public GeneratePasswordPage addPassword(){
        clickAddPassword();
        return new GeneratePasswordPage(driver, wait);
    }
    public void clickAddPassword() {
        try {
            driver.findElement(addPasswordButton).click();
        }
        catch (NoSuchElementException e) {
            driver.findElement(addPasswordButtonWithPasswords).click();
        }
    }

    public Paywall goToSecretFolderWithoutSubscribe(){
        driver.findElement(secretFolderButton).click();
        return new Paywall(driver, wait);
    }
    public Settings goToSettings(){
        driver.findElement(settingsButton).click();
        return new Settings(driver, wait);
    }
    public void goBack() {
        driver.findElement(backButton).click();
    }
    public void clickCopy(String link, String account, String password) {
        driver.findElement(By.xpath("(//XCUIElementTypeOther[@name=\"" + link + " " + link +
                " Vertical scroll bar, 1 page " + account + " " + password + "\"])[1]/XCUIElementTypeOther[1]/" +
                "XCUIElementTypeOther")).click();
    }
    public void clickDeletePassword(String link, String account, String password) {
        new Actions(driver).dragFromTo(300, 445, 115, 445);
        driver.findElement(By.xpath("(//XCUIElementTypeOther[@name=\"" + link + " " + link +
                " Vertical scroll bar, 1 page " + account + " " + password +
                "\"])[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther")).click();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource. contains("Are you sure?") && pageSource.contains("You can't " +
                "restore deleted password") && pageSource.contains("Cancel") && pageSource.contains("Delete"));
    }
    public void clickClearAll() {
        driver.findElement(clearAllButton).click();
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(pageSource. contains("Are you sure?") && pageSource.contains("You can't " +
                "restore deleted password") && pageSource.contains("Cancel") && pageSource.contains("Delete"));
    }
    public void clickCancel() {
        driver.findElement(By.cssSelector("[label = 'Cancel']")).click();
    }
    public void deletePassword(String link, String account, String password) {
        clickDeletePassword(link, account, password);
        driver.findElement(By.cssSelector("[label = 'Delete']")).click();
    }
    public void clearAll() {
        clickClearAll();
        driver.findElement(By.cssSelector("[label = 'Delete']")).click();
    }
}
