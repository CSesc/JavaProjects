package utils;

import entities.HospitalBed;
import io.github.bonigarcia.wdm.WebDriverManager;
import utkarsh.SiteScrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DelhiGovCovidSite {
	public String url="https://coronabeds.jantasamvad.org/beds.html";
    WebDriver driver;
    public DelhiGovCovidSite(){
    	WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//*[@id='hospitals_list']/tr[1]/th/a[2]//span")
    protected WebElement hospitalName;

    @FindBy(xpath = "//*[@id='hospitals_list']/tr")
    protected List<WebElement> hospitalList;

//    public String getHospitalName(){
//        System.out.println("Inside getHospitalname");
//        test.info("getHospitalname");
//        return hospitalName.getText();
//    }
	private static String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM HH:mm:ss");
       return sdf1.format(timestamp);       
	}

    public int getTotalHospital(){
        return hospitalList.size();
    }

    public List<HospitalBed> performRowOperation(){
    	driver.get(url);
        List<HospitalBed> bedList = new ArrayList<HospitalBed>();
        int hosNameRow=1;
        int addressRow = 2;
        int loopSize=3;
        if(SiteScrapper.debug==false)
        	{
        		System.out.println("Loop Disabled");
        		loopSize=hospitalList.size()/2;
        		      	
        	}
        
        for(int i = 1;i<=loopSize;i++){

            System.out.println("*********************************");

            WebElement hospNameEle = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/th/a[2]"));
            System.out.println(hospNameEle.getText());

            WebElement lastUpdated = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[1]//a"));
            System.out.println("Last updated: "+lastUpdated.getText());

            WebElement totalBeds = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[2]/a"));
            System.out.println("totalBeds: "+totalBeds.getText());

            WebElement normalBed = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+hosNameRow+"]/td[3]/a"));
            System.out.println("Vacant: "+normalBed.getText());

            hospNameEle.click();
           // CommonUtils.waitForSeconds(2);

            WebElement address = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+addressRow+"]//div[@class='card-body']/p"));
            System.out.println("Hospital Address: " +address.getText());

            WebElement phone = driver.findElement(By.xpath("//*[@id='hospitals_list']/tr["+addressRow+"]//div/ul/li[2]/a"));
            System.out.println("Contact: " +phone.getText());

            hospNameEle.click();
            System.out.println("*********************************");
            bedList.add(new HospitalBed(hospNameEle.getText(),"",phone.getText(),normalBed.getText(),"",address.getText(),"","","","",totalBeds.getText(),getTimeStamp()));
            //This condition is required to break the limits
            if(hosNameRow< (hospitalList.size()-2)){
                hosNameRow=hosNameRow+2;
                addressRow=addressRow+2;
            }
            
        }
        driver.quit();
        return bedList;
        

    }
}
