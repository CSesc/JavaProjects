import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

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
	private static String addFriendUrl="https://www.facebook.com/?sk=ff";
	private static int fullCount=0;
	public static void main(String[] args) throws InterruptedException {
		String url="https://www.facebook.com/full.stack.10420";
        System.setProperty("webdriver.gecko.driver", "C://bin//geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C://bin//chromedriver.exe");

        driver=new ChromeDriver();
 	   
        if(args.length>0 && args[0]=="chrome")
	        	driver=new FirefoxDriver();
	     
	    driver.get(url);
        driver.findElement(By.id("email")).sendKeys("fullstackprofessionals@gmail.com");

        driver.findElement(By.id("pass")).sendKeys("Fullstack123");
        driver.findElement(By.id("loginbutton")).click();
       for(int j=0;j<5;j++)
       {
    	   try{fullCount=fullCount+addFriends();}
    	   catch(Exception e)
    	   {}
    	   appendStrToFile(logFileName,System.lineSeparator()+fullCount+" Friends Added ");
    	   
       }
        	
        System.out.println("finished executing");
        Thread.sleep(30000);

        driver.quit();
	}

	private static int addFriends() throws InterruptedException {
			driver.navigate().to(addFriendUrl);
		 	int localCount=0;
	        List<WebElement> elements=driver.findElements(By.tagName("button"));
	        System.out.println("\nAdding Friends: ");
	        for(int i=0;i<elements.size();i++)
	        {	
	        	String label=elements.get(i).getText();
	        	if(label.contains("Add Friend") && localCount<10)
	        	{	boolean success=true;
	        		   System.out.print("+");
	        		   try{elements.get(i).click();}
	            	   catch(Exception e) {success=false;driver.get(addFriendUrl);}
	        		   
	        		   if(success)localCount++;
	        		   Thread.sleep(1000);
	            }	
	        }
//	        System.out.println(System.lineSeparator()+localCount+" Friends Added\n ");
	        
	        return localCount;
	}
	
public static void appendStrToFile(String fileName, 
            String str) 
{ 	
SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
Timestamp timestamp = new Timestamp(System.currentTimeMillis());
System.out.println();
try { 

// Open given file in append mode. 
BufferedWriter out = new BufferedWriter( 
	
        
new FileWriter(fileName, true)); 
out.write(timestamp+" :>>> "+str); 
out.close(); 
} 
catch (IOException e) { 
System.out.println("exception occoured" + e); 
} 
} 

}
