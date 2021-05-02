package utkarsh;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.interactions.Actions;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
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

import entities.HospitalBed;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.AppendExcel;
import utils.EmailSender;
import utils.ExcelUtil;
import utils.DelhiGovCovidSite;

@SuppressWarnings("unused")
public class PostScrapper {
	//// CONFIGS
	private static boolean debug=true;

	private static int normalImplicitWait=3;
	private static String disclaimer="This information is extracted from: https://covidrelief.glideapp.io/, we hold no responsibility on the validity or accuracy of the information\nThis is done only to help peple in urgent needs get instant information";
	///////////////
	private static String city="Delhi"; //Delhi  Gurugram Thane
	private static String workflow="Vacant Beds Tracker";
// 	private static String workflow="Oxygen Availability";		
    ///////////
	private static float shortTime=.5f;
	private static float mediumTime=1.2f;
	private static String fileName=city+"_"+workflow+".csv";
	private static String excelFileName="C:\\Users\\mod-X\\Google Drive\\COVID_INFO\\"+city+"_"+workflow+".csv";
	
	static WebDriver driver ;
	static String bedsDelhiUrl="https://covidrelief.glideapp.io/dl/ewAiAHQAIgA6ADAALAAiAHMAIgA6ACIAbQBlAG4AdQAtADIAMQBlADcANgA4ADkANwAtAGQANgBiADYALQA0ADQANgAzAC0AOABmAGMAMQAtADcAMwA1ADAAOQAxADgAMABlADgAZgA2AC0AMQBkAGYAOQBmAGUAYgAxAGQANABmAGMAYQAzAGYAZgA4AGYAOAA3ADEAOAA1ADkAMgA1ADIAZABiAGMAZQAwACIALAAiAHIAIgA6ACIAZABIAE4ASQBYAGQAYgB2AFEARgB1AGYALQB4AG0AdgBRAHUATwBjADAAZwAiACwAIgBuACIAOgAiAEQAZQBsAGgAaQAiAH0A";
	/////XPATHS
	static String url="https://covidrelief.glideapp.io/";
    static String searchBox="//input";
    static String mainPageRowElements="//div[@role='cell']";
    static String rowElements="//div[@role='button']";
    static String clickTextXpath="//*[contains(text(),\"replaceText\")]";
    static String hospitalRowElements="//div[@role='row']";
	private static String commentsXpath="//div[@data-test='app-comment']";
	private static String backButton="//button[@data-test='back-button']";
	private static Set <String> finalRows = new HashSet<String>() ;
//,nivi306@gmail.com,xautomation8@gmail.com
	private static String mailingList="shubhangna.sinha@gmail.com,smilecosts00@gmail.com";

	public static void main(String[] args) throws InterruptedException {
		 	System.out.println("############## STARTING ############## ");
		 	print("Args Length: "+args.length);
		 	if(args.length>0)
		 		{
		 		print("Current Debug"+debug);
		 			debug=(args[0].toUpperCase().contains("FALSE")?false:(args[0].toUpperCase().contains("TRUE")?true:debug));
		 			print("CLI Args: "+args[0]);
			 		
		 		}
		 	
///////FIRST SITE
		 	try {///site 1
		 		GetOxygenInfo();
		 	}catch(Exception e) {error("\nERROR OCCURED WHILE SCRAPPING SITE 1: "+url);
		 	e.printStackTrace();}

	       // sendEmail();
	        System.out.println("################ END OF PROGRAM ##################");
	}
	private static void GetOxygenInfo() {
		  
		  	workflow="Live Oxygen Leads";
			initialize();
	        clickByText(workflow);
	        searchAndOpenCityPage();	//if Workflow is for Vacant Beds 
	        sleep(7);
	        acceptDisclaimer();
	       // getList() ;
	        PrepareFile();
	        processPosts();
	        if(debug)sleep(10);
	        driver.close();		
	}
	
