package com.platformLayer;
import com.relevantcodes.extentreports.*;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import org.testng.Assert;
import org.testng.ITestResult;
//import library.Utility;

public class ExtentReportsVK {
	public static ExtentReports myReport;
	public static ExtentTest myLogger;
	public static WebDriver driver;
	public static String screenShotPath = "C:\\ExtentReports\\Screenshots";
	public static ExtentTest myTest;
	
	@BeforeSuite
	public void ReportSetup()
	{
		myReport = new ExtentReports("C:\\ExtentReports\\VK.html");
	}
	
	
	@BeforeMethod
	public void ReportSetup1(Method method)
	{		
		myLogger = myReport.startTest(method.getName());
		myTest = myReport.startTest(method.getName());
	}
	
	@Test
	public void VerifyBlogTitle()
	{
		myLogger.log(LogStatus.PASS,"Test started");
		
		driver=new FirefoxDriver();
		driver.manage().window().maximize();
		myLogger.log(LogStatus.INFO,"Browser started");
		driver.get("http://www.learn-automation.com");
		myLogger.log(LogStatus.INFO,"Application is up & running");
		String title = driver.getTitle();
		Assert.assertTrue(title.contains("Google"));
		myLogger.log(LogStatus.PASS,"Title verified");		
	}
	
	@Test
	public void VerifyGoogleTitle()
	{
		myLogger.log(LogStatus.PASS,"Test started");
		System.out.println("VerifyGoogleTitle - 1");
		driver=new FirefoxDriver();
		System.out.println("VerifyGoogleTitle - 2");
		driver.manage().window().maximize();
		System.out.println("VerifyGoogleTitle - 3");
		myLogger.log(LogStatus.INFO,"Browser started");
		driver.get("http://www.google.com");
		System.out.println("VerifyGoogleTitle - 4");
		myLogger.log(LogStatus.INFO,"Application is up & running");
		String title = driver.getTitle();
		Assert.assertTrue(title.contains("Google"));
		myLogger.log(LogStatus.PASS,"Title verified");		
	}
		
		@AfterMethod
		public void teardown(ITestResult result)
		{
			if(result.getStatus()== ITestResult.FAILURE)
			{
				//String ScreenshotPath = Utility.captureScreenshot();
				myLogger.log(LogStatus.FAIL,"Title verification");
				myReport.flush();
			}
			
		}
		
		@AfterSuite
		public void ReportSave()
		{
			myReport.endTest(myLogger);
			myReport.flush();
			driver.get("C:\\ExtentReports\\VK.html");
		}	
		
		// Use after asserts (\/)
		public void reportPass(String Desc,String testData)
		{
			myTest.log(LogStatus.PASS, Desc,testData);
		}
		
		// Use in Catch (X)
		public void reportFail(String Desc,String testData)
		{
			myTest.log(LogStatus.FAIL, Desc,testData);
		}
		
		// Use in testcase (i)
		public void reportInfo(String Desc,String testData)
		{
			myTest.log(LogStatus.INFO, Desc,testData);
		}
		
		// Use in testcase
		public void reportWarning(String Desc,String testData)
		{
			myTest.log(LogStatus.WARNING, Desc,testData);
		}
		
		
	}
	


