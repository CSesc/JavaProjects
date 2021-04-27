import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.interactions.Actions;
import org.omg.CORBA.TIMEOUT;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@SuppressWarnings("unused")
public class Test {
	//// CONFIGS
	private static boolean debug=false;
	private static float shortTime=.5f;
	private static float mediumTime=1.2f;
	private static int normalImplicitWait=3;
	private static String disclaimer="This information is extracted from: https://covidrelief.glideapp.io/, we hold no responsibility on the validity or accuracy of the information\nThis is done only to help peple in urgent needs get instant information";
	///////////////
    private static String workflow="Oxygen Availability"; //Oxygen Availability		Vacant Beds Tracker		Ambulance Availability
	private static String city="Delhi";
	private static String fileName="Beds"+"_"+workflow+".csv";
	static WebDriver driver ;
	//static String url="https://covidrelief.glideapp.io/dl/ewAiAHQAIgA6ADAALAAiAHMAIgA6ACIAbQBlAG4AdQAtADIAMQBlADcANgA4ADkANwAtAGQANgBiADYALQA0ADQANgAzAC0AOABmAGMAMQAtADcAMwA1ADAAOQAxADgAMABlADgAZgA2AC0AMQBkAGYAOQBmAGUAYgAxAGQANABmAGMAYQAzAGYAZgA4AGYAOAA3ADEAOAA1ADkAMgA1ADIAZABiAGMAZQAwACIALAAiAHIAIgA6ACIAZABIAE4ASQBYAGQAYgB2AFEARgB1AGYALQB4AG0AdgBRAHUATwBjADAAZwAiACwAIgBuACIAOgAiAEQAZQBsAGgAaQAiAH0A";
	static String url="https://covidrelief.glideapp.io/";
    static String searchBox="//input";

    static String mainPageRowElements="//div[@role='cell']";
    static String rowElements="//div[@role='button']";
    static String clickTextXpath="//*[contains(text(),'replaceText')]";

    
    static String hospitalRowElements="//div[@role='row']";
    //static String hospitalRowElements="//*[@class='ReactVirtualized__Grid__innerScrollContainer']/div";
  
	private static String commentsXpath="//div[@data-test='app-comment']";
	private static String backButton="//button[@data-test='back-button']";
	private static Set <String> finalRows = new HashSet<String>() ;

	 public static void main(String[] args) throws InterruptedException {
		 
		 
		 	
		 	System.out.println("############## STARTING ############## ");
		 	
		 	fileName=workflow+(workflow=="Vacant Beds Tracker"?"_"+city:"")+getTimeAppender()+".csv";
		 	
		 	
	        System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
	        initialize();
	        clickByText(workflow);
	        searchAndOpenCityPage();	//if Wrkflow is for Vacant Beds 
	        getList() ;
	        
//	        driver.findElement(By.xpath(searchBox)).sendKeys(city);
//	        WebElement cityRow= getRowElement( rowElements,city);
//	        clickWebElement(cityRow);sleep(mediumTime);
	        PrepareFile();
    	
    	processRows();
    	//fprint(disclaimer);
        System.out.println("################ END OF PROGRAM ##################");
        Thread.sleep(20000);
        driver.close();
	}

	 private static void getList() {
	        
	        List<WebElement> rows = driver.findElements(By.xpath(hospitalRowElements));
     	String prevLoop="current";
     	String curentLoop="last";String content="";
     	while(!curentLoop.contentEquals(prevLoop))
	        {	
	        	rows = driver.findElements(By.xpath(hospitalRowElements));
	        	for(int j=0;j<rows.size();j++)
		        {
	        		content=rows.get(j).getText().replaceAll("\\r\\n|\\r|\\n", " | ");
		        	finalRows.add(content);
		        	print(content);
		        	rows = driver.findElements(By.xpath(hospitalRowElements));//refresh
		        }
	        	prevLoop=curentLoop;
	        	curentLoop=content;
	        	scrollToElement(rows.get(rows.size()-1));sleep(1);

	        }
	        System.out.println("ITERATING SET");
	        int ctr=1;
	        Iterator<String> itr = finalRows.iterator(); 
	        while(itr.hasNext()){
	        	  print(ctr++ +" : "+itr.next() );
	        	}
	        
		
	}
		private static void scrollToElement(WebElement webElement) {
		debug("Scrolling to Element: "+webElement.getText().replaceAll("\\r\\n|\\r|\\n", " | "));
		 JavascriptExecutor js=(JavascriptExecutor) driver;
		 js.executeScript("arguments[0].scrollIntoView(true);",webElement);		
	}

