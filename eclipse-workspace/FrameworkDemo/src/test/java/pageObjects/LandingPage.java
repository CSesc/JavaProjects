package pageObjects;

import com.aventstack.extentreports.ExtentTest;
import entities.HospitalBed;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import reporting.TestManager;
import utils.CommonUtils;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.List;

public class LandingPage {

    ExtentTest test;
    WebDriver driver;

    public LandingPage(){
        test = TestManager.getTest();
        driver = DriverManager.getDriver();
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//*[@id='hospitals_list']/tr[1]/th/a[2]//span")
    protected WebElement hospitalName;

    @FindBy(xpath = "//*[@id='hospitals_list']/tr")
    protected List<WebElement> hospitalList;

    public String getHospitalName(){
        System.out.println("Inside getHospitalname");
        test.info("getHospitalname");
        return hospitalName.getText();
    }

    public int getTotalHospital(){
        return hospitalList.size();
    }

    public List<HospitalBed> performRowOperation(){
        List<HospitalBed> bedList = new ArrayList<>();
        int hosNameRow=1;
        int addressRow = 2;
        for(int i = 1;i<=hospitalList.size()/2;i++){

            System.out.println("*********************************");

            WebElement hospNameEle = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/th/a[2]"));
            System.out.println(hospNameEle.getText());

            WebElement lastUpdated = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[1]//a"));
            System.out.println("Last updated: "+lastUpdated.getText());

            WebElement totalBeds = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[2]/a"));
            System.out.println("totalBeds: "+totalBeds.getText());

            WebElement vacantBeds = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[3]/a"));
            System.out.println("Vacant: "+vacantBeds.getText());

            hospNameEle.click();
            CommonUtils.waitForSeconds(2);

            WebElement hosAddress = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+addressRow+"]//div[@class='card-body']/p"));
            System.out.println("Hospital Address: " +hosAddress.getText());

            WebElement contact = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+addressRow+"]//div/ul/li[2]/a"));
            System.out.println("Contact: " +contact.getText());

            hospNameEle.click();
            System.out.println("*********************************");

            bedList.add(new HospitalBed(hospNameEle.getText(),hosAddress.getText(),contact.getText(),totalBeds.getText(),vacantBeds.getText(),lastUpdated.getText()));

            //This condition is required to break the limits
            if(hosNameRow< (hospitalList.size()-2)){
                hosNameRow=hosNameRow+2;
                addressRow=addressRow+2;
            }

        }
        return bedList;
    }
}
