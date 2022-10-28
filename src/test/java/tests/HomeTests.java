package tests;

import helper.GeneratePassword;
import helper.MyiOSDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class HomeTests {
    IOSDriver driver;
    WebDriverWait wait;
    By backButton = By.cssSelector("[label='Back']");
    private static Integer[] password = new GeneratePassword().getRandomPassword();
    @Before
    public void setUp() throws MalformedURLException {
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(false, true));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        new EnterPasswordPage(driver, wait).enterPassword(password);
    }
    @BeforeClass
    public static void setPassword() throws MalformedURLException{
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        MyiOSDriver myiOSDriver = new MyiOSDriver();
        IOSDriver driver = new IOSDriver(remoteUrl, myiOSDriver.getIOSDriverCapabilities(true, false));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        new Onboarding(driver, wait).passTheOnboarding().createAndRepeatPassword(password).closePaywall();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testAutoFillOn() {
        new Home(driver, wait).autoFillOn();
        Assert.assertTrue(driver.getPageSource().contains("AutoFill on"));
    }

    @Test
    public void testHowtoUseIt() {
        new Home(driver, wait).goToHowToUse();
        Assert.assertTrue(driver.getPageSource().contains("How to enable"));
        driver.findElement(backButton).click();
        Assert.assertTrue(driver.getPageSource().contains("Home"));
    }
}
