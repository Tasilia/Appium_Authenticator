package helper;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.Map;

public class Actions {
    AppiumDriver driver;
    public Actions(AppiumDriver driver){
        this.driver = driver;
    }
    public void swipe(String direction) throws InterruptedException{
        driver.executeScript("mobile:swipe", ImmutableMap.of("direction", direction));
        Thread.sleep(200);
    }
    public void dragFromTo(int fromX, int fromY, int toX, int toY) {
        Map<String, Object> params = new HashMap<>();
        params.put("duration", 0.5);
        params.put("fromX", fromX);
        params.put("fromY", fromY);
        params.put("toX", toX);
        params.put("toY", toY);
        driver.executeScript("mobile: dragFromToForDuration", params);
    }
}
