package DEPR;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;


@SuppressWarnings("unused")
public class GetCovidOxygen_DEPR {
	//// CONFIGS
	private static boolean debug=true;
	private static float shortTime=.5f;
	private static float mediumTime=1.2f;

	///////////////
	private static String fileName="Oxygen.csv";
	static WebDriver driver ;
	//static String url="https://covidrelief.glideapp.io/dl/ewAiAHQAIgA6ADAALAAiAHMAIgA6ACIAbQBlAG4AdQAtADIAMQBlADcANgA4ADkANwAtAGQANgBiADYALQA0ADQANgAzAC0AOABmAGMAMQAtADcAMwA1ADAAOQAxADgAMABlADgAZgA2AC0AMQBkAGYAOQBmAGUAYgAxAGQANABmAGMAYQAzAGYAZgA4AGYAOAA3ADEAOAA1ADkAMgA1ADIAZABiAGMAZQAwACIALAAiAHIAIgA6ACIAZABIAE4ASQBYAGQAYgB2AFEARgB1AGYALQB4AG0AdgBRAHUATwBjADAAZwAiACwAIgBuACIAOgAiAEQAZQBsAGgAaQAiAH0A";
	static String url="https://covidrelief.glideapp.io/";
    static String searchBox="//input";
	private static String city="Delhi";

    static String mainPageRowElements="//div[@role='cell']";
    static String bedCityRowElements="//div[@role='button']";
    static String hospitalRowElements="//div[@role='button']";
	private static String commentsXpath="//div[@data-test='app-comment']";
	private static long normalImplicitWait=3;

	 public static void main(String[] args) throws InterruptedException {
		 	System.out.println("############## STARTING ############## ");
		
		 	
	        System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
	        driver = new FirefoxDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.manage().window().maximize();
	        driver.get(url); 
	        WebElement oxygenAvailability= getRowElement( mainPageRowElements,"Oxygen Availability");
	        clickWebElement(oxygenAvailability);sleep(mediumTime);
			driver.findElement(By.xpath(searchBox)).sendKeys(city);

	        PrepareFile("City,Name,Location,Phone,Notes,Verified");
	        processRows();
 
	        System.out.println("################ END OF PROGRAM ##################");
	        Thread.sleep(20000);
	        driver.close();
//	        
	         
	}

	 private static void processRows() {
		 zoomOut();
		 print("take elements");
		 if(!debug)
			 sleep(10);
		 print("take Complete");

		 List<WebElement> hospitalRows= driver.findElements(By.xpath(hospitalRowElements));
		 String name="//div[@class='summary-title']";
		 String location="//div[@class='summary-subtitle']";
		 
		 String generic="//div[@class='textStyle']";
		 String currentCity="NULL";
		 for(int i =0; i <hospitalRows.size()-1;i++)
		 {
			 
			 hospitalRows= driver.findElements(By.xpath(hospitalRowElements));
			 WebElement currentElement=hospitalRows.get(i);
			 JavascriptExecutor js = (JavascriptExecutor) driver;
			 if(i>10)
				 js.executeScript("arguments[0].scrollIntoView(true);",hospitalRows.get(i-1));
			 highlight(currentElement);
			
			 sleep(mediumTime);
			 clickWebElement(currentElement);
				 //Clickable and clicked
				 sleep(mediumTime);
				 print(i+" / "+hospitalRows.size()+"\tprocessing :\t"+getText(name));
				 List <WebElement> details=driver.findElements(By.xpath(generic));
				 if(details.size()>2)
				 {
					 String detailZ="";
					 for(int x=0;x<details.size();x++)
						 detailZ=getElementText(details.get(x))+","+detailZ;
					 String fString=currentCity+","+getText(name)+getText(location)+","+detailZ;
					 String comments=getComments();
					 fprint(fString+comments);
					 clickIfPresent("//button[@data-test='back-button']");
					 sleep(shortTime);
				 }
				 else
					 {
					 	currentCity=getElementText(currentElement);
					 	print("Processing City: "+currentCity );
					 }
				 
			 

		 }
		 
		  

	}



	private static void highlight(WebElement currentElement) {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 
		 try{js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", currentElement);
		 
		 }
		 catch (Exception e) {
			 error("Unable to highlight");

		 }
		}

	private static void zoomOut() {
		Robot robot;
		try {
			robot = new Robot();
		

		System.out.println("About to zoom out");
		for (int i = 0; i < 20; i++) {
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_SUBTRACT);
			robot.keyRelease(KeyEvent.VK_SUBTRACT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		}		
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getElementText(WebElement webElement) {
		String txt="";
		try{	

			txt=webElement.getText().replaceAll(",","-").replaceAll("\\r\\n|\\r|\\n", " | ");;
		}
		catch(Exception e)
		{error("Unable to get Text");}
		return	txt;
		
	}


	private static String getComments() {
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		List<WebElement> comments=driver.findElements(By.xpath(commentsXpath));
		String commentText="";
		for(int i =0;i<comments.size();i++)
		{
			commentText=commentText+" | "+getElementText(comments.get(i));
		}driver.manage().timeouts().implicitlyWait(normalImplicitWait, TimeUnit.SECONDS);
		return commentText;
	}

	private static void sleep(float i) {
		try {int sleep=(int) i;
			Thread.sleep(sleep*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static String getText(String xpath) {try{
		String txt=driver.findElement(By.xpath(xpath)).getText().replaceAll(",","-");
		return	txt;
		}
	catch(Exception E)
	{
		error(E.getMessage());
		return "No Text";
	}
	}
		private static boolean clickWebElement(WebElement element) {
		try {
		 if(element!=null && element.isDisplayed() && element.isEnabled())
	        	element.click();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	        		
	}

	private static WebElement getRowElement(String rowsXpath,String Text) {
		 List<WebElement> rows=driver.findElements(By.xpath(rowsXpath));
	        for(int i = 0; i <rows.size(); i ++)
	        {
	        	debug("Row "+i + " : "+rows.get(i).getText());
	        	if(rows.get(i).getText().contains(Text))
	        	{
	        		print("Row "+i + " : "+rows.get(i).getText());
	        		return rows.get(i);
	        	}
	        	
	        	
	        }
			return null;		
	}

	private static void print(String message) {System.out.println("INFO: "+message);}
	private static void debug(String message) {if(debug==true)System.out.println("DEBUG: "+message);}
	private static void error(String message) {System.err.println("ERROR: "+message);}

	private static void clickIfPresent(String xp) {
		 if(isElementPresent(xp))
		 {
			 //System.out.println("Clicking Element Now: "+xp);
			 driver.findElement(By.xpath(xp)).click();;
		 }
		 }

	
	
	private static String getElementText(String element) {
		String txt="404_NOT_FOUND";
		try {
				txt=driver.findElement(By.xpath(element)).getText();
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

public static void PrepareFile(String str) 
{ 	

try { 
BufferedWriter out = new BufferedWriter( 
new FileWriter(fileName, false)); 
out.write(""+str); 
out.close(); }
catch (IOException e) { 
System.out.println("exception occoured" + e); 
}
}

public static void fprint( String str) 
{ 	
try { 
BufferedWriter out = new BufferedWriter( 
new FileWriter(fileName, true)); 
out.write(System.lineSeparator()+""+str); 
out.close(); 
} 
catch (IOException e) { 
System.out.println("exception occoured" + e); 
}
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

 
}
