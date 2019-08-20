package com.applicationLayer;


import java.lang.reflect.Method;

import com.businessFunctions.Demo.demoAndOther;
import com.platformLayer.driverScript;
import com.platformLayer.selActionKeywords;
import com.uilities.Constants;

//bikram
public class demoTcController {
	//############################################################
	// do NOT Change\edit  the function driver
	/**
	 * Method to invoke test cases based on execution controller TCID
	 */	
	public static void driver(String testCaseID){
		try{

			System.out.println("################################ "+ testCaseID +" ################################");
			Method method = demoTcController.class.getMethod(testCaseID, String.class);
			method.invoke(method, "");
		}catch(Exception ex){
			System.out.println("Test Case lodding issue at demoTcController ----> " +ex + "  for test case "+ testCaseID);			
		}

		if(Constants.kyExecutionCounter.equals(true))
			System.out.println( "Tc Id -->  "+testCaseID+" -->  Passed");
		else
			System.out.println( "Tc Id -->  "+testCaseID+" -->  FAILED");
		System.out.println("################################ "+ testCaseID +" ################################");
	}
	
	
	
	// do NOT Change\edit  the function driver
	// ############################################################

	

	public static void Demo_gmail(String str) {	
		demoAndOther.gmail();
	}
	

	public static void Demo_Cyclos_Accounts(String str) {	
		
		demoAndOther.sub_CyclosLaunch();
		demoAndOther.sub_Cycloslogin();
//		demoAndOther.sub_ManageCurrenciesTreeNavigate();
//		demoAndOther.sub_CreateNewCurrency();
//		demoAndOther.sub_CreateNewAccount();
//		demoAndOther.sub_InsertNewTransaction();
//		demoAndOther.sub_PerformSystemPayment();
//		demoAndOther.sub_PerformMemberPayment();
//		demoAndOther.sub_MemberInvoiceInitiate();
		demoAndOther.sub_RemoveAccount();
		demoAndOther.sub_CyclosLogout();
		demoAndOther.sub_CyclosClose();
		
	}
	

}
