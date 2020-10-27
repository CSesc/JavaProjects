import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;

@SuppressWarnings("unused")
public class SelTest {

	  static WebDriver driver ;
    static String url="https://www.bigbasket.com/";
    static String cartUrl="https://www.bigbasket.com/basket/?ver=1";
    static String strCmp="Unfortunately,";// we do not have any available slots to service you right now. Please try again later!";
    static int cnt=0;
    
    static String checkoutXpath="//*[@id=\"checkout\"]/p";
    
    //static String popupXpath="//html/body/div[2]/div[5]/div/div/div[4]/div/div[1]/span";
    static String popupXpath="//*[@class='slotmodal-content']/div/p[@id='slotContent']";
    static String popupXpath2="//*[@id=\"slotContent\"]";

	 public static void main(String[] args) throws InterruptedException {
		 	System.out.println("############## STARTING ############## ");
		
		 	String xpath="//a[@ng-show='vm.newLoginFlow'][contains(.,'Login')]";
	        System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
	        driver = new FirefoxDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.manage().window().maximize();
	        driver.get(url); 
	        driver.findElement(By.xpath(xpath)).click();
	        driver.findElement(By.xpath("//*[@id=\"otpEmail\"]")).sendKeys("7798927033");
	        driver.findElement(By.xpath("//*[@id=\"login\"]/login/div/form/div[2]/button")).click();
	        
	        System.out.println("############## ENTER OTP ############## ");
	        Thread.sleep(30000);
	      
	        System.out.println("############## SET HOME LOCATION  ##############");
	        Thread.sleep(3000);
	        //SETTING HOME LOCATION
	        driver.findElement(By.xpath("(//span[contains(@ng-bind,'vm.user.currentAddress.city_name')])[1]")).click();
	        System.out.println("Clicking Home To set Location");
	        Thread.sleep(2000);
	        clickIfPresent("(//p[contains(@class,'other-address-loc')])[1]");
	        System.out.println("Clicking Continue");
	        Thread.sleep(7000);
	        clickIfPresent("//button[@type='button'][contains(.,'CONTINUE')]");
	        System.out.println("Clicking Continue Again");
	        clickIfPresent("(//button[contains(@class,'btn btn-default yellow-btn')])[1]");
	        //btn btn-default yellow-btn
	        System.out.println("CONTINUEING with LOOP");
	        looper();
	        System.out.println("################ END OF PROGRAM ##################");
	         
	}

	 private static void clickIfPresent(String xp) {
		 if(isElementPresent(xp))
		 {
			 System.out.println("Clicking Element Now: "+xp);
			 driver.findElement(By.xpath(xp)).click();;
		 }
		 
	}

	private static void looper()
	 {
		 try {
			 while(true)
			 {
				System.out.println("Retrying : " + cnt++);
		    	driver.get(cartUrl); //Go to Cart    
		    	driver.findElement(By.xpath(checkoutXpath)).click();//Click on Checkout
		        Thread.sleep(1000);
		    	newScreen("MissingItems");
		    	String x=getElementText("//*[@id=\"slotContent\"]");
				if(x=="")
					x=getElementText("//*[@id=\"slotContent\"]");
		    	System.out.println("Got Response: "+x);
		    		
		    	if(!x.contains(strCmp))
		    		{	int ctr=6;int delay=700;
		    			iftttTrigger(ctr,delay);
		    		}

		    	
		     }
			  	
		    }
	        catch(Exception e)
			 {
	        	looper();
			 }       	
	        
	}

	private static void iftttTrigger(int ctr,int delay) throws InterruptedException {
		WebDriver driver2=new FirefoxDriver();
		int i=0;
		while(i++<ctr)
		{
			Thread.sleep(delay);
			funTrigger(driver2);
		}
		driver2.quit();
	}

	private static void funTrigger(WebDriver driver2) {
		System.out.println("Unsucessful Message Not displayed Probably Empty Slot available");
		driver2.get("https://maker.ifttt.com/trigger/BB_Empty_Slot_Found/with/key/eY_8eOmlSa8OvicW4OvFac2poUcn7kHx6xSJaaD5w12");
		String src=driver2.getPageSource();
		if(src.contains("Congratulations!"))
			System.out.println("IFTTT TRIGGER SUCCESS: "+driver2.getPageSource());
		else
			System.out.println("Unsucessful Trigger !!!");		
	}

	private static String getElementText(String string) {
		String txt="404_NOT_FOUND";
		try {
				txt=driver.findElement(By.xpath(string)).getText();
		}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return txt;
}

	private static Boolean isElementPresent( String xpath) {
		Boolean elementFound=false;
		try {
				driver.findElement(By.xpath(xpath)).isDisplayed();
				driver.findElement(By.xpath(xpath)).isEnabled();
				elementFound=true;
		}
		catch(Exception e)
			{
				elementFound=false;
				e.printStackTrace();
			}
		return elementFound;
	}


	private static void newScreen(String element)
	{
		try {
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		String outDir="C://REPO//ScreenShots//BigBasket";
		String fName=outDir + "//"+element +"_" + (new java.sql.Timestamp(System.currentTimeMillis())).toString().replaceAll(":","-").replaceAll(" ","_") + ".png";
		File DestFile= new File(fName);
		FileHandler.copy(SrcFile, DestFile);
		} catch (IOException e) {
			System.out.println("ERROR Taking Screen Shot");
			e.printStackTrace();
		}
	
	}

//	private static void mouseOver(WebDriver driver, String xpath) {
//		System.out.println("MOUSE OVER ");
//		Actions action = new Actions(driver);
//		WebElement element = driver.findElement(By.xpath(xpath));
//		action.moveToElement(element).perform();		
//	}

}
