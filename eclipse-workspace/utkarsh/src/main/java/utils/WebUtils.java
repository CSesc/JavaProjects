package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;

import utkarsh.SiteScrapper;

public class WebUtils {
	

	public static Boolean isElementPresent( String xpath,WebDriver driver) {
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
	@SuppressWarnings("unused")
	public static void sleep(double i) {
		try {int sleep=(int) i;
			Thread.sleep(sleep*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	public static void scrollToElement(WebElement webElement,WebDriver driver) {
		debug("Scrolling to Element: "+webElement.getText().replaceAll("\\r\\n|\\r|\\n", " | "));
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);",webElement);		
}	
	public static void print(String message) {System.out.println("INFO: "+message);}
	public static void debug(String message) {if(SiteScrapper.debug==true)System.out.println("DEBUG: "+message);}
	public static void error(String message) {System.err.println("ERROR: "+message);}

	
	
	@SuppressWarnings("unused")
	private static void zoomOut() {
		WebUtils.sleep(2);
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

	public static String getTimeAppender() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_HH-mm");
       return sdf1.format(timestamp);       
	}
	public static String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM HH:mm:ss");
       return sdf1.format(timestamp);       
	}

	public static void newScreen(String name, WebDriver driver)
	{
		try {
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		String outDir="ScreenShots";
		String fName=outDir + "//"+name +"_" + (new java.sql.Timestamp(System.currentTimeMillis())).toString().replaceAll(":","-").replaceAll(" ","_") + ".png";
		WebUtils.print("Taking ScreenShot: " +fName);
				
		File DestFile= new File(fName);
		FileHandler.copy(SrcFile, DestFile);
		} catch (IOException e) {
			System.out.println("ERROR Taking Screen Shot");
			e.printStackTrace();
		}
	
	}
}
