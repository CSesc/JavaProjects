import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class Email {
	public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        System.setProperty("webdriver.chrome.driver", "C:\\bin\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://accounts.google.com/ServiceLogin?");
        // gmail login
        driver.findElement(By.xpath("//input")).sendKeys("smilecosts00@gmail.com");
        driver.findElement(By.id("identifierNext")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Kriti7333");
        driver.findElement(By.id("passwordNext")).click();

        // some optional actions for reaching gmail inbox
        driver.findElement(By.xpath("//*[@title='Google apps']")).click();
        driver.findElement(By.id("gb23")).click();
        // clicks compose
        driver.findElement(By.cssSelector(".T-I.J-J5-Ji.T-I-KE.L3")).click();
        // types message in body without hampering signature
        driver.findElement(By.id(":pg")).sendKeys("This is an auto-generated mail");;

}}