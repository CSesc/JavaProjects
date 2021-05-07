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
import utils.GuruGramCovidSite;
import utils.WebUtils;
import utils.DelhiGovCovidSite;

@SuppressWarnings("unused")
public class SiteScrapper {
	//// CONFIGS
	
	private static String city="Pune"; 
	public static boolean debug=true;
	public static boolean isdebug() {
		return debug;
	}
	public static void setdebug(boolean debug) {
		SiteScrapper.debug = debug;
	}
	private static int normalImplicitWait=5;
	private static int longImplicitWait=15; //seconds

	private static String disclaimer="This information is extracted from: https://covidrelief.glideapp.io/, we hold no responsibility on the validity or accuracy of the information\nThis is done only to help peple in urgent needs get instant information";
	///////////////
	private static String workflow="Vacant Beds Tracker";
// 	private static String workflow="Oxygen Availability";		
    ///////////
	private static float shortTime=.5f;
	private static float mediumTime=1.2f;
	private static String fileName=city+"_"+workflow+".csv";
	private static String baseFileLocation="C:\\Users\\mod-X\\Google Drive\\COVID_INFO\\";
	private static String excelFileName=baseFileLocation+city+"_"+workflow+".xlsx";
	static boolean runAll=true;
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
	private static boolean buildSuccess=true;
	public static void main(String[] args) throws InterruptedException {
		 	System.out.println("############## STARTING ############## ");
		 	
		 	//System.out.println("ARGS: "+ args.length);
		 	handleArgs(args);
		 	if(debug)
		 		baseFileLocation=baseFileLocation+"\\TEST"+"\\";
///////GURUGRAM
		 	if(city.contentEquals("Gurugram")||runAll)
	 			{excelFileName=baseFileLocation+"Gurugram_"+workflow+".xlsx";
		 		String[] guruHeader = {"Hospital","HelpLine","Phone No","Normal Bed","Oxygen Bed","ICU Beds","Ventilator Beds","Notes","Comments","Up Votes","Total Beds","Updated On"};
			 	 int [] hideGuruHeader= {7,8,9,10};
			 	 try{AppendExcel.writeFile(excelFileName,guruHeader,hideGuruHeader,new GuruGramCovidSite().performRowOperation(),false);}catch(Exception e) {WebUtils.error("ERROR OCCURED WHILE SCRAPPING SITE 2: Govt Site");WebUtils.newScreen("GURUGRAM_FAILURE",driver);buildSuccess=false;}
	 			}
///////DELHI
		 		if(city.contentEquals("Delhi")||runAll)
		 			{	excelFileName=baseFileLocation+"Delhi_"+workflow+".xlsx";
		 				System.out.println("SITE 2 FOR DELHI ########## : "+ excelFileName+"");
			            String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};
			            int [] hideHeader= {7,8,9};
			            try {AppendExcel.writeFile(excelFileName,header,hideHeader,new DelhiGovCovidSite().performRowOperation(),true);}catch(Exception e) {WebUtils.error("ERROR OCCURED WHILE SCRAPPING SITE 2: Govt Site");WebUtils.newScreen("DELHI_FAILURE",driver);buildSuccess=false;}
		 			}
/////// GENERIC 
		 		try {///site 1
		 			excelFileName=baseFileLocation+city+"_"+workflow+".xlsx";
			 		GetBedsInfo();
		 		}catch(Exception e) {WebUtils.error("ERROR OCCURED WHILE SCRAPPING SITE 1: "+url);e.printStackTrace();WebUtils.newScreen(city+"_FAILURE",driver);buildSuccess=false;}
	        System.out.println("################ END OF PROGRAM ##################");
	        if(buildSuccess)
	        	;//EmailSender.sendSuccessMail();
	}

	private static void GetBedsInfo() {
		  	initialize();
	        clickByText(workflow);
	        searchAndOpenCityPage();	//if Workflow is for Vacant Beds 
	        getList() ;
	        PrepareFile();
	        processRows2();
	        
	        if(debug)WebUtils.sleep(10);
	        
	        driver.close();		
	}
	
	private static void processRows2() {
		WebUtils.print("ITERATING LIST SIZE:"+finalRows.size());
        int ctr=1;
        Iterator<String> itr = finalRows.iterator(); 
        List <HospitalBed> HospitalBedz = new ArrayList<HospitalBed>();
        while(itr.hasNext()){
        	  String Content=itr.next();
        	  WebUtils.print(ctr++ +"/"+finalRows.size()+" : "+Content);
    		  serchAndClick(Content);
    		  
    		  HospitalBedz.add(fetchData(Content));
        	  clickIfPresent(backButton,driver);
        		if(debug==true && ctr>2)
	         	{	WebUtils.error("\n\nITERATION HAS BEEN DISABLED");
	         		break;
	         	}
        	} 
        int [] hideHeader= {7,8,9,10};
        String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};
        AppendExcel.writeFile(excelFileName,header,hideHeader,HospitalBedz,false);
			
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
			 dataString=dataString.replace("| Oxygen",",Oxygen");
			 
			 WebUtils.debug("Size: "+ dataString.split(",").length+" - DATA STRING: "+dataString);
			 String [] data=dataString.split(",");
			 
			 finalData[0]=data[0];
			 
			 if(data[1].contains("Normal")) {
				 finalData[3]=data[1].replace("Normal Beds: ","").replace("Normal: ", ""); // handle beds here 
				 //WebUtils.debug("Found Beds: "+data[1]+" FINAL: "+finalData[3]);
			 }
			 else
				 finalData[1]=data[1];
			 
			 for(int i=2;i<data.length;i++)
			 {
				String value=data[i];
				if(value.contains("Number") || value.contains("Phone") || value.contains("Contact")  )
					finalData[2]=value.replace("Number | ", "").replace("Phone | ", "").replace("Contact | ", "");
				
				if(value.contains("Normal"))
					finalData[3]=value.replace("Normal Beds: ", "").replace("Normal: ","").replace("Vacancy | ", "");
				
				if(value.contains("Beds") && !(value.contains("Normal") || value.contains("Oxygen")))
					finalData[3]=value.replace("Beds: ", "").replace("Vacancy |", "");
				
				if(value.contains("Oxygen"))
					finalData[4]=value.replace("Oxygen Beds: ", "").replace("Oxygen: ", "");
			
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
			 finalData[11]=WebUtils.getTimeStamp();
			 for(int i=0;i<finalData.length;i++)
				 if(finalData[i]==null)
					 finalData[i]="N/A";
			 fprint(String.join(",",Arrays.asList(finalData)));
			 
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
	        	WebUtils.scrollToElement(rows.get(rows.size()-1),driver);WebUtils.sleep(.6);
	        	
	         	if(debug==true)
	         	{	WebUtils.error("\n\nWHILE HAS BEEN DISABLED");
	         		curentLoop=prevLoop;
	         	}
	       }
		
	}


	private static void clickByText(String text) {
		WebElement element=driver.findElement(By.xpath(clickTextXpath.replace("replaceText",text)));
		clickWebElement(element);
		WebUtils.sleep(mediumTime);		
	}

	private static void searchAndOpenCityPage() {
		WebUtils.print("Opening CITY Page: "+ city);
		serchAndClick(city);
	}
	private static void serchAndClickRowElement(String text) {
		driver.findElement(By.xpath(searchBox)).sendKeys(text);
		clickIfPresent(rowElements,driver);

	}

	private static void clickIfPresent(String xp,WebDriver driver) {
		 if(WebUtils.isElementPresent(xp,driver))
		 {
			 if(!driver.getTitle().contains(workflow))
				 driver.findElement(By.xpath(xp)).click();;
		 }
		 }
	private static void serchAndClick(String text) {
		WebElement searchBar=driver.findElement(By.xpath(searchBox));
		searchBar.clear();  WebUtils.sleep(.4);
		searchBar.sendKeys(text);WebUtils.sleep(1.3);
		try {clickByText(text);
  	  }
  	  catch(Exception e3)
  	  {
  		  WebUtils.print("Unable to click List Item: "+text+"Retrying...");
  		
  		try {
			searchBar.clear();  WebUtils.sleep(1);
			searchBar.sendKeys(text);WebUtils.sleep(1.3);
			clickByText(text);
	  	  }
	  	  catch(Exception e23)
	  	  {
	  		  WebUtils.error("Retry to click Failed for List Item: "+text);
	  	  }WebUtils.sleep(.6);
  	  }
		
	}


	private static void initialize() {
		//
		
		WebUtils.print("EXECUTION STARTED FOR\nWORKFLOW: "+workflow+"\nCity: "+city+"\nDebug: "+debug);
		fileName=workflow+(workflow=="Vacant Beds Tracker"?"_"+city+"":"")+WebUtils.getTimeAppender()+".csv";
		//excelFileName="C:\\Users\\mod-X\\Google Drive\\COVID_INFO\\"+workflow+(workflow=="Vacant Beds Tracker"?"_"+city+"":"")+".xlsx";
		//new File(excelFileName).delete();
		//System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(longImplicitWait, TimeUnit.SECONDS);
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
	        	WebUtils.debug("Row "+i + " : "+rows.get(i).getText());
	        	if(rows.get(i).getText().contains(Text))
	        	{
	        		WebUtils.print("Row "+i + " : "+rows.get(i).getText());
	        		return rows.get(i);
	        	}
	        	
	        	
	        }
			return null;		
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
	


	private static void handleArgs(String[] args) {
		if(args.length>0)
 		{WebUtils.print("RECEIVED SYSTEM ARGS: "+args.length+"\n-----------------------");
			for(String arguements : args)
			{
			String []arguement=arguements.split(",");
 			for(String arg :arguement)
 			{
 				if(arg.contains("="))
 					{
 				//WebUtils.print("Contains");
 						String key=arg.split("=")[0];
 						String value=arg.split("=")[1];
 						WebUtils.print (key + "  : "+ value);
 						if(key.toUpperCase().contentEquals("DEBUG"))debug=Boolean.parseBoolean(value);
 						if(key.toUpperCase().contentEquals("WORKFLOW"))workflow=value;
 						if(key.toUpperCase().contentEquals("CITY"))city=value;
 						if(key.toUpperCase().contentEquals("RUNALL"))runAll=Boolean.parseBoolean(value);;
 						
 					}
 				else
 					WebUtils.error(arg + " is in incorrect format");
 				
 			}
			}
 		}
		
	}	
}
