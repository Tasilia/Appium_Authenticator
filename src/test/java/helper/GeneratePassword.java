package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GeneratePassword {
    public Integer[] getRandomPassword() {
        Integer[] password = new Integer[4];
        for (int i = 0; i < 4; i++) {
            password[i] = (int) (Math.random() * 9);
        }
        return password;
    }

    public void clickFourTimes(Integer[] password, WebDriverWait wait) {
        for (int i = 0; i < 4; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[label=" + password[i] + "]"))).click();
        }
    }
}
