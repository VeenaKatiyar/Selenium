package com.uilities;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.platformLayer.selActionKeywords;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import static  org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
//import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class reportUtil {

	public static void reportDefaultFolder(){
		String str = replaceUtil.replaceAllSpecialCharWithUnderscore(dateUtil.dateNow("dd.MM.yyyy hh.mm.ss"));
		Constants.ssRelativePath = "..\\"+str;
		Constants.ssRelativePathParent ="";

		str = System.getProperty("user.dir")+"\\ResultReporting\\"+str;		
		try{
			new File(str).mkdirs();
			Constants.reportParentPath =str;
		}catch(Exception ex){
			System.out.println("Parent folder for reporting not get created");
			Constants.reportParentPath ="";
		}
	}

	public static ExtentReports resultReport;
	public static void suitReport(String suitName){		
		if (!Constants.reportParentPath.equals("")){
			Constants.suitReportName = Constants.reportParentPath+"\\Suit-Report_"+suitName+".html" ;
			Constants.suitReportTextFileName= Constants.reportParentPath +"\\"+suitName;
			fileFunctions.createfile(Constants.suitReportTextFileName,".txt");
			resultReport=new ExtentReports(Constants.suitReportName);	
			resultReport.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));
			resultReport.addSystemInfo("Environment Version", "7.4.0");
		}
	}

	public static void createSuitFolder(String suitName){		

		String strTFP = Constants.reportParentPath+"\\Suit_"+suitName;

		//Constants.ssRelativePathParent = Constants.ssRelativePath; 
		Constants.suitName = suitName;
		try{
			new File(strTFP).mkdirs();
			Constants.reportSuitFolderPath = strTFP;
			if(Constants.ssRelativePath.contains("\\Suit_")){
				Constants.ssRelativePath = Constants.ssRelativePath.substring(0, Constants.ssRelativePath.indexOf("\\Suit_"));
				//ssRelativePath = Constants.ssRelativePathParent+"\\Test-Case_"+testCaseID;
			}
			Constants.ssRelativePath = Constants.ssRelativePath + "\\Suit_"+ suitName;
		}catch(Exception ex){
			System.out.println("Parent folder for reporting not get created");
			Constants.reportSuitFolderPath = "";
		}
	}

	public static void tcFolder(String testCaseID){
		String testCF = Constants.reportSuitFolderPath+"\\Test-Case_"+testCaseID ;
		try{
			new File(testCF).mkdirs();
			Constants.reportTcFolderPath = testCF;
			if(Constants.ssRelativePath.contains("\\Test-Case_")){
				Constants.ssRelativePath = Constants.ssRelativePath.substring(0, Constants.ssRelativePath.indexOf("\\Test-Case_"));
				//ssRelativePath = Constants.ssRelativePathParent+"\\Test-Case_"+testCaseID;
			}
			Constants.ssRelativePath = Constants.ssRelativePath+"\\Test-Case_"+testCaseID;			
		}catch(Exception ex){
			System.out.println("Test Case folder for reporting not get created");
			Constants.reportTcFolderPath = "";
		}		
	}

	public static void testDatFolder(String testSetColumnName){
		String testDCN = Constants.reportTcFolderPath+"\\"+testSetColumnName ;
		try{
			new File(testDCN).mkdirs();
			Constants.reportTestDataFolderPath = testDCN;
			if(Constants.ssRelativePath.contains("\\Test-Data_Set")){
				Constants.ssRelativePath = Constants.ssRelativePath.substring(0, Constants.ssRelativePath.indexOf("\\Test-Data_Set"));
				//Constants.ssRelativePath = Constants.ssRelativePath + testSetColumnName;
			}
			Constants.ssRelativePath = Constants.ssRelativePath + "\\"+ testSetColumnName;
		}catch(Exception ex){
			System.out.println("Test Data folder for reporting not get created");
			Constants.reportTestDataFolderPath = "";
		}		
	}

	public static void screenShotFolder(){
		String testSSF = Constants.reportTestDataFolderPath+"\\ScreenShots" ;
		try{
			new File(testSSF).mkdirs();
			Constants.reportScreenShotPath = testSSF;
			Constants.ssRelativePath = Constants.ssRelativePath + "\\ScreenShots\\";

			//System.out.println("Constants.ssRelativePath  --->>>> " + Constants.ssRelativePath);
		}catch(Exception ex){
			System.out.println("test Data folder for reporting not get created");
			Constants.reportScreenShotPath = "";
		}		
	}




	public static ExtentTest parent;
	public static void startReporting(String testCaseName, String testCaseSummery ){
		parent=resultReport.startTest(testCaseName,testCaseSummery);		
	}	

	public static ExtentTest report;
	public static void startChildReporting(String testSetColumnName, String testCaseSummery ){
		report=resultReport.startTest(testSetColumnName,testCaseSummery);		
	}	

	public static void printReporting(){
		try{
			if(!report.equals(null)){
				parent.appendChild(report);
				resultReport.endTest(report);
				report = null;
			}
			resultReport.endTest(parent);			
			resultReport.flush();
		}catch(Exception ex){
			//System.out.println( " =================  Exception in function printReporting  ======================   ");
			//ex.printStackTrace();
			resultReport.endTest(parent);
			resultReport.flush();
		}

	}

	public static void endReporting(){
		resultReport.close();		
	}	

	public static void reportPass(String Description, String testData){		
		report.log(LogStatus.PASS, Description,  testData);		
	}

	public static void reportFail(String Description, String testData){		
		report.log(LogStatus.FAIL, Description, testData);		
	}

	public static void reportInfo(String Description, String testData){		
		report.log(LogStatus.INFO, Description, testData);		
	}

	public static void reportWarning(String Description,String testData){
		report.log(LogStatus.WARNING, Description, testData);
	}

	public static void reportSkip(String Description,String testData){
		parent.log(LogStatus.SKIP, Description, testData);
	}	


	public static void logReport(String Description,String testData ){
		// //  ########  @Bikram --> made changes so that evn after fail ,if user wants to continue , it could capture screenshots
		if (Constants.reportCounter == true){  

			if(Constants.specialScreenShotFlagOn == true){
				specialScreenShot();
				Constants.specialScreenShotFlagOn = false;
			}else
				captureScreenShot();			
			int counter = Constants.ssNameCounter-1;
			String htmlTag = "<br><a href='"+ Constants.ssRelativePath + counter +".png"+"'><span class='label info'>Screen Shot</span></a>";
			
			String stepCount = "<b><u><mark>Step "+Constants.reportStepCount+" : </mark> </u></b>";
			Description = stepCount + Description;
		
			//String escaped1 = escapeHtml4(Description);
			//String escaped2 = escapeHtml4(testData);
			
			
			if (Constants.kyExecutionCounter.equals(false)){
				Constants.reportCounter =  false;
				reportUtil.reportFail(Description, testData +"  "+ htmlTag);
				
				//Constants.ssCapture = false;  //#######  @ Bikram  --> Explained above
			}else
				reportUtil.reportPass(Description, testData +"  "+ htmlTag);
			//}
			Constants.reportStepCount++;
		}
	}
	public static void logReport(String Description ){
		reportUtil.logReport(Description, "");
	}

	public static void captureScreenShot(){
		/*try{			
			File scrFile = ((TakesScreenshot)selActionKeywords.driver).getScreenshotAs(OutputType.FILE);

			Constants.screenShotUrl = Constants.reportScreenShotPath+"\\"+Constants.ssNameCounter+".png";//Constants.ssRelativePath+Constants.ssNameCounter+".png";

			FileUtils.copyFile(scrFile, new File(Constants.screenShotUrl));
			Constants.ssNameCounter = Constants.ssNameCounter +1;			
		}catch(Exception ex){
			System.out.println("Error while talking screen shot");
			ex.printStackTrace();
		}*/
		specialScreenShot();
		
	}

	public static void specialScreenShot(){
		try{
			BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			Constants.screenShotUrl = Constants.reportScreenShotPath+"\\"+Constants.ssNameCounter+".png";	        
			ImageIO.write(image, "png", new File(Constants.screenShotUrl));

			Constants.ssNameCounter = Constants.ssNameCounter +1;	
		}catch(Exception e){
			System.out.println("Error while talking screen shot inside function specialScreenShot ");
		}
	}


}


