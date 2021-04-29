import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
public class AddFbFriend {
	private static String logFileName="C:/temp/fbLogger.txt";
	private static WebDriver driver;
	private static String addFrndXpath="//div/span[contains(text(),'')]";
	private static String loginUrl="https://www.facebook.com/full.stack.10420";
	private static String addFriendUrl="https://www.facebook.com/friends/";
	private static int fullCount=0;
	public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C://bin//drivers//geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C://bin//drivers//chromedriver.exe");
        

      //  driver=new ChromeDriver();
       

        if(false)//args.length>0 && args[0]=="chrome")
	        	driver=new ChromeDriver();
        else
        	 	driver=new FirefoxDriver();
    	System.out.println("First Login Attempt");
    	driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
    	
        if(!login())
        {
        	System.out.println("Second Login Attempt");
        	login();
        }
        	
        	
        
        
       System.out.println("\nAdding Friends: ");
       for(int j=0;j<50;j++)
       {int current=addFriends();
    	  fullCount=fullCount+current;
    	//  System.out.println("\nLoop "+j+" complete : " +current+"\tTotal="+fullCount+" Friends Added ");

       }
       appendStrToFile(logFileName,fullCount+" Friends Added ");
        	
        System.out.println("\n\nFinished Executing\n"+fullCount+" Friends Added ");
        Thread.sleep(30000);
        driver.quit();
	}

	private static boolean login() {
		System.out.println("Logging In");
		boolean status=false;
		driver.get(loginUrl);
        driver.findElement(By.id("email")).sendKeys("fullstackprofessionals@gmail.com");
        driver.findElement(By.id("pass")).sendKeys("Fullstack123");
        driver.findElement(By.id("loginbutton")).click();
        
	        try {Thread.sleep(2000);
	        	
	        	if(driver.findElements(By.id("email")).size()==0)
	        		status=true;
	        	 }
	        catch(Exception e)
	        {
	        	status= false;
	        }
	        System.out.println("Login Status: "+status);
	        return status;
	}

	private static int addFriends() throws InterruptedException {
		int localCount=0;String label="";

		driver.navigate().to(addFriendUrl);
		Thread.sleep(1999);
		List<WebElement> elements=driver.findElements(By.xpath(addFrndXpath));
        //System.out.println("\nAdding Friends: ");
		//System.out.println("Found buttons:"+elements.size());
        for(int i=0;i<elements.size();i++)
          {
        	label=elements.get(i).getText();
        	if(label.contains("Add Friend"))
        		{
        			elements.get(i).click();
        			System.out.print("+");
        			localCount++;
        			break;
        			
        		}
        	
          }
//        for(int i=0;i<elements.size();i++)
//        {	  
// 		   try{
// 			  label=elements.get(i).getText();
// 		   }catch (Exception E){
// 			   System.out.println("unable to fetch label reloading");
// 			  driver.navigate().to(addFriendUrl);
// 			  elements=driver.findElements(By.tagName("button"));
//
// 		  }
// 		  
// 		if(label.contains("Add Friend") )
//        	{	boolean success=true;
//        		   
//        		   try{elements.get(i).click();}
//            	   catch(Exception e) { System.out.print(".");success=false;driver.get(addFriendUrl);}
//        		   
//        		   if(success) {
//        			   localCount++;System.out.print("+");
//        		   }
//        		   else
//        			   System.out.print(".");
//        			   
//        		   Thread.sleep(1000);
//            }	
//        }
        
        return localCount;
}
	
	
	
	
	private static int addFriends1() throws InterruptedException {
			int localCount=0;String label="";
			driver.navigate().to(addFriendUrl);
		 	List<WebElement> elements=driver.findElements(By.xpath(addFrndXpath));
	        System.out.println("\nAdding Friends: ");
	        for(int i=0;i<elements.size();i++)
	        {	  
     		   try{
     			  label=elements.get(i).getText();
     		   }catch (Exception E){
     			   System.out.println("unable to fetch label reloading");
     			  driver.navigate().to(addFriendUrl);
     			  elements=driver.findElements(By.xpath(addFrndXpath));

     		  }
     		 System.out.println(label);
     		if(label.contains("Add Friend") )
	        	{	boolean success=true;
	        		   
	        		   try{elements.get(i).click();}
	            	   catch(Exception e) { System.out.print(".");success=false;driver.get(addFriendUrl);}
	        		   
	        		   if(success) {
	        			   localCount++;System.out.print("+");
	        		   }
	        		   else
	        			   System.out.print(".");
	        			   
	        		   Thread.sleep(1000);
	            }	
	        }
	        
	        return localCount;
	}
	
public static void appendStrToFile(String fileName, 
            String str) 
{ 	
SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//System.out.println();
try { 

// Open given file in append mode. 
BufferedWriter out = new BufferedWriter( 
	
        
new FileWriter(fileName, true)); 
out.write(System.lineSeparator()+timestamp+" :>>> "+str); 
out.close(); 
} 
catch (IOException e) { 
System.out.println("exception occoured" + e); 
} 
} 
}