	private static void acceptDisclaimer() {
		driver.switchTo().alert().dismiss();
driver.findElement(By.xpath("//button[contains(text(),'Understand')]")).click();		
	}
	private static void processPosts() {
//
		String listItemXpath="\\[@role=listitem]'";
		List<WebElement> elements=driver.findElements(By.xpath(listItemXpath));
		for(WebElement element : elements )
		{
			print(element.getText());
		}
		
	}
	private static void sendEmail() {
		String mailMessage="Current Status for : \nProcesed : "+workflow + 
        		"\nCompleted At :\t"+getTimeStamp()+(workflow=="Vacant Beds Tracker"?"\nCity Covered :\t"+city:"")
        		+"\nMailingList : "+mailingList;
        		
        EmailSender.SendMail(mailingList, workflow+ " Results",mailMessage, excelFileName,excelFileName);		
	}
	private static void processRows2() {
		print("ITERATING LIST SIZE:"+finalRows.size());
        int ctr=1;
        Iterator<String> itr = finalRows.iterator(); 
        List <HospitalBed> HospitalBedz = new ArrayList<HospitalBed>();
        while(itr.hasNext()){
        	  String Content=itr.next();
        	  print(ctr++ +"/"+finalRows.size()+" : "+Content);
    		  serchAndClick(Content);  
    		  HospitalBedz.add(fetchData(Content));
        	  clickIfPresent(backButton);
        		if(debug==true && ctr++>2)
	         	{	error("\n\nITERATION HAS BEEN DISABLED");
	         		break;
	         	}
        	} 
        AppendExcel.writeFile(excelFileName,HospitalBedz,false);
			
		 }
	
