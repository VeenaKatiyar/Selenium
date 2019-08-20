package com.businessFunctions.Demo;

import java.util.Map;
import java.sql.Driver;

import org.openqa.selenium.By;

import com.platformLayer.driverScript;
import com.platformLayer.selActionKeywords;
import com.uilities.CodeOfKeys;
import com.uilities.Constants;
import com.uilities.commonUtil;
import com.uilities.reportUtil;

import com.platformLayer.artifactsLoader;
import java.util.Hashtable;;

public class demoAndOther {

	//private static String newCurrency;
	//private static String newAccName;
	//private static String newTransactionName;
	//Hashtable<String,String> employeeHashmap=new Hashtable<String,String>();    

	//testData = artifactsLoader.tesdataLoader();


	public static void gmail(){
		if (Constants.kyExecutionCounter.equals(true)){	
			try{
				System.out.println("URL = " + driverScript.testData.get("url"));
				selActionKeywords.wDriver.ieDriver();
				selActionKeywords.kyLaunchUrl("gmail.com");

				selActionKeywords.commonAction.kyInsertData("in_userName", "veenakatiyar");
				reportUtil.logReport("Enter User name","veenakatiyar");

				selActionKeywords.commonAction.kyClick("but_next");
				selActionKeywords.commonAction.kyInsertData("but_Password", "Jaipur@128");
				reportUtil.logReport("Enter Password","Jaipur@128");

				selActionKeywords.commonAction.kyClick("cb_staySignIn");				
				selActionKeywords.verification.kyIfObjectExist("but_signIn", true);		

				selActionKeywords.commonAction.kyClick("but_signIn");
				selActionKeywords.verification.kyIfObjectExist("but_COMPOSE", true); 
				selActionKeywords.driver.findElement(By.xpath(""));
			}catch(Exception e){
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}		
	}




	public static void sub_ManageCurrenciesTreeNavigate() {
		if (Constants.kyExecutionCounter.equals(true)){				
			try {
				selActionKeywords.commonAction.kyJsClick("lnk_Accounts");
				selActionKeywords.commonAction.kyJsClick("lnk_ManageCurrencies");
				selActionKeywords.commonAction.kyHighLight("lnk_ManageCurrencies");
				reportUtil.logReport("Navigate to ManageCurrencies");
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}
	}


	public static void sub_CreateNewCurrency() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyJsClick("btn_NewCurrencySubmit");
				newCurrency = driverScript.testData.get("CurrencyName")+commonUtil.getRandomNumber(999,9999);
				selActionKeywords.commonAction.kyInsertData("in_CurrencyName", newCurrency);
				String cS = driverScript.testData.get("CurrencySymbol");
				String cP= driverScript.testData.get("CurrencyPattern");
				String cD= driverScript.testData.get("CurrencyDesc");
				selActionKeywords.commonAction.kyInsertData("in_CurrencySymbol",cS );
				selActionKeywords.commonAction.kyInsertData("in_CurrencyPattern", cP);
				selActionKeywords.commonAction.kyInsertData("in_CurrencyDesc",cD );
				selActionKeywords.commonAction.kyHighLight("btn_CurrencySubmit");
				selActionKeywords.commonAction.kyJsClick("btn_CurrencySubmit");
				selActionKeywords.handellingAleart.kyAccept();
				reportUtil.logReport("Enter new currency","CurrencySymbol=" + cS + "<BR> "
						+ "CurrencyPattern ="+ cP+"<BR>"
						+ "CurrencyDesc "+ cD +"<BR>");
				selActionKeywords.kyWait(2);
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}

	}


