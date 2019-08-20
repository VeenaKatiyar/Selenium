package com.uilities;

import org.openqa.selenium.Keys;

public class Constants {


	public static String Data = "Data";
	public static String TEST_SUITE_SHEET="Test Suite";
	public static String Test_Suite_ID="TSID";
	public static String RUNMODE_YES="Y";
	public static String TEST_CASES_SHEET="TestSet";
	public static String RUNMODE = "Runmode";
	public static String TCID="TCID";
	public static String TEST_STEPS_SHEET="Test Steps";
	public static String KEYWORD="Keyword";
	public static String KEYWORD_PASS="PASS";
	public static String KEYWORD_FAIL="FAIL";
	public static String RESULT="Result";
	public static String KEYWORD_SKIP="SKIP";
	public static String CONFIG="config";
	public static String COL="col";
	public static String dataSeparator="\\|";
	public static String OBJECT="Object";

	public static Boolean kyExecutionCounter = true;
	//public static Boolean mthdExecutionCounter = true;

	public static String reportParentPath ="";
	public static String reportSuitFolderPath ="";
	public static String reportTcFolderPath ="";
	public static String reportTestDataFolderPath ="";
	public static String reportScreenShotPath="";
	public static String screenShotUrl="";
	public static String ssRelativePath ="";//="..\\";
	public static String ssRelativePathParent="";
	public static String suitReportName="";
	public static boolean specialScreenShotFlagOn = false;
	public static String suitReportTextFileName = "";
	public static int reportStepCount = 1;

	public static String executionController = System.getProperty("user.dir")+"\\InputFiles\\Execution_Controller.xlsx";


	public static boolean ssCapture = true;
	public static int ssNameCounter = 0;
	public static boolean reportCounter = true;

	public static String suitName="";

	public static int totalPassTcCount = 0;
	public static int totalFailTcCount = 0;
	public static int testCaseCounter = 0;

	public static String nextLine = "<br>";

	public static String commonBatfilePath= System.getProperty("user.dir")+"\\otherRequiredFiles\\";
	public static String commonBatfileName= "CommonBatFile.bat";
	public static String commonBatfile = System.getProperty("user.dir")+"\\otherRequiredFiles\\CommonBatFile.bat";
	public static String fileKillIe = System.getProperty("user.dir")+"\\otherRequiredFiles\\kill-ie.bat";

	public static String launchReportSimulator ="lanchSimulator.bat"; //RoSRequestReportSimulator.exe" ;
	public static String launchReportSimulator_location =System.getProperty("user.dir")+"\\otherRequiredFiles\\RoSRequestReportSimulatorReg"; //RoSRequestReportSimulator.exe" ;

	public static String keySelectAll =Keys.chord(Keys.CONTROL, "a");
	public static String keyCopy =Keys.chord(Keys.CONTROL, "c");
	public static String testCaseStatus = "";
	public static String currentTestSuiteName = "";
	

	// ####################### Configuration Data ##################
	public static int defaultWattingTime = 10; // Wait time in second as default
	public static int maxmimumExecutionUntilPass = 3;
	// #############################################################
	
}
