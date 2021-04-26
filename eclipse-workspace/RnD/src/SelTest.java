import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class SelTest {

public static WebDriver driver;
public static String driverPath = "C:\\\\bin\\\\chromedriver.exe";

@SuppressWarnings("deprecation")
public static void main(String[] args) {

System.setProperty("webdriver.chrome.driver", driverPath);

DesiredCapabilities caps = DesiredCapabilities.chrome();

LoggingPreferences logPrefs = new LoggingPreferences();
logPrefs.enable(LogType.PERFORMANCE, Level.INFO);

caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

WebDriver driver= new ChromeDriver(caps);
driver.get("https://www.edureka.co/");

List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");

for (LogEntry entry : entries) {
    System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());

}

driver.close();
driver.quit();

}
}