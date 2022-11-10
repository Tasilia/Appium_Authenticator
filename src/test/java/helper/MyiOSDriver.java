package helper;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyiOSDriver {
    public Capabilities getIOSDriverCapabilities(Boolean fullReset, Boolean noReset) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "iOS");
        desiredCapabilities.setCapability("appium:platformVersion", "16.0");
        desiredCapabilities.setCapability("appium:deviceName", "iPhone 14");
        desiredCapabilities.setCapability("appium:UDID", "3955C0BD-4642-4C69-94AA-0D0C3EBA9CE2");
        desiredCapabilities.setCapability("appium:fullReset", fullReset);
        desiredCapabilities.setCapability("appium:useNewWDA", false);
        desiredCapabilities.setCapability("appium:noReset", noReset);
        desiredCapabilities.setCapability("appium:usePrebuiltWDA", true);
        desiredCapabilities.setCapability("appium:autoAcceptAlerts", true);
        desiredCapabilities.setCapability("appium:automationName", "XCUITest");
        desiredCapabilities.setCapability("appium:app", "/Users/Tasilia/Library/Developer/Xcode/" +
                "DerivedData/Authenticator-aiudlbtxdvyrgiftagiewwociedu/Build/Products/Debug-iphonesimulator/" +
                "Authenticator.app");
        desiredCapabilities.setCapability("appium:includeSafariInWebviews", true);
        desiredCapabilities.setCapability("appium:newCommandTimeout", 3600);
        desiredCapabilities.setCapability("appium:connectHardwareKeyboard", false);
        return desiredCapabilities;
    }
}
