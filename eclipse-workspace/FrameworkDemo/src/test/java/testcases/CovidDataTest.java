package testcases;

import com.aventstack.extentreports.ExtentTest;
import entities.HospitalBed;
import pageObjects.LandingPage;
import reporting.TestManager;
import utils.DriverManager;
import utils.ExcelUtil;

import java.util.List;

public class CovidDataTest {

    public static void main(String[] args) {

        try {
            //DriverManager.getDriver();
            ExtentTest t1 = TestManager.startTest("Test 1", "To verify that Test 1 run");
            t1.info("Started test");

            LandingPage landingPage = new LandingPage();
            System.out.println("Total count of hospital: " + landingPage.getTotalHospital());

            List<HospitalBed> bedList = landingPage.performRowOperation();

            //Write into Excel
            ExcelUtil.createExcel(bedList);

            TestManager.endTest();

        }finally {
            DriverManager.quitDriver();
        }
    }
}
