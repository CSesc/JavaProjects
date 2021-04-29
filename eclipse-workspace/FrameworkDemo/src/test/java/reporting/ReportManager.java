package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import extra.FileExtensions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DateUtil;
import utils.DriverManager;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ReportManager {

    private static ExtentReports extent;

    public synchronized static ExtentReports getReporter() {
        if (extent == null) {

            extent = new ExtentReports();
            String reportPath = "reports/";
            String fileName = "MyReport-"+DateUtil.getDateInFormat("dd-MM-yyyy-HH-mm-ss") + FileExtensions.HTML;
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath+fileName);
            //ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath+fileName);
            htmlReporter.config().setDocumentTitle("My title");
            extent.attachReporter(htmlReporter);

            extent.setSystemInfo("Project","My Project");

            /*String workingDir = System.getProperty("user.dir");
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                //extent = new ExtentReports(workingDir + "\\ExtentReports\\ExtentReportResults.html", true);
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                //extent = new ExtentReports(workingDir + "/ExtentReports/ExtentReportResults.html", true);
            }*/
        }
        return extent;
    }

    public static void markTestFailAndAttachException(Throwable e) {
        String exceptionMsg = Arrays.toString(e.getStackTrace());
        TestManager.getTest().fail("<details>" + "<summary>" + "<b>" +"<font color='red'>"
                + "Exception Occurred : Click to see"
                + "</font> </b>" + "</summary>" + "Exception Message:" +e.getMessage() +"<br>"
                + exceptionMsg.replaceAll(",", "<br>")
                + "</details>" + "\n");

        try {
            //TestManager.getTest().info(e,MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(),"Title").build());
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    public static void attachScreenshotAsLink() {
        //String exceptionMsg = Arrays.toString(e.getStackTrace());
        TestManager.getTest().fail("<details>" + "<summary>" + "<b>" +"<font color='red'>"
                + "Exception Occurred : Click to see"
                + "</font> </b>" + "</summary>" + "</details>" + "\n");

        try {
            //TestManager.getTest().info(e,MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(),"Title").build());
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    public static String takeScreenshot() {
        String fileName = "snap-"+ DateUtil.getCurrentTimeInMilis()+FileExtensions.PNG;
        String filePath = "reports/captures/"+fileName;
        String base64StringofScreenshot ="";
        try {
            File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            //byte[] fileContent = FileUtils.readFileToByteArray(src);
            //base64StringofScreenshot = "data:image/png;base64,"+ Base64.getEncoder().encodeToString(fileContent);
            File dest = new File(filePath);
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "captures/"+fileName;
    }

    public static void attachFailScreenshot(){
        try {
            TestManager.getTest().log(Status.FAIL,"<b>" +"<font color='red'>"
                    + "See Screenshot Below:"
                    + "</font> </b>", MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
            //TestManager.getTest().addScreenCaptureFromPath(takeScreenshot());
            TestManager.getTest().info("Info",MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