	private static void clickByText(String text) {
		clickWebElement(driver.findElement(By.xpath(clickTextXpath.replace("replaceText",text))));
		sleep(mediumTime);		
	}


	


	private static void searchAndOpenCityPage() {
		if(workflow=="Vacant Beds")
			serchAndClick(city);
	}


	private static void serchAndClick(String city) {
		driver.findElement(By.xpath(searchBox)).sendKeys(city);
		clickByText(city);

	}


	private static void initialize() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(normalImplicitWait, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url); 		
        zoomOut();
	}


	private static void processRows() {
		 print("ZOOM OUT and SCROLL DOWN AND UP ");
		 if(!debug)
			 sleep(10);
		 print("CAPTURE ELEMENTS COMPLETED");

		 List<WebElement> hospitalRows= driver.findElements(By.xpath(hospitalRowElements));
		 String hospital="//div[@class='summary-title']";
		 String bed="//div[@class='summary-subtitle']";
		 String phone="//div[@class='textStyle']";
		
		 for(int i =1; i <hospitalRows.size()-1;i++)
		 {
			 
			 hospitalRows= driver.findElements(By.xpath(hospitalRowElements));
			 WebElement currentElement=hospitalRows.get(i);
			 if(isClicable(currentElement))
					 {
				 		i++;
				 		//print("City is now:"+currentElement.getText());
					 }
			 JavascriptExecutor js = (JavascriptExecutor) driver;
			 if(i>10)
				 js.executeScript("arguments[0].scrollIntoView(true);",hospitalRows.get(i-1));
			 sleep(mediumTime);
			 js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", currentElement);
			 clickWebElement(currentElement);sleep(mediumTime);
			 print(i+" / "+hospitalRows.size()+"\tprocessing :\t"+getText(hospital));
			 String commentText=getComments();
			 String beds=getText(bed);//
			 if(!city.contentEquals("Raipur") && !city.contentEquals("Pune")  )
				 beds=beds.replace("Beds: ","");
			 fprint(getText(hospital)+","+getText(phone)+","+beds+","+commentText);//+","+commentText);
			 if(!driver.getTitle().contains(workflow))
				 clickIfPresent(backButton);
			 sleep(shortTime);

		 }
		 
		  

	}



	private static boolean isClicable(WebElement currentElement) {
		try {
		       new WebDriverWait(driver, 100).until(ExpectedConditions.elementToBeClickable(currentElement));
		       //System.out.println("Element is clickable");
		       return true;
		     }
		catch(TimeoutException e) {
		     //  System.out.println("Element isn't clickable");
			return false;
		    }
		
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

	private static String getText(String xpath) {
		String finalStr=driver.findElement(By.xpath(xpath)).getText();
		finalStr=finalStr.replace("Normal Beds: ","");
		return finalStr.replaceAll(",","-").replaceAll("\\r\\n|\\r|\\n", " | ").replace(" | Oxygen Beds: ",",");	
			}
	private static String getElementText(WebElement element) {return	element.getText().replaceAll(",","-").replaceAll("\\r\\n|\\r|\\n", " | ");}

		private static void clickWebElement(WebElement element) {
		 if(element!=null && element.isDisplayed() && element.isEnabled())
	        	element.click();
	        		
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
	private static void clickIfPresent(String xp) {
		 if(isElementPresent(xp))
		 {
			 //System.out.println("Clicking Element Now: "+xp);
			 driver.findElement(By.xpath(xp)).click();;
		 }
		 }

	private static void print(String message) {System.out.println("INFO: "+message);}
	private static void debug(String message) {if(debug==true)System.out.println("DEBUG: "+message);}
	private static void error(String message) {System.err.println("ERROR: "+message);}

	
	
	
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

public static void PrepareFile() 
{String str="";
	switch(workflow)
	{
		case "Vacant Beds Tracker": str="Hospital,Phone,Beds,Comments,Updated Time";
				if(city.contains("Raipur"))
					str="Hospital,Normal Bed,Oxygen Bed,Comments,Updated On";break;
		case "Ambulance Availability": str="Hospital,Location,Phone,Comments,Updated Time";break;
		case "Oxygen Availability": str="ContactPerson,Phone,Comments,Updated Time";break;
	}
	

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
	str=str+","+getTimeStamp();
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
	private static String getTimeAppender() {
		// TODO Auto-generated method stub
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_HH-mm");
       return sdf1.format(timestamp);       
	}
	private static String getTimeStamp() {
		// TODO Auto-generated method stub
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM HH:mm:ss");
       return sdf1.format(timestamp);       
	}
	private static void zoomOut() {
		sleep(2);
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
}
