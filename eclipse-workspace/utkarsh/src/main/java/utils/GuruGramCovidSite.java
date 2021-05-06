package utils;

import entities.HospitalBed;
import io.github.bonigarcia.wdm.WebDriverManager;
import utkarsh.SiteScrapper;

import org.apache.commons.lang3.Validate;
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

public class GuruGramCovidSite {
	public String url="https://coronaharyana.in/?city=6";
    WebDriver driver;
    public GuruGramCovidSite(){
    	WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        PageFactory.initElements(driver,this);
    }

    
    @FindBy(xpath = "//*[@class='psahuDiv community-post wow fadeInUp']")
    protected List<WebElement> hospitalList;

	private static String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM HH:mm:ss");
       return sdf1.format(timestamp);       
	}

    public int getTotalHospital(){
        return hospitalList.size();
    }

    public List<HospitalBed> performRowOperation(){
    	String hospitalName="NA",NormalBeds="NA",oxygenBeds="NA",totalBeds="NA",icuBeds="NA",ventilatorBeds="NA",phoneNumber="000";
    	driver.get(url);
        List<HospitalBed> bedList = new ArrayList<HospitalBed>();
        int loopSize=3;
        if(SiteScrapper.debug==false)
        	{
        		System.out.println("Loop Disabled");
        		loopSize=hospitalList.size();
        		      	
        	}
        System.out.println("ARRAY SIZE: "+ hospitalList.size()+ "  Loop SIZE: "+loopSize);
        for(int i = 0;i<loopSize;i++){

           
            String details=hospitalList.get(i).getText();
            if(details.length()<1)
            	continue;
            System.out.println("*********************************\nFetching hospital #"+i);
            System.out.println("Details :"+details);
            String[] lines = details.split("\\n");
            hospitalName=lines[0].replace("Facility Name: ","");
            for(String line : lines)
            {
            	if(line.contains("Availability of Beds"))
            	{
            		String[] values = line.split(",");
            		System.out.println("vlues:"+ values[0]+ " | "+values[1]+ " | "+values[2]+ " | "+values[3]+ " | "+values[4]);
            		totalBeds=process(values[0]);//Total Beds
            		oxygenBeds=process(values[1]);
            		NormalBeds=process(values[3]);//Total Beds
            		icuBeds=process(values[4]);
            		ventilatorBeds=process(values[5]);
            	}

            	if(line.contains("Helpline"))
            	{
            		String[] values = line.split(":");
            		phoneNumber=values[values.length-1];
            	}
            }
        
            System.out.println("*********************************");
            bedList.add(new HospitalBed(hospitalName,"108, 1075",phoneNumber,NormalBeds,oxygenBeds,icuBeds,ventilatorBeds,"","","",totalBeds,getTimeStamp()));
            
        }
       

        driver.quit();
        return bedList;
        

    }

	private String process(String string) {
		return string.replace(string.split(": ")[0]+": ","");
		
	}
}
