package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver(){
        if(driver ==null) {
            System.setProperty("webdriver.chrome.driver", "C:\\bin\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(PropertyReader.getProperty("url"));
        }
        return driver;
    }

    public static void quitDriver(){
        driver.quit();
        driver = null;
    }

    public static void closeDriver(){
        driver.close();
    }
}