	public static void sub_Cycloslogin() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyInsertData("in_LoginUserName", driverScript.testData.get("UserName"));
				selActionKeywords.commonAction.kyInsertData("in_LoginPassword", driverScript.testData.get("Password"));
				reportUtil.logReport("Enter login credentials");
				selActionKeywords.commonAction.kyHighLight("btn_LoginSubmit");
				selActionKeywords.commonAction.kyJsClick("btn_LoginSubmit");


			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}
	}


	public static void sub_CyclosLaunch() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.wDriver.chromeDriver();
				selActionKeywords.kyLaunchUrl(driverScript.testData.get("url"));
				reportUtil.logReport("Launch Cyclos");
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}
	}

	public static void sub_CyclosClose() {		
		try {
			selActionKeywords.wDriver.closeDriver();
		} catch (Exception e) {
			Constants.kyExecutionCounter = false;
			e.printStackTrace();
		}
	}




	public static void sub_CreateNewAccount() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyHighLight("lnk_ManageAccounts");
				selActionKeywords.commonAction.kyJsClick("lnk_ManageAccounts");
				reportUtil.logReport("Navigate to 'Manage Accounts'");
				//Click on Submit button to navigate to the New Acc page
				selActionKeywords.commonAction.kyJsClick("btn_NewAccSubmit");
				//Fill the Acc details
				selActionKeywords.listOperatios.kySelectByText("lst_AccType", driverScript.testData.get("AccType"));
				newAccName = driverScript.testData.get("AccName")+commonUtil.getRandomNumber(999,9999);
				String sAccDesc = driverScript.testData.get("AccDesc");
				selActionKeywords.commonAction.kyInsertData("in_Accname", newAccName);
				selActionKeywords.commonAction.kyInsertData("in_AccDesc", sAccDesc);
				selActionKeywords.listOperatios.kySelectByText("lst_AccCurrency", newCurrency);
				reportUtil.logReport("Fill the new account details'<br>",""
						+ "Account Name: "+newAccName+"<br>"
						+ "Account Description: "+sAccDesc+"<br>"
						+ "Account Currency: "+newCurrency);
				selActionKeywords.commonAction.kyHighLight("btn_CurrencySubmit");
				selActionKeywords.commonAction.kyJsClick("btn_CurrencySubmit");
				selActionKeywords.handellingAleart.kyAccept();
				reportUtil.logReport("Click on Submit button to create the Account: "+newAccName);
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}
	}




	public static void sub_InsertNewTransaction() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyJsClick("btn_NewTransSubmit");

				//Fill the Transaction Details
				newTransactionName = driverScript.testData.get("TransName")+commonUtil.getRandomNumber(999,9999);
				selActionKeywords.commonAction.kyInsertData("in_TranName", newTransactionName);
				selActionKeywords.commonAction.kyInsertData("in_TranDesc", driverScript.testData.get("TransDesc"));
				selActionKeywords.commonAction.kyInsertData("in_TranConfMsg", driverScript.testData.get("TransConfMsg"));
				selActionKeywords.commonAction.kyInsertData("in_TransMemUsrName", driverScript.testData.get("FixedDestMembLogin"));
				selActionKeywords.commonAction.kyJsClick("cb_TransEnabled");
				selActionKeywords.commonAction.kyJsClick("cb_TransPriority");
				selActionKeywords.commonAction.kyInsertData("in_MaxAmntPerDay", driverScript.testData.get("TransMaxAmnt"));
				selActionKeywords.commonAction.kyInsertData("in_MinAmntPerDay", driverScript.testData.get("TransMinAmnt"));
				selActionKeywords.listOperatios.kySelectByText("lst_ShowParent", driverScript.testData.get("ShowParent"));
				selActionKeywords.commonAction.kyJsClick("cb_TransIsConciliable");
				reportUtil.logReport("Enter Transaction details","Transaction Name: "+newTransactionName+"<br>"
						+ "Transaction Description: "+driverScript.testData.get("TransDesc")+"<br>"
						+ "Transaction Confirmation Msg: "+driverScript.testData.get("TransConfMsg")+"<br>"
						+ "Transaction Member UserName: "+driverScript.testData.get("FixedDestMembLogin")+"<br>"
						+ "Max Amount Per day: "+driverScript.testData.get("TransMaxAmnt")+"<br>"
						+ "Min Amount Per Day: "+driverScript.testData.get("TransMinAmnt")+"<br>"
						+ "Show Parent/Child: "+driverScript.testData.get("ShowParent"));
				selActionKeywords.commonAction.kyJsClick("btn_CurrencySubmit");
				selActionKeywords.handellingAleart.kyAccept();
				reportUtil.logReport("Click on Submit button after filling transaction details");

			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}

	}




	public static void sub_PerformSystemPayment() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyHighLight("lnk_SystemPayment");
				selActionKeywords.commonAction.kyJsClick("lnk_SystemPayment");

				selActionKeywords.commonAction.kyInsertData("in_SysPmntAmnt", driverScript.testData.get("SysAmntTransfer"));
				selActionKeywords.listOperatios.kySelectByText("lst_SysTransType", driverScript.testData.get("SysTransType"));
				selActionKeywords.commonAction.kyInsertData("in_SysPmntDesc", driverScript.testData.get("SysPamntDesc"));
				reportUtil.logReport("Enter System Payment details", ""
						+"System Payment Amount: "+driverScript.testData.get("SysAmntTransfer")+"<br>"
						+ "System Transaction Type: "+driverScript.testData.get("SysTransType")+"<br>"
						+ "System Payment Description: "+driverScript.testData.get("SysPamntDesc"));
				selActionKeywords.commonAction.kyJsClick("btn_SysPmntSubmit");
				selActionKeywords.commonAction.kyJsClick("btn_SysPmntSubmit");
				reportUtil.logReport("Click on Submit button after filling System Payment details");
				//				selActionKeywords.handellingAleart.kyAccept();
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}

		}

	}




	public static void sub_PerformMemberPayment() {
		if (Constants.kyExecutionCounter.equals(true)){	
			//Click on the Member Payment link
			try {
				selActionKeywords.commonAction.kyHighLight("lnk_MemberPayment");
				selActionKeywords.commonAction.kyJsClick("lnk_MemberPayment");

				selActionKeywords.commonAction.kyInsertData("in_MemLoginRecpnt", driverScript.testData.get("MemRecpntName"));
				selActionKeywords.commonAction.kyEnter("in_MemLoginRecpnt");
				selActionKeywords.commonAction.kyHighLight("in_MemPaymntAmnt");
				selActionKeywords.commonAction.kyInsertData("in_MemPaymntAmnt", driverScript.testData.get("MemAmntTransfer"));				
				selActionKeywords.listOperatios.kySelectByText("lst_MemPaymntType", driverScript.testData.get("MemPaymntType"));
				selActionKeywords.commonAction.kyInsertData("in_MemPaymntDesc", driverScript.testData.get("MemPaymntDesc"));
				reportUtil.logReport("Enter details for Member payment",""
						+"Member Recipient Name: "+driverScript.testData.get("MemRecpntName")+"<br>"
						+ "Member Amount Transfer: "+driverScript.testData.get("MemAmntTransfer")+"<br>"
						+ "Member Payment Type: "+driverScript.testData.get("MemPaymntType")+"<br>"
						+ "Member Payment Desc: "+driverScript.testData.get("MemPaymntDesc"));
				selActionKeywords.commonAction.kyJsClick("btn_SysPmntSubmit");
				selActionKeywords.commonAction.kyJsClick("btn_Submit");
				reportUtil.logReport("Click on Submit button to process the Member payment");
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}
		}

	}




	public static void sub_MemberInvoiceInitiate() {
		if (Constants.kyExecutionCounter.equals(true)){	
			try {
				selActionKeywords.commonAction.kyHighLight("lnk_MemberInvoice");
				selActionKeywords.commonAction.kyJsClick("lnk_MemberInvoice");

				//Fill the details for Member Invoice
				selActionKeywords.commonAction.kyInsertData("in_MemLoginRecpnt", driverScript.testData.get("MemInvRecpntName"));
				selActionKeywords.commonAction.kyEnter("in_MemLoginRecpnt");
				selActionKeywords.commonAction.kyHighLight("in_MemPaymntAmnt");
				selActionKeywords.commonAction.kyInsertData("in_MemPaymntAmnt", driverScript.testData.get("MemInvAmnt"));
				selActionKeywords.listOperatios.kySelectByText("lst_MemPaymntType", driverScript.testData.get("MemInvType"));
				selActionKeywords.commonAction.kyInsertData("in_MemPaymntDesc", driverScript.testData.get("MemInvDesc"));
				reportUtil.logReport("Enter the Invoice details", ""
						+"Member Recipient Name: "+driverScript.testData.get("MemInvRecpntName")+"<br>"
						+ "Member Payment Amount: "+driverScript.testData.get("MemInvAmnt")+"<br>"
						+ "Member Payment Type: "+ driverScript.testData.get("MemInvType") +"<br>"
						+ "Member Payment Desc: "+driverScript.testData.get("MemInvDesc"));
				selActionKeywords.commonAction.kyJsClick("btn_Submit");
				reportUtil.logReport("Click on Submit button to process the Invoice");
				selActionKeywords.handellingAleart.kyAccept();
				selActionKeywords.kyWait(2);
				reportUtil.logReport("Click on Notification alert");
				selActionKeywords.robotKeyPress.kyPressSingleKey(CodeOfKeys.VK_ENTER);
				selActionKeywords.kyWait(3);
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				e.printStackTrace();
			}


		}

	}




	public static void sub_CyclosLogout() {
		if (Constants.kyExecutionCounter.equals(true)){	
			selActionKeywords.commonAction.kyJsClick("lnk_Logout");
			reportUtil.logReport("Click on Logout Button");
			selActionKeywords.handellingAleart.kyAccept();
			reportUtil.logReport("Click on Logout alert to successfully log out");
			selActionKeywords.kyWait(2);
		}

	}




	public static void sub_RemoveAccount() {
		if (Constants.kyExecutionCounter.equals(true)){	
			selActionKeywords.commonAction.kyHighLight("lnk_ManageAccounts");
			selActionKeywords.commonAction.kyJsClick("lnk_ManageAccounts");
			int row = selActionKeywords.webTable.kyGetRowNumber("tbl_Acc", 1, "text", "AlphaAccount_12");
			
		}

	}








}
