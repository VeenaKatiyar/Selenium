package com.platformLayer;


import com.applicationLayer.demoTcController;

import com.uilities.Constants;
import com.uilities.ExecutionLogger;
import com.uilities.SendMail;
import com.uilities.Xls_Reader;
import com.uilities.fileFunctions;
import com.uilities.reportUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class driverScript {
	public static Xls_Reader suiteXLS;
	public static int currentSuiteID;
	public static String currentTestSuite;

	// current test suite
	public static Xls_Reader currentTestSuiteXLS, currentTestDataFile;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestDataSetID;

	public static String executeTestcase; 
	public static int varTotalTc;

	public static String testCaseFileName;
	public static String testDataFileName;
	public static String testCaseSummery;
	public static String objectContainerFileName;
	public static String testCaseID;

	/*public static Hashtable<String, String> testData; // Contains all test data
	public static Hashtable<String, String> testObject; // Contains all obeject's property
	 */	
	public static Map<String, String> testData;
	public static Map<String, String> testObject;

	public static boolean testDataFound,testMode;
	
	public static Logger logger = Logger.getLogger(driverScript.class);

	@Test
	public static void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		//driverScript.closeDriver();
		reportUtil.reportDefaultFolder();

		suiteXLS = new Xls_Reader(Constants.executionController);

		executeTestcase ="";
		varTotalTc = 0;

		for(currentSuiteID=2;currentSuiteID<=suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET);currentSuiteID++){
			// test suite name = test suite xls file having test cases
			currentTestSuite=suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID);
			if(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID).equals(Constants.RUNMODE_YES)){
				// execute the test cases in the suite
				Constants.currentTestSuiteName = currentTestSuite;
				currentTestSuiteXLS=new Xls_Reader(System.getProperty("user.dir")+"\\InputFiles\\"+currentTestSuite+".xlsx");

				reportUtil.suitReport(currentTestSuite);
				ExecutionLogger.initiateLogger(currentTestSuite);
				
				Dimension screnSize = Toolkit.getDefaultToolkit().getScreenSize();
				logger.info("Screen Width ="+ screnSize.getWidth() + " Hight = "+ screnSize.getHeight());
				
				// iterate through all the test cases in the suite

				for(currentTestCaseID=2;currentTestCaseID<=currentTestSuiteXLS.getRowCount(Constants.TEST_CASES_SHEET);currentTestCaseID++){				

					currentTestCaseName=currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestCaseID);

					if(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID).equals(Constants.RUNMODE_YES)){						
						reportUtil.createSuitFolder(currentTestSuite);

						
						objectContainerFileName = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Object Container File Name", currentTestCaseID);
						testDataFileName = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Test Data File Name", currentTestCaseID);
						testCaseSummery = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Test Case Summery", currentTestCaseID);
						testCaseID = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "TCID", currentTestCaseID);
						
						reportUtil.tcFolder(testCaseID);
						reportUtil.startReporting(testCaseID, testCaseSummery);

						//############################################################################################################
						// Loading object from ObjectContainer

				
							testObject = artifactsLoader.objectLoader(objectContainerFileName,"");
				
							
						//
						//############################################################################################################
						// loading test data
						testDataFound = false;
						testMode = true;
						String testSetColumnName ="";
						try{
							Xls_Reader currentTestDataFile = new Xls_Reader(System.getProperty("user.dir")+"\\InputFiles\\TestData\\"+testDataFileName);

							for( int count =3; count<= currentTestDataFile.getColumnCount("TestData") ;count++){						

								testSetColumnName = "Test-Data_Set-"+ (count-2);

								//System.out.print(" -------------------  testSetColumnName   " + testSetColumnName);							

								String testDataSetValue= artifactsLoader.valueSetFinder(testDataFileName, currentTestCaseName, testSetColumnName);
								//System.out.print( "    and  runmode " + testSetValue);
								if ((testDataSetValue.toUpperCase().equals("Y")) || (testDataSetValue.toUpperCase().equals("YES"))){
									testDataFound = true;
									//test data folder creation
									reportUtil.testDatFolder(testSetColumnName);;
									// screen shot folder creation
									reportUtil.screenShotFolder();

									testData = artifactsLoader.testDataLoader(testDataFileName, currentTestCaseName,testSetColumnName);																		
									
									lodeTestCase("", testCaseID,testSetColumnName);
									Constants.reportTestDataFolderPath = "";

								}else if (testDataSetValue.trim().equals(""))								
									break;
							}

							if (testDataFound == false){
								testSetColumnName = "Test-Data_Set-1";
								//test data folder creation
								reportUtil.testDatFolder(testSetColumnName);
								// screen shot folder creation
								reportUtil.screenShotFolder();

								testData = artifactsLoader.testDataLoader(testDataFileName, currentTestCaseName,"Test-Data_Set-1");								
								
								lodeTestCase("", testCaseID,testSetColumnName);
								Constants.reportTestDataFolderPath = "";
							}
						}catch(Exception ex){
							logger.error(ex);
							reportUtil.reportFail("Test Data NOT Loded properly","");
						}

						varTotalTc=varTotalTc+1;
						Constants.reportTcFolderPath = "";
						testObject.clear();
					}else{
						reportUtil.startReporting(currentTestCaseName, " NOT in Scope of Execution " );
						reportUtil.reportSkip("Test Skipped","");
						reportUtil.report = null;
						reportUtil.printReporting();			
					}					
				}
				try{
					reportUtil.endReporting();
				}catch(Exception ex){
					logger.error(ex);
				}
			}		
		}
		driverScript.CloseApplication();
	}			

	public static void lodeTestCase(String testCaseFileName, String testCaseID, String testSetColumnName){
		try{
			reportUtil.startChildReporting(testCaseID+"_"+testSetColumnName, "");

			if (testCaseFileName.trim().equals("")){				
				if(testCaseID.startsWith("Demo")){
					demoTcController.driver(testCaseID);
				}
			}
			reportUtil.printReporting();
			resetCountrs();
//			CloseApplication();
			selActionKeywords.kyWait(5);
		}catch(Exception e){
			logger.error(e);
//			CloseApplication();
		}
		/*if(Constants.kyExecutionCounter.equals(true))
			driverScript.closeDriver();
		else
			driverScript.launchReport();*/
	}

	/*private static void launchReport() {

			selActionKeywords.kyLaunchUrl(Constants.suitReportName);

	}*/

	public static void CloseApplication(){

		selActionKeywords.wDriver.kyCloseIEDriverbyForce();
		selActionKeywords.kyWait(10);
		/*		try{			 
			selActionKeywords.kyCloseIEDriverbyForce();
			selActionKeywords.kyWait(10);
		}catch(Exception ex){
			fileFunctions.executeBatFile(Constants.fileKillIe);			
			boolean bolCheck = Constants.kyExecutionCounter;
			if(Constants.kyExecutionCounter.equals(false))
				Constants.kyExecutionCounter = true;
			selActionKeywords.kyWait(10);			
			Constants.kyExecutionCounter = bolCheck;			
		}*/

	}

	public static void resetCountrs(){
		Xls_Reader.exportDataToExcel(Constants.reportTestDataFolderPath+"\\TestData.xlsx");
		Constants.kyExecutionCounter = true;

		Constants.ssCapture = true;
		Constants.ssNameCounter = 0;
		testData.clear();
		selActionKeywords.webTable.chkTableObject = "";
		selActionKeywords.webTable.totalEmptyRow = -1;
		Constants.reportStepCount =1;
//		Order.activityId.clear();
//		DeptList_Inbox.activityId.clear();
	}

	/*@AfterTest
	public static void reportSummery(){
		
		//String messageText = "Total Passed TC =  45 \nTotal Failed   TC = 5\n";
		//System.out.println(messageText);
		String messageText = " Total Pass TC =  "+Constants.totalPassTcCount+" ===== Total  Failed TC = "+ Constants.totalFailTcCount;
		fileFunctions.appendTextInTextFile(Constants.suitReportTextFileName+".txt", messageText);
		SendMail.mailling(messageText);
		
	}*/
}