	 private static HospitalBed fetchData(String currentSearch) {
		String heading="//div[@class='summary-title']";
		String subHeading="//div[@class='summary-subtitle']";
		String generic="//*[@role='button']";
		String upVoteXpath="//div[@data-test='app-expandable-options']";
		String detailZ="";
		String []  finalData=new String[12];
		if(driver.getTitle().contains(currentSearch))
		{
			 List <WebElement> details=driver.findElements(By.xpath(generic));
			 for(int x=0;x<details.size();x++)
				 detailZ=getElementText(details.get(x))+","+detailZ;
		
		
			 String comments=getComments();
			 String dataString=getText(heading)+","+getText(subHeading)+","+detailZ+getText(upVoteXpath);//+","+comments;
			 dataString=dataString.replace("| Oxygen Beds",",Oxygen Beds");
			 String [] data=dataString.split(",");
			 
			 finalData[0]=data[0];
			 
			 if(data[1].contains("Normal Beds: "))
					 finalData[3]=data[1].replace("Normal Beds: ",""); // handle beds here
			 else
				 finalData[1]=data[1];
			 
			 for(int i=2;i<data.length;i++)
			 {
				String value=data[i];
				if(value.contains("Number") || value.contains("Phone") || value.contains("Contact")  )
					finalData[2]=value.replace("Number | ", "").replace("Phone | ", "").replace("Contact | ", "");
				
				if(value.contains("Normal Beds: "))
					finalData[3]=value.replace("Normal Beds: ", "").replace("Vacancy | ", "");
				if(value.contains("Beds") && !(value.contains("Normal Beds") || value.contains("Oxygen Beds")))
					finalData[3]=value.replace("Beds: ", "").replace("Vacancy |", "");
				
				if(value.contains("Oxygen Beds"))
					finalData[4]=value.replace("Oxygen Beds: ", "");
			
				if(value.toUpperCase().contains("ADDRESS"))
					finalData[5]=value;
				
				if(value.contains("Found Useful (In Last 1 hr)"))
					finalData[6]=value.replace("Found Useful (In Last 1 hr) |", "");
				if(value.contains("Notes |"))
					finalData[7]=value.replace("Notes |", "");
				if(value.contains("Upvote"))
					finalData[9]=value.replace("Upvote |", "");
				
			}
			 finalData[8]=comments;
			 finalData[10]="TB";
			 finalData[11]=getTimeStamp();
			 for(int i=0;i<finalData.length;i++)
				 if(finalData[i]==null)
					 finalData[i]="N/A";
			 fprint(String.join(",",Arrays.asList(finalData)));
			 //str="Hospital,Contact,Phone No,Normal Bed,Oxygen Bed,Found Useful(1hr),Notes,Comments,Up Votes,Updated On";
			 //return(new HospitalBed(hospitalName, contact, phone, normalBeds, oxygenBeds, address, foundUseful, notes, comments, votes, totalBeds, lastUpdateTime));
			 
		}
		return(new HospitalBed(finalData[0],finalData[1],finalData[2],finalData[3],finalData[4], finalData[5],finalData[6], finalData[7],
				 finalData[8],finalData[9],finalData[10], finalData[11]));
	
	}
	private static void getList() {
	        
	        List<WebElement> rows = driver.findElements(By.xpath(hospitalRowElements));
     	String prevLoop="current";
     	String curentLoop="last";
     	String content="";
     	//
     	while(!curentLoop.contentEquals(prevLoop))
	       {	
	        	rows = driver.findElements(By.xpath(hospitalRowElements));
	        	for(int i=0;i<rows.size();i++)
		        {
	        		content=rows.get(i).getText();
	        		content=content.replaceAll("\\r\\n|\\r|\\n", " \\| ");
	        		if(content.contains(" | "))
	        			content =content.split(" \\| ")[1];
	        		finalRows.add(content);
		        	rows = driver.findElements(By.xpath(hospitalRowElements));//refresh
		        }
	        	prevLoop=curentLoop;
	        	curentLoop=content;
	        	scrollToElement(rows.get(rows.size()-1));sleep(.6);
	        	
	         	if(debug==true)
	         	{	error("\n\nWHILE HAS BEEN DISABLED");
	         		curentLoop=prevLoop;
	         	}
	       }
		
	}
		private static void scrollToElement(WebElement webElement) {
			debug("Scrolling to Element: "+webElement.getText().replaceAll("\\r\\n|\\r|\\n", " | "));
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);",webElement);		
	}	

	private static void clickByText(String text) {
		WebElement element=driver.findElement(By.xpath(clickTextXpath.replace("replaceText",text)));
		clickWebElement(element);
		sleep(mediumTime);		
	}

	private static void searchAndOpenCityPage() {
		if(workflow=="Vacant Beds Tracker" || workflow=="Live Oxygen Leads")
			serchAndClick(city);
	}
	private static void serchAndClickRowElement(String text) {
		driver.findElement(By.xpath(searchBox)).sendKeys(text);
		clickIfPresent(rowElements);

	}


	private static void serchAndClick(String text) {
		WebElement searchBar=driver.findElement(By.xpath(searchBox));
		searchBar.clear();  sleep(.4);
		searchBar.sendKeys(text);sleep(1.3);
		try {clickByText(text);
  	  }
  	  catch(Exception e3)
  	  {
  		  print("Unable to click List Item: "+text+"Retrying...");
  		
  		try {
			searchBar.clear();  sleep(1);
			searchBar.sendKeys(text);sleep(1.3);
			clickByText(text);
	  	  }
	  	  catch(Exception e23)
	  	  {
	  		  error("Retry to click Failed for List Item: "+text);
	  	  }
  	  }
		
	}


	private static void initialize() {
		//
		
		print("EXECUTION STARTED FOR\nWORKFLOW: "+workflow+"\nCity: "+city+"\nDebug: "+debug);
		fileName=workflow+(workflow=="Vacant Beds Tracker"?"_"+city+"":"")+getTimeAppender()+".csv";
		excelFileName="C:\\Users\\mod-X\\Google Drive\\COVID_INFO\\"+workflow+(workflow=="Vacant Beds Tracker"?"_"+city+"":"")+".xlsx";
		new File(excelFileName).delete();
		//System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(normalImplicitWait, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url); 		
        //zoomOut();
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

	private static void sleep(double i) {
		try {int sleep=(int) i;
			Thread.sleep(sleep*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	private static String getText(String xpath) {
		String finalStr=driver.findElement(By.xpath(xpath)).getText();
		//finalStr=finalStr.replace("Normal Beds: ","");
		return finalStr.replaceAll(",","-").replaceAll("\\r\\n|\\r|\\n", " | ");//.replace(" | Oxygen Beds: ",",");	
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
			 if(!driver.getTitle().contains(workflow))
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
str="Hospital,Contact,Phone No,Normal Bed,Oxygen Bed,Found Useful(1hr),Notes,Comments,Up Votes,Updated On";

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
if(!debug)
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
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_HH-mm");
       return sdf1.format(timestamp);       
	}
	private static String getTimeStamp() {
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
			e.printStackTrace();
		}
	}
	
}
