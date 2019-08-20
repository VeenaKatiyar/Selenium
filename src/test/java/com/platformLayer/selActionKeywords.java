package com.platformLayer;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;
import com.uilities.Constants;
import com.uilities.fileFunctions;
import com.uilities.objPropType;
import com.uilities.reportUtil;


/** 
 * Action Keywords for Selenium 
 * @author Bikram Ranjan Sinha
 *
 */
public class selActionKeywords {


	public static WebDriver driver;
	private static final Long MIN_WAIT_TIME_IN_MS = 1000L;
	public static boolean specificCheckOnExistance= false;
	
	public static Logger logger = Logger.getLogger(selActionKeywords.class);

	public static class wDriver{
		/**
		 * Description --> Initiate Internet Explorer driver 
		 */
		public static void ieDriver() {
			if(Constants.kyExecutionCounter == true){
				try{

					System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\DriverFiles\\IEDriverServer.exe");
					DesiredCapabilities capabilities = new DesiredCapabilities();			
					capabilities.setCapability("nativeEvents",true);
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
					capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

					driver = new InternetExplorerDriver(capabilities);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(Constants.defaultWattingTime, TimeUnit.SECONDS);

				}catch(UnreachableBrowserException ex)
				{
					Constants.kyExecutionCounter = false;
					if (specificCheckOnExistance == false){
						reportUtil.logReport("Unable to read browser restarting session");
						logger.error("Unable to read browser restarting session",ex);
						kyCloseIEDriverbyForce();
					}
				}catch(Exception e){
					Constants.kyExecutionCounter = false;
					logger.error("Problem found during loading IE driver",e);
					//e.printStackTrace();
					reportUtil.reportFail("Problem found during loading IE driver","");
				}
			}
		}

		public static void firefoxDriver() {
			if(Constants.kyExecutionCounter == true){
				try{
					ProfilesIni profile = new ProfilesIni();			 
					FirefoxProfile myprofile = profile.getProfile("selenium");			 
					driver = new FirefoxDriver(myprofile);
					driver.manage().timeouts().implicitlyWait(Constants.defaultWattingTime, TimeUnit.SECONDS);
				}catch(Exception e){
					Constants.kyExecutionCounter = false;
					logger.error("Problem found during loading FireFox driver",e);
					reportUtil.reportFail("Problem found during loading FireFox driver","");
				}
			}
		}

		public static void chromeDriver() {
			if(Constants.kyExecutionCounter == true){
				try{
					System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\DriverFiles\\chromedriver.exe");
					driver = new ChromeDriver();				
					driver.manage().timeouts().implicitlyWait(Constants.defaultWattingTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				}catch(Exception e){
					Constants.kyExecutionCounter = false;
					reportUtil.reportFail("Problem found during loading Chrome driver","");
					logger.error("Problem found during loading Chrome driver",e);
					//e.printStackTrace();
				}
			}
		}

		public static void closeDriver(){
			try{	
				//reportUtil.logReport("Close Driver/Application");
				driver.quit();
				//Constants.specialScreenShotFlagOn =true;
				//driver=null;
				selActionKeywords.kyWait(5);
			}catch(Exception e){
				//e.printStackTrace();
				Constants.kyExecutionCounter = false;
				//reportUtil.reportFail("Problem found during closing driver","");
				logger.error("Problem found during closing driver",e);
				////System.out.println("Problem found during closing driver");
			}
		}

		public static void kyCloseIEDriverbyForce(){
			try{			
				fileFunctions.executeBatFile(Constants.fileKillIe);
				Thread.sleep(5000);
			}catch(Exception e){
				logger.error("Problem found during closing driver by fource",e);
				//////System.out.println("Problem found during closing driver by fource");
			}
		}

	}

	/**
	 * Description --> Verify object's existence
	 */
	public  static WebElement getElement(String objectName){
		WebElement e = null;
		String objectPropertyType ="";
		String objectProperty = "";

		if(Constants.kyExecutionCounter == true){
			try{
				objectPropertyType = driverScript.testObject.get(objectName);
				if(objectPropertyType == null || objectPropertyType.trim().equals("")){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Property for Object " + objectName + " Not Found","Please check Object Containner");
				}else{				
					objectProperty= objectPropertyType.substring((0),objectPropertyType.lastIndexOf("_"));	

					//kyWait(Constants.defaultWattingTime);)
					if(objectPropertyType.endsWith(objPropType.id))
						e= driver.findElement(By.id(objectProperty));
					else if(objectPropertyType.endsWith(objPropType.name))
						e= driver.findElement(By.name(objectProperty));
					else if(objectPropertyType.endsWith(objPropType.xpath))
						e= driver.findElement(By.xpath(objectProperty));			
					else if(objectPropertyType.endsWith(objPropType.linkText))
						e= driver.findElement(By.linkText(objectProperty));
					else if(objectPropertyType.endsWith(objPropType.className))
						e= driver.findElement(By.className(objectProperty));
					else if(objectPropertyType.endsWith(objPropType.partialLinkText))
						e= driver.findElement(By.partialLinkText(objectProperty));
					else if(objectPropertyType.endsWith(objPropType.tagName))
						e= driver.findElement(By.tagName(objectProperty));				
					else if(objectPropertyType.endsWith(objPropType.cssSelector))
						e= driver.findElement(By.cssSelector(objectProperty));
				}

			}catch(NoSuchElementException ex)
			{
				Constants.kyExecutionCounter = false;
				if (specificCheckOnExistance == false){
					reportUtil.logReport("Object " + objectName + " not reachable","PropertyValue_PropertyType = "+objectPropertyType+"<br>");
					logger.error("Object Not reachable",ex);
					wDriver.kyCloseIEDriverbyForce();
				}
			}
			catch(StaleElementReferenceException ex)
			{
				Constants.kyExecutionCounter = false;
				if (specificCheckOnExistance == false){
					reportUtil.logReport("Object " + objectName + " no more attached to DOM or not have latest details","PropertyValue_PropertyType = "+objectPropertyType+"<br>");
					logger.error("Object Not reachable",ex);
					wDriver.kyCloseIEDriverbyForce();
				}
			}
			catch(Exception ex){
				Constants.kyExecutionCounter = false;
				if (specificCheckOnExistance == false){
					reportUtil.logReport("Object " + objectName + " NOT Found","PropertyValue_PropertyType = "+objectPropertyType+"<br>");
					logger.error("Object Not Found",ex);
					//ex.printStackTrace();
				}
			}
		}
		return e;		
	}

	/**
	 * Description --> Launching url
	 */
	public static void kyLaunchUrl(String Value){
		if(Constants.kyExecutionCounter == true){
			try{
				driver.get(Value);			
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				logger.error("Url " + Value + " can NOT be launched",ex);
				reportUtil.logReport("Url " + Value + " can NOT be launched" );
			}
		}
	}

	/**
	 * Description --> Wait function
	 */
	public static void kyWait(int seconds){
		if(Constants.kyExecutionCounter == true){
			try{
				//for( int count =0; count <= seconds; count++){
				long count = MIN_WAIT_TIME_IN_MS * seconds;
				//Thread.sleep(MIN_WAIT_TIME_IN_MS);
				Thread.sleep(count);

				//}
			}catch(Exception ex){
				//ex.printStackTrace();
				logger.error("",ex);
			}
		}
	}
	
	public static class getProperty{

		/**
		 * Description --> Fetch the data/text/visible text from any objects 
		 */
		public static String kyGetText(String objName){
			WebElement e = getElement(objName);
			String str = null;
			if(Constants.kyExecutionCounter == true){
				try{
					str = e.getText();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text for the object " + objName + " can NOT be found",ex);
					reportUtil.logReport("Text for the object " + objName + " can NOT be found" );
				}
			}		
			return str;	
		}
		/**
		 * Description --> Fetch the string from the object and verify with expected string
		 */

		public static String kyGetContaint(String objName, String containt){
			WebElement e = getElement(objName);
			String str = null;
			if(Constants.kyExecutionCounter == true){
				try{
					str = e.getAttribute(containt);
					//////System.out.println(" kyGetContaint = "+str);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text for the object " + objName + " can NOT be found" ,ex);
					reportUtil.logReport("Text for the object " + objName + " can NOT be found" );
				}
			}		
			return str;	
		}


	}

	public static class objectHandelling{
		/**
		 * Description --> Add object to object container hastable testObject. Mainly required during handling dynamic objects.
		 * Object value need to be passed from Test Data
		 */
		public static void kyAddObject(String objName, String objValue, String objSelectionType){
			try{
				if(!objSelectionType.equals(objPropType.xpath)){
					objValue = driverScript.testData.get(objName);			
				}		
				objValue = objValue + objSelectionType;
				driverScript.testObject.put(objName, objValue);	
			}catch(Exception ex){
				logger.error("Object not added properly",ex);
			}
		}

		/**
		 * Add object during runtime
		 * @param objName
		 * @param objValue
		 * @param objSelectionType -> get type from objPropType.java
		 */
		public static void kyAddDynamicObject(String objName, String objValue, String objSelectionType){
			try{
				objValue = objValue + objSelectionType;
				driverScript.testObject.put(objName, objValue);	
			}catch(Exception ex){
				logger.error("Dynamic object addition failed",ex);
			}
		}

		public static void kyTurnOnVisibility(String objName){
			WebElement e = getElement(objName);
			if(Constants.kyExecutionCounter == true){
				try{				
					String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
					((JavascriptExecutor)driver).executeScript(js, e);	
					e.click();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport(" Issue in switching visibility ");
					logger.error(" ",ex);
					//ex.printStackTrace();				
				}
			}
		}

		
	}

	public static class verification{

		public static void kyVerifyText(String objName, String searchingString){		
			if(Constants.kyExecutionCounter == true){
				String parentString = getProperty.kyGetText(objName);
				//////System.out.println("parentString");
				try{
					if (!parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Text verification Fail."," Expected text =" + searchingString +Constants.nextLine+ " Found text ="+ parentString );
					}else
						reportUtil.logReport("Text verification Pass."," Expected text =" + searchingString +Constants.nextLine+ " Found text ="+ parentString  );

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text verification failed ",ex);
					reportUtil.logReport("Text verification failed ","Expected text ="+ searchingString+Constants.nextLine+" Found text="+parentString );
				}
			}		
		}

		/**
		 * Description --> Fetch the string from the object and verify with expected string
		 */
		public static void kyVerifyContaint(String objName, String containt, String searchingString){		
			if(Constants.kyExecutionCounter == true){
				String parentString = getProperty.kyGetContaint(objName, containt);
				//////System.out.println("parentString =" +parentString);
				try{
					if (!parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Content verification Fail. for object ="+objName," Expected "+containt+" =" + searchingString + Constants.nextLine+ "Found "+containt+" = "+ parentString );
					}else
						reportUtil.logReport("Content verification Pass. for object ="+objName," Expected "+containt+" =" + searchingString + Constants.nextLine+ "Found "+containt+" = "+ parentString  );

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text verification failed for text =" + parentString + " and text = "+ searchingString,ex);
					reportUtil.logReport("Text verification failed for text =" + parentString + " and text = "+ searchingString );
				}
			}		
		}

		/**
		 * verify 2 string and expecting pass only
		 * @param parentString ---> String in which other string to be searched
		 * @param searchingString --> Other string	 * 
		 */
		public static void kyVerifyString(String parentString, String searchingString){		
			if(Constants.kyExecutionCounter == true){
				try{
					kyVerifyString(parentString, searchingString, true);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text verification failed for text =" + parentString + " and text = "+ searchingString,ex);
					reportUtil.logReport("Text verification failed for text =" + parentString + " and text = "+ searchingString );
				}
			}		
		}

		/**
		 * verify 2 string depend on expectation type (True or false)
		 * @param parentString ---> String in which other string to be searched
		 * @param searchingString --> Other string
		 * @param TrueOrFalse --> expectation type True or Flase
		 */
		public static void kyVerifyString(String parentString, String searchingString, boolean TrueOrFalse){		
			if(Constants.kyExecutionCounter == true){
				try{

					if ((!parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())) & (TrueOrFalse == true)) {
						Constants.kyExecutionCounter = false;
					}else if((!parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())) & (TrueOrFalse == false)){
						Constants.kyExecutionCounter = true;
					}else if((parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())) & (TrueOrFalse == true)){
						Constants.kyExecutionCounter = true;
					}else if((parentString.trim().toUpperCase().contains(searchingString.trim().toUpperCase())) & (TrueOrFalse == false)){
						Constants.kyExecutionCounter = false;
					}					
					reportUtil.logReport("Text verification "," Expected text =" + parentString+Constants.nextLine + " Found = "+  searchingString);	

					if(TrueOrFalse == false)
						Constants.ssCapture = true;
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error("Text verification failed for text =" + parentString + " and text = "+ searchingString,ex);
					reportUtil.logReport("Text verification failed for text =" + parentString + " and text = "+ searchingString );
				}
			}		
		}

		public static void kyIsSelected(String obejectName, boolean bool){

			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					boolean flag = e.isSelected();
					if(flag!=bool)
						Constants.kyExecutionCounter = false;

					reportUtil.logReport("Verify SELECTED state of the object ="+obejectName ,"Expected ="+bool);

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Verification can not be done as Action isSelected is not valid operatio for Object ="+ obejectName );
				}
			}
		}

		public static String kyisSelected(String objectName){
			String strIsSelected = "";
			WebElement e = getElement(objectName);
			if(Constants.kyExecutionCounter == true){
				try{
					boolean flag = e.isSelected();
					if (flag== true)
						strIsSelected="true";
					else
						strIsSelected= "false";
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action isSelected is not valid operatio for Object ="+ objectName );
				}
			}
			return strIsSelected;
		}

		public static void kyisEnabled(String objectName, boolean bool){

			WebElement e = getElement(objectName);
			if(Constants.kyExecutionCounter == true){
				try{
					boolean flag = e.isEnabled();
					if(flag!=bool)
						Constants.kyExecutionCounter = false;

					reportUtil.logReport("Verify ENABLED state of the object ="+objectName ,"Expected ="+bool);

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action isEnabled is not valid operatio for Object ="+ objectName );
				}
			}
		}

		public static void kyisDisplayed(String objectName, boolean bool){

			WebElement e = getElement(objectName);
			if(Constants.kyExecutionCounter == true){
				try{
					boolean flag = e.isDisplayed();
					if(flag!=bool)
						Constants.kyExecutionCounter = false;

					reportUtil.logReport("Verify DISPALYED state of the object ="+objectName ,"Expected ="+bool);

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action isDisplayed is not valid operatio for Object ="+ objectName );
				}
			}
		}


		public static void kyIfObjectExist(String objName, Boolean bool ){		
			if(Constants.kyExecutionCounter == true){
				try{
					specificCheckOnExistance = true;
					getElement(objName);
					if(Constants.kyExecutionCounter == true & bool== false){
						Constants.kyExecutionCounter = false;
					}else if((Constants.kyExecutionCounter.equals(false)) & bool == false){
						Constants.kyExecutionCounter = true;
					}
					reportUtil.logReport("Verify exsistance of Object = "+ objName ,"Expected = "+ bool);

					if(bool == false)
						Constants.ssCapture = true;

				}catch(Exception ex){
					logger.error(" ",ex);
				}finally{
					specificCheckOnExistance = false;
				}
			}
		}


	}
	//**************All Class*****************************************


	public static class mouseAction{
		public static void kyDoubleClick(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				Actions action = new Actions(driver);
				try{
					action.doubleClick(e).perform();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Double click NOT performed for the Object "+ obejectName);
				}
			}
		}

		public static void kyMouseOver(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				Actions action = new Actions(driver);
				try{
					action.moveToElement(e).perform();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Mouse over not performed for the Object "+ obejectName );
				}
			}
		}

		public static void kyClick(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				Actions action = new Actions(driver);
				try{
					action.click(e).perform();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Mouse over not performed for the Object "+ obejectName );
				}
			}
		}
	}


	public static class handellingAleart{
		public static String kyGetText(){
			String str="";
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					str = alrt.getText();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					reportUtil.logReport("Not able to retrive text from the aleart");	
				}
			}
			return str;
		}

		public static void kyVerifyText(String expectedtext){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					String str = alrt.getText();
					if(!str.trim().toUpperCase().contains(expectedtext.trim().toUpperCase()))
						Constants.kyExecutionCounter = false;

					Constants.specialScreenShotFlagOn =true;
					reportUtil.logReport("Expected alert text = " + expectedtext +Constants.nextLine+" Found  alert  text  =  "+str );						

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					//ex.printStackTrace();
					logger.error(" ",ex);
					reportUtil.logReport("Alert text verification issue" );		
				}
			}
		}

		public static void kyVerifyTextAndAccept(String expectedtext){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					String str = alrt.getText();
					//////System.out.println("alart text =" +str);
					if(!str.trim().toUpperCase().contains(expectedtext.trim().toUpperCase()))
						Constants.kyExecutionCounter = false;
					Constants.specialScreenShotFlagOn =true;
					reportUtil.logReport("Expected alert text = " + expectedtext +Constants.nextLine+" Found  alert  text  =  "+str );	
					alrt.accept();					
				}catch(Exception ex){
					//////System.out.println("Alart isuueee -->");
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					//ex.printStackTrace();
					logger.error(" ",ex);
					reportUtil.logReport("Issue in Alert text verification and accept " );
				}
			}
		}

		public static void kyVerifyTextAndDismiss(String expectedtext){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					String str = alrt.getText();
					if(!str.trim().toUpperCase().contains(expectedtext.trim().toUpperCase()))
						Constants.kyExecutionCounter = false;
					Constants.specialScreenShotFlagOn =true;
					reportUtil.logReport("Expected alert text = " + expectedtext +Constants.nextLine+" Found  alert  text  =  "+str );	
					alrt.dismiss();					
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					reportUtil.logReport("Issue in Alert text verification and dismiss " );
				}
			}
		}

		public static void kyAccept(){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =driver.switchTo().alert();
					alrt.accept();					
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					ex.printStackTrace();
					reportUtil.logReport("Issue in accepting the Alert");	
				}
			}
		}

		public static void kyDismiss(){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					alrt.dismiss();					
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					reportUtil.logReport("Issue in dismissing the Alert");	
				}
			}
		}

		public static void kySendKey(String key){
			if(Constants.kyExecutionCounter == true){
				try{
					Alert alrt;
					alrt =selActionKeywords.driver.switchTo().alert();
					alrt.sendKeys(key);					
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					reportUtil.logReport("Issue in using <b>"+key+"</b> key to the Alert");	
				}
			}
		}
	}


	public static class commonAction{

		public static void kyClick(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.click();
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(" ",ex);
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Object "+ obejectName + "  can NOT be clicked"  );
				}
			}
		}

		public static void kyInsertData(String obejectName, String Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.clear();
					e.sendKeys(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Text = "+ Value + "can NOT be inserted into Object "+ obejectName );
				}
			}
		}		

		public static void kyInsertDataAfterText(String obejectName, String Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.sendKeys(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Text = "+ Value + "can NOT be inserted into Object "+ obejectName );
				}
			}
		}

		public static void kyClear(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.clear();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Text of the Object "+ obejectName +" can NOT be cleared");
				}
			}
		}

		public static void kyJsClick(String obejectName){		    
			WebElement element = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					JavascriptExecutor js = (JavascriptExecutor)selActionKeywords.driver;
					js.executeScript("arguments[0].click();", element);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("JsClick not working for Object "+ obejectName);
				}
			}
		}

		public static void kySendKey(String obejectName, Keys key){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.clear();
					e.sendKeys(key);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Key = "+ key + "can NOT be inserted into Object "+ obejectName );
				}
			}
		}

		/**
		 * Instead of click one can press enter on a specific object/button
		 * @param obejectName
		 */
		public static void kyEnter(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.sendKeys(Keys.ENTER);
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(" ",ex);
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error while pressing Enter on the Object "+ obejectName );
				}
			}
		}

		/**
		 * Press TAB on a specific object/button. Focus will move to the next object depend on tab sequence
		 * @param obejectName
		 */
		public static void kyTAB(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					e.sendKeys(Keys.TAB);
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(" ",ex);
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error while pressing TAB on the Object "+ obejectName );
				}
			}
		}

		/**
		 * Description -- This function need to be used for those special condition 
		 * where Webdriver got hanged after clicking. It always throws exception.
		 */
		public static void kyHangClick(String objName, int seconds){
			WebElement e = getElement(objName);
			if(Constants.kyExecutionCounter == true){
				try{
					driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);

					e.click();
				}catch(Exception ex){
					logger.info("Expected exception inside HangClick");
					//////System.out.println( " == Expected exception in hangClick   ===   ");
				}
			}
		}

		public static void kyHangJSClick(String objectName, int seconds){		    
			WebElement element = getElement(objectName);
			if(Constants.kyExecutionCounter == true){
				try{
					driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
					JavascriptExecutor js = (JavascriptExecutor)selActionKeywords.driver;
					js.executeScript("arguments[0].click();", element);
				}catch(Exception ex){
					logger.info("Expected exception inside HangJSClick");
					//				Constants.kyExecutionCounter = false;
					//				reportUtil.logReport("JsClick not working for Object "+ obejectName);
				}
			}
		}

		/**
		 * Highlight object 
		 * @param objectName
		 */
		public static void kyHighLight(String objectName)
		{
			WebElement element = getElement(objectName);
			if(Constants.kyExecutionCounter == true){
				try 
				{
					if (driver instanceof JavascriptExecutor) {
						((JavascriptExecutor)driver).executeScript("arguments[0].style.background='yellow'", element);
						((JavascriptExecutor)driver).executeScript("arguments[0].style.border='2px solid red'", element);
						Thread.sleep(500);
//						reportUtil.logReport("Highlight object ="+objectName);
						((JavascriptExecutor)driver).executeScript("arguments[0].style.background=''", element);
						((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element);
					}
				}
				catch (Exception ex) {
					logger.error(" ",ex);
					reportUtil.logReport("Highlight can not be performed for Object ="+ objectName );
				} 
			}
		}

	}


	/**
	 * Perform All Operations related to and list or Drop-down or Combobox
	 * @author sinhabik
	 *
	 */
	public static class listOperatios{

		/**
		 * Select by Visible text
		 * @param obejectName 
		 * @param Text
		 */
		public static void kySelectByText(String obejectName, String Text){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				if(Text.trim().equals("")){
					reportUtil.logReport("Select action can not be performed for object  "+ obejectName,"Data not found");
				}else{
					try{
						Select select = new Select(e);
						select.selectByVisibleText(Text);
					}catch(Exception ex){
						Constants.kyExecutionCounter = false;
						logger.error(" ",ex);
						reportUtil.logReport("Action Type SelectByText NOT working for dropdown object  "+ obejectName +" and Data ="+Text);
					}
				}
			}
		}

		/**
		 * Select by Value not visible text
		 * @param obejectName
		 * @param Value
		 */
		public static void kySelectByValue(String obejectName, String Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					Select select = new Select(e);
					select.selectByValue(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action Type SelectByValue NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 * Select by Index
		 * @param obejectName
		 * @param Value
		 */
		public static void kySelectByIndex(String obejectName, int Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					Select select = new Select(e);
					select.selectByIndex(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action Type SelectByINDEX NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 * @author Praveen Shedge
		 * Select All options.
		 * @param obejectName
		 */
		public static void kySelectAll(String obejectName){
			WebElement e = getElement(obejectName);
			Select SelectAll = new Select(e);

			if(Constants.kyExecutionCounter.equals(true)){
				try{
					List<WebElement> Listcount = SelectAll.getOptions();
					int Listsize =Listcount.size();

					for(int i=0; i<Listsize;i++)
					{
						SelectAll.selectByIndex(i);
					}                     
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("All options cannot be selected in "+ obejectName );
				}
			}
		}



		/**
		 * Deselect by Visible text
		 * @param obejectName
		 * @param Value
		 */
		public static void kyDeselectByText(String obejectName, String Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					Select select = new Select(e);
					select.deselectByVisibleText(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action Type DeselectByText NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 * Deselect by Value not by Visible text
		 * @param obejectName
		 * @param Value
		 */
		public static void kyDeselectByValue(String obejectName, String Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){				
				try{
					Select select = new Select(e);
					select.deselectByValue(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Action Type DeselectByValue NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 * Deselect All
		 * @param obejectName
		 */
		public static void kyDeselectAll(String obejectName){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){
				try{
					Select select = new Select(e);
					select.deselectAll();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action Type DeSelectAll NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 * Deselect by Index
		 * @param obejectName
		 * @param Value
		 */
		public static void kyDeselectByIndex(String obejectName, int Value){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){					
				try{
					Select select = new Select(e);
					select.deselectByIndex(Value);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Action Type DeSelectByIndex NOT working for dropdown object  "+ obejectName  );
				}
			}
		}

		/**
		 *  Verify default selected value in the dropdown
		 * @param obejectName
		 * @param value
		 */
		public static void kyVerifyDefaultSelectedValue(String obejectName, String value){			
			if(Constants.kyExecutionCounter == true){					
				try{
					String defaultValue = kyGetDefaultSelectedValue(obejectName);
					if(Constants.kyExecutionCounter == true){
						if(!defaultValue.toUpperCase().contains(value.toUpperCase()))
							Constants.kyExecutionCounter = false;
						reportUtil.logReport("Verify default value in the dropdown","Expected = "+value+Constants.nextLine+"Found = "+defaultValue);
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Verify Default Selected Value NOT working for dropdown object  "+ obejectName  );
				}
			}	
		}

		/**
		 * Return default value of the dropdown
		 * @param obejectName
		 * @return
		 */
		public static String kyGetDefaultSelectedValue(String obejectName){
			String defaultValue= null;
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){					
				try{
					Select select = new Select(e);					
					WebElement option = select.getFirstSelectedOption();
					defaultValue = option.getText();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Get Default Selected Value NOT working for dropdown object  "+ obejectName  );
				}
			}
			return defaultValue;
		}

		/* Return value of attribute the  of the dropdown's default selected value
		 * @param obejectName
		 * @attribute --> example "value"
		 * @return
		 */
		public static String kyGetDefaultSelectedAttribute(String obejectName, String attribute){
			String strAttribute= null;
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){					
				try{
					Select select = new Select(e);					
					WebElement option = select.getFirstSelectedOption();
					strAttribute = option.getAttribute(attribute);
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Get value of attribute  NOT working for dropdown object  "+ obejectName  );
				}
			}
			return strAttribute;
		}


		/**
		 * Verify if expected text is an option of the list
		 * @param obejectName
		 * @param expectedText
		 */
		public static void kyVerifyText(String obejectName, String expectedText){
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){					
				try{
					Select select = new Select(e);
					List<WebElement> allOptions = select.getOptions();
					boolean check = false;
					for( WebElement ex:allOptions){
						if(ex.getText().trim().equalsIgnoreCase(expectedText.trim())){
							check = true;
							kySelectByText(obejectName, expectedText);
							break;
						}						
						//////System.out.println(ex.getText());
					}
					if(check == false){
						Constants.kyExecutionCounter = false;						
					}
					reportUtil.logReport("Verify option <b>"+expectedText+" </b>in Drop-down object "+ obejectName );

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Option <b>"+expectedText+"</b>  NOT found in the Drop-down "+ obejectName );
				}
			}
		}

		/**
		 * Get total number of option in a dropdown
		 * @param obejectName --> Logical object name of Drop Down
		 * @return --> option count
		 */
		public static int kyGetOptionCount(String obejectName){
			int totalCount = -1;
			WebElement e = getElement(obejectName);
			if(Constants.kyExecutionCounter == true){					
				try{
					Select select = new Select(e);
					List<WebElement> allOptions = select.getOptions();
					totalCount = allOptions.size();

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Option not found in the Drop Down "+ obejectName );
				}
			}
			return totalCount;
		}


	}


	public static class frameHandelling{
		public static void kySelectFrame(String frameName){
			if(Constants.kyExecutionCounter == true){
				try{
					driver.switchTo().frame(frameName);
				}catch(NoSuchFrameException ex)
				{
					Constants.kyExecutionCounter = false;
					logger.error("frame not reachable " + ex);
					wDriver.kyCloseIEDriverbyForce();
				}
				catch(Exception ex){
					try{
						ex.printStackTrace();
						////System.out.println("frameHandelling --> kySelectFrame");
						windowHandler.kyActivateIEWindow();
						driver.switchTo().frame(frameName);
					}catch(Exception e){
						Constants.kyExecutionCounter = false;
						logger.error(" ",ex);
						reportUtil.logReport("Frame " + frameName + " can NOT be selected or NOT found ");
					}
				}
			}
		}
		public static void kySelectParentFrame(){
			if(Constants.kyExecutionCounter == true){
				try{
					driver.switchTo().parentFrame();
				}catch(NoSuchFrameException ex)
				{
					Constants.kyExecutionCounter = false;
					logger.error("frame not reachable " + ex);
					wDriver.kyCloseIEDriverbyForce();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("NO Parent Frame found ");
				}
			}
		}
		public static void kyDefaultContent(){
			if(Constants.kyExecutionCounter == true){
				try{
					driver.switchTo().defaultContent();
				}catch(NoSuchFrameException ex)
				{
					Constants.kyExecutionCounter = false;
					logger.error("frame not reachable " + ex);
					wDriver.kyCloseIEDriverbyForce();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error found during switching to default content (frame)  ");
				}
			}
		}

	}

		
	/**
	 * Handels all window related functions
	 * @author sinhabik
	 *
	 */
	public static class windowHandler{
		/**
		 * Switched to new window.
		 */
		public static String kySwitchWindow(){			
			String winHandleBefore = selActionKeywords.driver.getWindowHandle();
			//////System.out.println(" winHandleBefore = "+ winHandleBefore);
			if (Constants.kyExecutionCounter == true) {
				try {
					String Childwindow = winHandleBefore;					
					for (String Handle : selActionKeywords.driver.getWindowHandles()) {
//						System.out.println("Handle --> " + Handle);
						if (!Childwindow.equals(Handle)) {							
							Childwindow = Handle;
							Thread.sleep(2000);
							selActionKeywords.driver.switchTo().window(Childwindow);
//							System.out.println("switched to new window --> " + Handle);
						}
					}
				} catch (Exception ex) {
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("No new window Found");
				}
			}
			//////System.out.println("============================================================");
			return winHandleBefore;
		}

		/**
		 * Get how many windows are opens-get count of windows
		 */

		public static int kyGetCountOfWindows(){
			int winCounter=0;
			if(Constants.kyExecutionCounter == true){	
				try{
					Set<String> winHandleBefore = selActionKeywords.driver.getWindowHandles();
					winCounter=winHandleBefore.size();			
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();

				}				

			}
			return winCounter;

		}

		/**
		 * Switched back to previous window.
		 */
		public static void kySwitchBackToPreviousWindow(String winHandleBefore ){		

			if(Constants.kyExecutionCounter == true){			
				try{				
					selActionKeywords.driver.switchTo().window(winHandleBefore); 
					//////System.out.println("switched to new window --> " +winHandleBefore);

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					//////System.out.println("No Main window found" );
				}				

			}

		}

		/**
		 * Special function --> to switch back and activate the previous window
		 * @param winHandleBefore
		 */
		public static void kyHang_SwitchBackToPreviousWindow(String winHandleBefore ){
			if(Constants.kyExecutionCounter == true){			
				try{
					kyActivateIEWindow();
					selActionKeywords.kyWait(5);
					selActionKeywords.driver.switchTo().window(winHandleBefore);	
				}catch(Exception ex){
					logger.info("Expected exception inside Hang_SwitchBackToPreviousWindow");
					//////System.out.println("Exception As expected inside kyHang_SwitchBackToPreviousWindow");
				}				

			}		
		}

		/**
		 * Close the current window / popup
		 * @param winHandleBefore
		 */
		public static void kyCloseCurrentWindow(String winHandleBefore){			
			if (Constants.kyExecutionCounter == true) {
				try {									
					for (String Handle : selActionKeywords.driver.getWindowHandles()) {						
						if (!winHandleBefore.equals(Handle)) {
							driver.switchTo().window(Handle).close();
							break;
						}
					}
				} catch (Exception ex) {
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Not able to close current window");
				}
			}
		}

		/**
		 * Activate Internet explorer window. 
		 * @ Note -- Please make sure no other Internet explorer window is present except the window you want to activate
		 */
		public static void kyActivateIEWindow(){
			if(Constants.kyExecutionCounter == true){
				try{
					fileFunctions.executeBatFile(System.getProperty("user.dir")+"\\otherRequiredFiles\\IEActivate.vbs");
					//////System.out.println("kyActivateIEWindow -- > window activated");
				}catch(Exception ex){
					//////System.out.println(" Exception As expected ");
					logger.error(" ",ex);
					//e.printStackTrace();
				}
			}		
		}

	}


	public static class webTable{

		public static String tableXpath = null;
		public static int totalEmptyRow = -1;  //  #####  @ bikram  --> Changed totalEmptyRow == -1 from 0
		public static String chkTableObject = "";

		/**
		 * Initiate webtable for any action performed . This functiontion need to be called before any keyword
		 * @param tableObject --> Table name
		 */
		public static void initiateTable(String tableObject){

			getElement(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					String objectPropertyType = driverScript.testObject.get(tableObject);
					String objectProperty= objectPropertyType.substring((0),objectPropertyType.lastIndexOf("_"));	

					if(objectPropertyType.endsWith(objPropType.id))				
						tableXpath =".//table[@id='"+objectProperty+"']";
					else if(objectPropertyType.endsWith(objPropType.name))
						tableXpath = ".//table[@name='"+ objectProperty +"']";
					else if(objectPropertyType.endsWith(objPropType.xpath))
						tableXpath = objectProperty;
					else
						reportUtil.logReport("Property type not defind for table object "+ tableObject +" Modify code");

				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					//ex.printStackTrace();
					reportUtil.logReport("Initiation fail of the table "+ tableObject);	
				}
			}
		}

		public static void getInitialRowNo(String tableObject){
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){		
				try{
					// #####  @ bikram  --> Changed totalEmptyRow == -1 from 0
					if ( (!chkTableObject.equals(tableObject)) | (totalEmptyRow == -1)  ){
						totalEmptyRow =0;						
						int columnCount = kyGetColumnCount(tableObject);
						if(Constants.kyExecutionCounter == true){
							chkTableObject = tableObject;
							int actualColumnNumber =0;							
							//***************************************************************************************
							List<WebElement> rows = driver.findElements(By.xpath(tableXpath +"/tbody/tr"));
							int rowCount = rows.size();
							//***************************************************************************************
							for (int count = 0 ; count <= rowCount ; count++){						
								actualColumnNumber = kyGetColumnCount(tableObject, count);
								if (actualColumnNumber != columnCount){
									totalEmptyRow ++;
								}else{
									//////System.out.println("actualStartingRowNo = "+ actualStartingRowNo);
									break;
								}
							}

							//@Bikram --> Commented
							/*if (actualStartingRowNo == 0)
								actualStartingRowNo++;*/
						}
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error Occard during fetching initial Row no of the table = "+ tableObject);
				}
			}
		}

		/**
		 * Find total number of rows present in the table ( EXCLUDING empty/non visible row )
		 * @param tableObject
		 * @return  --> Row count including empty/non visible row
		 */
		public static int kyGetRowCount(String tableObject){
			int rowCount =-1;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{					
					if(Constants.kyExecutionCounter == true){						
						try{
							List<WebElement> rows = driver.findElements(By.xpath(tableXpath +"/tbody/tr"));
							rowCount = rows.size();
							//////System.out.println("rowCount = " + rowCount);
						}catch(Exception e){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Error occard during counting ROW number of the table "+ tableObject);
						}

						// @Bikram --> modified and commented
						if(Constants.kyExecutionCounter == true){	
							getInitialRowNo(tableObject);							
							rowCount= rowCount-totalEmptyRow;
						}
						//////System.out.println("rowCount --> " + rowCount);

						/*if(Constants.kyExecutionCounter == true){	
							getInitialRowNo(tableObject);
							rowCount= rowCount-(actualStartingRowNo-1);
							//////System.out.println("rowCount --> " + rowCount);
						}*/
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during counting ROW number of the table "+ tableObject);					
				}
			}
			return rowCount;
		}

		/**
		 * Find total number of rows present in the table ( INCLUDING empty/non visible row )
		 * @param tableObject
		 * @return
		 */
		public static int kyGetRowCount_generic(String tableObject){
			int rowCount = -1;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{					
					if(Constants.kyExecutionCounter == true){						
						try{
							List<WebElement> rows = driver.findElements(By.xpath(tableXpath +"/tbody/tr"));
							rowCount = rows.size();
						}catch(Exception e){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Error occard during counting ROW number of the table "+ tableObject);
						}
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during counting ROW number of the table "+ tableObject);					
				}
			}
			return rowCount;
		}

		/*
		public static int getRowCountRawdata(String tableObject){
			int rowCount =0;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					List<WebElement> rows = driver.findElements(By.xpath(tableXpath +"/tbody/tr"));
					rowCount = rows.size();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during counting ROW number of the table "+ tableObject);					
				}
			}
			return rowCount;
		}*/

		/**
		 * Find total number of column present in the table ( including empty/non visible column )
		 * @param tableObject --> Table Name
		 * @return --> Total number of columns
		 */
		public static int kyGetColumnCount(String tableObject){
			int columnCount =-1;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					List<WebElement> cols = driver.findElements(By.xpath(tableXpath +"/thead/tr/td"));
					columnCount = cols.size();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during counting Column number of the table "+ tableObject);					
				}
			}
			return columnCount;
		}

		/**
		 * Get Total column Count of the specific row.
		 * @param tableObject --> Table name
		 * @param rowNumber --> Row Number (Visible)
		 * @return  --> Total column count 
		 */
		public static int kyGetColumnCount(String tableObject, int rowNumber){
			int columnCount =-1;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{				
					// @bikram --> added --> actualStartingRowNo
					List<WebElement> cols = driver.findElements(By.xpath(tableXpath +"/tbody/tr["+(rowNumber+totalEmptyRow)+"]/td"));
					columnCount = cols.size();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during counting COLUMN number of the table "+ tableObject +" and for the row number "+ rowNumber);					
				}
			}
			return columnCount;
		}

		/**		
		 * Get the column number contains specific column text
		 * @param tableObject  --> Table object name
		 * @param columnName --> Name of the column
		 * @return --> Column number of the table .
		 */
		public static int kyGetColumnNumber(String tableObject, String columnName){
			int columnNumber =-1;
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					List<WebElement> cols = driver.findElements(By.xpath(tableXpath +"/thead/tr/td"));
					boolean found= false;
					int count=1;
					for(WebElement temp:cols){
						//////System.out.println("col -->"+temp.getText());
						if(columnName.trim().equalsIgnoreCase(temp.getText().trim())){
							columnNumber = count;
							found = true;
							break;
						}
						count++;						
					}
					if(found == false){
						Constants.kyExecutionCounter = false;
						reportUtil.logReport ("Column "+columnName+" NOT found in the table " + tableObject);						
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during finding COLUMN number of the table "+ tableObject +" for the column "+ columnName);					
				}
			}
			return columnNumber;
		}

		/**
		 * Based on column number , content and content's text/value find the row number
		 * @param tableObject
		 * @param columnNumber
		 * @param contentType ---> ex class or value or onclick or title etc.
		 * @param contentText
		 * @return --> row number which have the value/text of the specified content
		 */
		public static int kyGetRowNumber(String tableObject, int columnNumber, String contentType, String contentText){
			int rowNumber=-1;
			if(Constants.kyExecutionCounter == true){
				try{
					getInitialRowNo(tableObject);
					//int columnNumber = kyGetColumnNumber(tableObject, columnName);
					//boolean check = false;
					int totalRowCount = kyGetRowCount(tableObject);
					for( int rowCount =0; rowCount < totalRowCount; rowCount++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
						String str ="";
						if(contentType.equalsIgnoreCase("text"))
							str = kyGetCellData(tableObject, rowCount+totalEmptyRow, columnNumber);
						else
							str = getCellContaint(tableObject, rowCount+totalEmptyRow, columnNumber, contentType);
						//////System.out.println("kyGetRowNumber str= "+str+"  ==contentText="+contentText);
						if(str.contains(contentText)){
							rowNumber = rowCount+totalEmptyRow;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}				
					//				if(check==false)
					//					rowNumber=0;					
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
			return rowNumber;
		}

		/**
		 * Based on column number , content and content's text/value find the row number
		 * @param startingRowNumber  --> Enter Row number, from where searching will be started.
		 * @param tableObject
		 * @param columnNumber
		 * @param contentType
		 * @param contentText
		 * @return
		 */
		public static int kyGetRowNumber(int startingRowNumber, String tableObject, int columnNumber, String contentType, String contentText){
			int rowNumber=-1;
			if(Constants.kyExecutionCounter == true){
				try{

					getInitialRowNo(tableObject);
					//int columnNumber = kyGetColumnNumber(tableObject, columnName);
					//boolean check = false;
					int totalRowCount = kyGetRowCount(tableObject);
					for( int rowCount = startingRowNumber; rowCount < totalRowCount; rowCount++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
						String str ="";
						if(contentType.equalsIgnoreCase("text"))
							str = kyGetCellData(tableObject, rowCount+totalEmptyRow, columnNumber);
						else
							str = getCellContaint(tableObject, rowCount+totalEmptyRow, columnNumber, contentType);
						//////System.out.println("kyGetRowNumber str= "+str+"  ==contentText="+contentText);
						if(str.contains(contentText)){
							rowNumber = rowCount+totalEmptyRow;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}				
					//				if(check==false)
					//					rowNumber=0;					
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
			return rowNumber;
		}

		/**
		 * Based on column name , content and content's text/value find the row number
		 * @param tableObject
		 * @param columnName
		 * @param contentType
		 * @param contentText
		 * @return
		 */
		public static int kyGetRowNumber(String tableObject, String columnName, String contentType, String contentText){
			int rowNumber=-1;
			if(Constants.kyExecutionCounter == true){
				rowNumber = kyGetRowNumber(tableObject, kyGetColumnNumber(tableObject, columnName), contentType, contentText);
			}
			if (rowNumber==-1)
				logger.error("Row number not found");
			return rowNumber;
		}


		/**
		 * When user knows specific row and column number Irrespective of EMPTY row or column at beginning of table.
		 * @param tableObject  --> Table object name
		 * @param rowNumber --> Row number
		 * @param columnNumber --> Column Number
		 * @return --> Cell contained text
		 */		
		public static String kyGetCellData(String tableObject, int rowNumber ,  int columnNumber){
			String celltext ="";
			initiateTable(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					//@ Bikram -- Added rowNumber+1 23-Mar-2017
					celltext =  selActionKeywords.driver.findElement(By.xpath(tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]")).getText();
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Error occard during getting cell data of the table "+ tableObject + " for Row = "+rowNumber+"  for Column ="+ columnNumber);					
				}
			}
			return celltext;
		}

		/**
		 * User No NEED to BOTHER about the presence of empty row present at beginning of the table
		 * @param tableObject  --> Table object name
		 * @param rowNumber --> Visual Row number
		 * @param columnNumber --> Column Name
		 * @return --> Cell contained text
		 */	
		public static String kyGetCellData(String tableObject, int rowNumber , String columnName){
			String celltext =null;
			if(Constants.kyExecutionCounter == true){
				celltext = kyGetCellContaint(tableObject, rowNumber, columnName, "text");
			}
			if (celltext.equals(null)){
				logger.error("celltext not found");
			}
			return celltext;
		}

		/**
		 * When user knows specific row and column number Irrespective of EMPTY row or column at beginning of table.
		 * @param tableObject  --> Table object name
		 * @param rowNumber --> Row number
		 * @param columnNumber --> Column Number
		 * @return --> Value/text assigned content type title
		 */	
		public static String kyGetCellTitle(String tableObject, int rowNumber , String columnName){
			String celltext =null;
			if(Constants.kyExecutionCounter == true){
				celltext = kyGetCellContaint(tableObject, rowNumber, columnName, "title");
			}
			if (celltext.equals(null)){
				logger.error("celltext not found");
			}
			return celltext;
		}


		/**
		 * User NO NEED to BOTHER about empty row/column
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param searchingString  
		 * @return
		 */

		public static String kyVerifyCellData(String tableObject, int rowNumber , String columnName,String searchingString){
			String celltext =null;
			if(Constants.kyExecutionCounter == true){
				try{
					celltext = kyGetCellContaint(tableObject, rowNumber, columnName, "text");				
					if (!celltext.equals("")){
						if (!celltext.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Verify text of rownumber ="+(rowNumber+1)+" AND"+Constants.nextLine+ "Column name = "+columnName," Expected text =" + searchingString + Constants.nextLine +"Found = "+ celltext );
						}else{						
							reportUtil.logReport("Verify text of rownumber ="+(rowNumber+1)+" AND"+Constants.nextLine+ "Column name = "+columnName," Expected text =" + searchingString + Constants.nextLine + "Found = "+ celltext );
						}
					}else{
						if(searchingString.trim().equals(""))
							reportUtil.logReport("Verify text of rownumber ="+(rowNumber+1)+" AND"+Constants.nextLine+ "Column name = "+columnName," Expected text = BLANK" + Constants.nextLine + "Found = BLANK");
						else{
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Not able to fetch data from table of row number ="+(rowNumber+1)+"  and column Name = "+columnName );
						}
					}
				}catch(Exception ex){
					logger.error(" ",ex);
				}				
			}
			if (celltext.equals(null)){
				logger.error("celltext not found");
			}
			return celltext;
		}

		/**
		 * User NO NEED to BOTHER about empty row. Take care about columnNu Number
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param searchingString
		 * @return
		 */
		public static String kyVerifyCellData(String tableObject, int rowNumber , int columnNumber,String searchingString){
			String celltext =null;
			if(Constants.kyExecutionCounter == true){
				try{
					celltext = kyGetCellContaint(tableObject, rowNumber, columnNumber, "text");
					if (!celltext.equals("")){
						if (!celltext.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Verify text of rownumber ="+(rowNumber+1)+" AND"+Constants.nextLine+"Column number = "+columnNumber," Expected text =" + searchingString +Constants.nextLine+ "Found = "+ celltext );
						}else
							reportUtil.logReport("Verify text of rownumber ="+(rowNumber+1)+" AND"+Constants.nextLine+"Column number = "+columnNumber," Expected text =" + searchingString +Constants.nextLine+ "Found = "+ celltext );
					}else{
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Not able to fetch data from table of row number ="+(rowNumber+1)+"  and column Number = "+columnNumber );
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Exception found during veify cell data of table ="+tableObject,"Row Number ="+rowNumber+"<Br>Column Number="+columnNumber+"<Br>String to verify="+searchingString+"<Br>String found in application="+celltext);
					logger.error(" ",ex);
				}
			}
			if (celltext.equals(null)){
				logger.error("celltext not found");
			}
			return celltext;
		}

		/**
		 * User NO NEED to BOTHER about empty row/column
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param containt  --> ex.- class or title or text or onclick etc.
		 * @return
		 */
		public static String kyGetCellContaint(String tableObject, int rowNumber , String columnName , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					getInitialRowNo(tableObject);

					rowNumber = rowNumber + totalEmptyRow;					
					int columnNumber = kyGetColumnNumber(tableObject, columnName);					
					cellContaint = getCellContaint(tableObject, rowNumber, columnNumber, containt);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column name = "+columnName);					
				}
			}
			return cellContaint;
		}

		/**
		 * User NO NEED to BOTHER about empty row. Take care about columnNu Number
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param containt  --> ex.- class or title or text or onclick etc.
		 * @return
		 */
		public static String kyGetCellContaint(String tableObject, int rowNumber , int columnNumber , String containt){
			String cellContaint ="";
			initiateTable(tableObject);
			//@bikram  -->add function
			getInitialRowNo(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					//@Bikram  --> added totalEmptyRow
					cellContaint = getCellContaint(tableObject, (rowNumber + totalEmptyRow ), columnNumber, containt);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		/**
		 * User NO NEED to BOTHER about empty row. Take care about columnNu Number
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param containt  --> ex.- class or title or text or onclick etc.
		 * @return -->Returns value/text of specified content IRRESPECTIVE of empty row/column
		 */
		public static String getCellContaint(String tableObject, int rowNumber , int columnNumber , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){				
				try{
					if(Constants.kyExecutionCounter == true){						
						switch (containt){							
						case "text":
							// ###  @bikram --> tr value can not be zero , add 1
							cellContaint =  selActionKeywords.driver.findElement(By.xpath(tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]")).getText();
							break;

						case "title":
							cellContaint =  selActionKeywords.driver.findElement(By.xpath(tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]")).getAttribute("title");
							//////System.out.println(" cellContaint --> " + cellContaint);
							break;	
						default:
							try{
								cellContaint =  selActionKeywords.driver.findElement(By.xpath(tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]")).getAttribute(containt);
							}catch(Exception e){
								//////System.out.println(" Content Type  " + containt +" NOT defiend");
								////System.out.println("function name getCellContaint(String tableObject, int rowNumber , int columnNumber , String containt)");
								////System.out.println("rowNumber =" + rowNumber);
								////System.out.println("columnNumber =" + columnNumber);
								////System.out.println("------------------------------------------");

								Constants.kyExecutionCounter = false; // @bikram --> added 10-Apr
								reportUtil.logReport(" Attribute NOT Found","Attribute = "+containt);
							}
							break;							
						}
						//////System.out.println("rowNumber = "+rowNumber+"  --> getCellContaint cellContaint = "+ cellContaint);
					}					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		/**
		 * User NO NEED to BOTHER about empty row/column
		 * @param tableObject
		 * @param rowNumber
		 * @param columnName
		 * @param containt --> ex.- class or title or text or onclick etc.
		 * @param searchingString -->  string that needs to be tested
		 * @return
		 */
		public static String verifyCellContaint(String tableObject, int rowNumber , String columnName , String containt, String searchingString){
			String cellContaint =null;
			if(Constants.kyExecutionCounter == true){
				String cellContent = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
				try{
					if (!cellContent.equals("")){
						if (!cellContent.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Verify "+containt+" of rownumber ="+(rowNumber+1)+" and column name = "+columnName," Expected text =" + searchingString + " and Found = "+ cellContent );
						}else
							reportUtil.logReport("Verify "+containt+" of rownumber ="+(rowNumber+1)+" and column name = "+columnName," Expected text =" + searchingString + " and Found = "+ cellContent );
					}else{
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Not able to fetch "+ containt  +" from table of row number ="+(rowNumber+1)+"  and column name = "+columnName );
					}				
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during verifying "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column name = "+columnName);					
				}
			}			
			return cellContaint;
		}




		/**
		 * 
		 * @param tableObject
		 * @param rowNumber
		 * @param columnNumber
		 * @param containt
		 * @param searchingString
		 * @return
		 */
		public static String verifyCellContaint(String tableObject, int rowNumber , int columnNumber , String containt, String searchingString){
			String cellContaint =null;
			if(Constants.kyExecutionCounter == true){
				String cellContent = kyGetCellContaint(tableObject, rowNumber, columnNumber, cellContaint);
				try{
					if (!cellContent.equals("")){
						if (!cellContent.trim().toUpperCase().contains(searchingString.trim().toUpperCase())){
							Constants.kyExecutionCounter = false;
							reportUtil.logReport("Verify "+containt+" of rownumber ="+(rowNumber+1)+" and column number = "+columnNumber," Expected text =" + searchingString + " and Found = "+ cellContent );
						}else
							reportUtil.logReport("Verify "+containt+" of rownumber ="+(rowNumber+1)+" and column number = "+columnNumber," Expected text =" + searchingString + " and Found = "+ cellContent );
					}else{
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Not able to fetch "+ containt  +" from table of row number ="+(rowNumber+1)+"  and column number = "+columnNumber );
					}				
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during verifying "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}			
			return cellContaint;
		}


		/**
		 * Verify if table is empty or not.
		 * @param tableObject --> Table Name
		 * @param YesNo --> YES for expected empty Table , NO for table expected NOT empty table
		 *//*
		public static void kyTableEmpty_1(String tableObject, String YesNo){
			int row = kyGetRowCount(tableObject);
			if(Constants.kyExecutionCounter == true){
				YesNo = YesNo.trim();
				if(row<=0 & YesNo.equalsIgnoreCase("YES"))
					Constants.kyExecutionCounter = true;
				else if(row<=0 & YesNo.equalsIgnoreCase("NO"))
					Constants.kyExecutionCounter = false;
				else if(row >0 & YesNo.equalsIgnoreCase("YES"))
					Constants.kyExecutionCounter = false;
				else if(row >0 & YesNo.equalsIgnoreCase("NO"))
					Constants.kyExecutionCounter = true;
				else{
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Verify parameter YesNo for the keyword \"kyTableEmpty\"");
				}				
			}				
		}*/

		/**
		 * Verifu if table is empty or not empty
		 * @param tableObject
		 * @param true_false
		 */
		public static void kyTableEmpty(String tableObject, boolean bool){
			int row = kyGetRowCount(tableObject);
			if(Constants.kyExecutionCounter == true){
				try{
					if(row<=0 & bool ==false)
						Constants.kyExecutionCounter = false;
					else if(row >0 & bool ==true)
						Constants.kyExecutionCounter = false;

					reportUtil.logReport("Verify the table ="+tableObject+" is empty", "Expected = "+ bool);
					if(bool == false)
						Constants.ssCapture = true;
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}				
		}

		public static void kyVerifyContaintExistanceInColumn(String tableObject, String columnName, String containt, String text, boolean bool){			
			if(Constants.kyExecutionCounter == true){
				try{
					boolean check = false;
					getInitialRowNo(tableObject);
					int columnNumber = kyGetColumnNumber(tableObject, columnName);

					for(int rowNumber =0; rowNumber < kyGetRowCount(tableObject); rowNumber++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
						String str = getCellContaint(tableObject, rowNumber+ totalEmptyRow , columnNumber, containt);
						//////System.out.println("kyVerifyContaintExistanceInColumn --> content="+str);
						if(str.contains(text)){
							check = true;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}

					if(Constants.kyExecutionCounter == true){
						if (check == true & bool ==true)
							Constants.kyExecutionCounter = true;
						else if (check == true & bool ==false)
							Constants.kyExecutionCounter = false;
						else if (check == false & bool ==true)
							Constants.kyExecutionCounter = false;
						else if (check == false & bool==false)
							Constants.kyExecutionCounter = true;

						reportUtil.logReport("Verify "+containt + " into table ", "Column Name ="+ columnName+ Constants.nextLine+ containt +" = "+ text +Constants.nextLine+ "Expected = "+ bool);
					}
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
		}

	}


	/**
	 * Class handles Special types of table having non conventional structures 
	 * @author sinhabik
	 *
	 */
	public static class customeWebTable{
		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/div/u
		 */
		public static String kyGetCellContaint_Type_divU(String tableObject, int rowNumber , String columnName , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;					
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);				
					// #### @ bikram --> TR can not be 0 , added 1
					cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div[1]/u")).getAttribute(containt);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber)+"  & column name = "+columnName);					
				}
			}
			return cellContaint;
		}

		/**
		 * User NO need to bother empty row, Take care of column number
		 * For those table structure where contains lies under structure --> table/body/tr/td/div/u
		 */
		public static String kyGetCellContaint_Type_divU(String tableObject, int rowNumber , int columnNumber , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					//////System.out.println(" rowNumber= "+rowNumber+ " <-->  webTable.actualStartingRowNo ="+ webTable.totalEmptyRow);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					//////System.out.println("rowNumber + actualStartingRowNo -1 ="+rowNumber);					
					if(containt.equalsIgnoreCase("text")){
						// #### @ bikram --> TR can not be 0 , added 1
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div[1]/u")).getText();
						//////System.out.println("cellContaint = "+ cellContaint);
					}
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div[1]/u")).getAttribute(containt);					
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(" ",ex);
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber-1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		//--------------------------------------------------------------------------------------------------------		

		/**
		 * User NO need to bother empty row/column
		 * For those table structure where contains lies under structure --> table/body/tr/td/div/img
		 */
		public static String kyGetCellContaint_Type_divImg(String tableObject, int rowNumber , String columnName , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;

					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);	
					// #### @ bikram --> TR can not be 0 , added 1
					cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div[1]/img")).getAttribute(containt);					
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(" ",ex);
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column name = "+columnName);					
				}
			}
			return cellContaint;
		}
		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/div/img
		 */
		public static String kyGetCellContaint_Type_divImg(String tableObject, int rowNumber , int columnNumber , String containt){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;					
					//int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);					
					cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div[1]/img")).getAttribute(containt);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ containt +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		//--------------------------------------------------------------------------------------------------------		

		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/div
		 * @param tableObject --> Table Object
		 * @param rowNumber --> Row number
		 * @param columnNumber --> Column Number
		 * @param content --> Content Type ( like -> class, value , title etc.)
		 * @return
		 */
		public static String kyGetCellContaint_Type_div(String tableObject, int rowNumber , int columnNumber , String content){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					//////System.out.println(" rowNumber --> kyGetCellContaint_Type_div = " + rowNumber);
					cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getAttribute(content);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ content +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/div
		 * @param tableObject --> Table Object
		 * @param rowNumber --> Row number
		 * @param columnName --> Column Name
		 * @param content --> Content Type ( like -> class, value , title etc.)
		 * @return --> Content value as String
		 */
		public static String kyGetCellContaint_Type_div(String tableObject, int rowNumber , String  columnName , String content){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);
					if(content.equalsIgnoreCase("text"))
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getText();
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getAttribute(content);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ content +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column name = "+columnName);					
				}
			}
			return cellContaint;
		}

		public static String kyVerifyCellData_div(String tableObject, int rowNumber , String  columnName , String content, String text, String expected_Yes_No){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);
					if(content.equalsIgnoreCase("text"))
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getText();
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getAttribute(content);

					if(expected_Yes_No.equalsIgnoreCase("yes")){
						if(!cellContaint.equals(text))
							Constants.kyExecutionCounter = false;
					}else if(expected_Yes_No.equalsIgnoreCase("no")){
						if(cellContaint.equals(text))
							Constants.kyExecutionCounter = false;
					}
					reportUtil.logReport("Verification of text="+text+" inside containt="+content,"RowNumber="+rowNumber+" ColumnName="+columnName);					

				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Errror occard during verification of text="+text+" inside containt="+content,"RowNumber="+rowNumber+" ColumnName="+columnName);					
				}
			}
			return cellContaint;
		}

		public static String kyVerifyCellData_div(String tableObject, int rowNumber , int  columnNumber , String content, String text, String expected_Yes_No){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					if(content.equalsIgnoreCase("text"))
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getText();
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/div")).getAttribute(content);

					if(expected_Yes_No.equalsIgnoreCase("yes")){
						if(!cellContaint.equals(text))
							Constants.kyExecutionCounter = false;
					}else if(expected_Yes_No.equalsIgnoreCase("no")){
						if(cellContaint.equals(text))
							Constants.kyExecutionCounter = false;
					}
					reportUtil.logReport("Verification of text="+text+" inside containt="+content,"RowNumber="+rowNumber+" ColumnNumber="+columnNumber);					

				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Errror occard during verification of text="+text+" inside containt="+content,"RowNumber="+rowNumber+" ColumnNumber="+columnNumber);					
				}
			}
			return cellContaint;
		}

		//--------------------------------------------------------------------------------------------------------

		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/span
		 * @param tableObject --> Table Object
		 * @param rowNumber --> Row number
		 * @param columnNumber --> Column Number
		 * @param content --> Content Type ( like -> class, value , title etc.)
		 * @return
		 */
		public static String kyGetCellContaint_Type_Span(String tableObject, int rowNumber , int columnNumber , String content){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					//////System.out.println(" rowNumber --> kyGetCellContaint_Type_div = " + rowNumber);
					if(content.equalsIgnoreCase("text"))
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/span")).getText();
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/span")).getAttribute(content);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ content +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column number = "+columnNumber);					
				}
			}
			return cellContaint;
		}

		/**
		 * For those table structure where contains lies under structure --> table/body/tr/td/span
		 * @param tableObject --> Table Object
		 * @param rowNumber --> Row number
		 * @param columnName --> Column Name
		 * @param content --> Content Type ( like -> class, value , title etc.)
		 * @return --> Content value as String
		 */
		public static String kyGetCellContaint_Type_Span(String tableObject, int rowNumber , String  columnName , String content){
			String cellContaint ="";
			if(Constants.kyExecutionCounter == true){
				try{
					webTable.getInitialRowNo(tableObject);	
					rowNumber = rowNumber + webTable.totalEmptyRow;
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);
					if(content.equalsIgnoreCase("text"))
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/span")).getText();
					else
						cellContaint =   driver.findElement(By.xpath(webTable.tableXpath+"/tbody/tr["+(rowNumber+1)+"]/td["+columnNumber+"]/span")).getAttribute(content);					
				}catch(Exception ex){
					logger.error(" ",ex);
					//ex.printStackTrace();
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Error occard during fetching "+ content +" of cell data from the table "+ tableObject +" for Row = "+(rowNumber+1)+"  & column name = "+columnName);					
				}
			}
			return cellContaint;
		}

		//--------------------------------------------------------------------------------------------------------
		/**
		 *  Get row count 
		 * @param GetCellContaint_Type --> divU or divImg or div or span
		 * @param tableObject --> Table Object
		 * @param columnNumber -->  Column number
		 * @param contentType --> Content Type ( like -> class, value , title etc.)
		 * @param contentText --> Value  of the content
		 * @return --> Row number as Integer
		 */
		public static int kyGetRowNumber(String GetCellContaint_Type , String tableObject, int columnNumber, String contentType, String contentText){
			int rowNumber= -1;
			if(Constants.kyExecutionCounter == true){
				try{

					webTable.getInitialRowNo(tableObject);

					int TotalRowCount = webTable.kyGetRowCount(tableObject);

					//////System.out.println("rowCount = "+rowCount);
					//int tempRowNumber = webTable.totalEmptyRow ;
					//////System.out.println("tempRowNumber = "+tempRowNumber);
					// ###  @Bikram added "=" at rowNumber <= rowCount as when row count is only 1 function was NOT working 


					for( int tempRowNumber = 0; tempRowNumber < TotalRowCount; tempRowNumber++){
						String str ="";
						if (GetCellContaint_Type.equalsIgnoreCase("divU") )
							str = kyGetCellContaint_Type_divU(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("divImg") )
							str = kyGetCellContaint_Type_divImg(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("div"))
							str = kyGetCellContaint_Type_div(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("span"))
							str = kyGetCellContaint_Type_Span(tableObject, tempRowNumber, columnNumber, contentType);

						//////System.out.println("str ="+str+"and contentText ="+contentText +" rowNumber = "+rowNumber);
						if(str.contains(contentText)){
							rowNumber = tempRowNumber + webTable.totalEmptyRow ; //added webTable.totalEmptyRow to get actual row count 
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}				

					/*				}else
					reportUtil.logReport("Cell containt type = "+GetCellContaint_Type+ " NOT defiend");*/
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
			return rowNumber;
		}

		/**
		 *  Get row count 
		 * @param GetCellContaint_Type --> divU or divImg or div or span
		 * @param tableObject
		 * @param columnName
		 * @param contentType
		 * @param contentText
		 * @return
		 */
		public static int kyGetRowNumber(String GetCellContaint_Type , String tableObject, String columnName, String contentType, String contentText){
			int rowNumber= -1;
			if(Constants.kyExecutionCounter == true){

				rowNumber = kyGetRowNumber(GetCellContaint_Type, tableObject, webTable.kyGetColumnNumber(tableObject, columnName), contentType, contentText);
				/*				}else
					reportUtil.logReport("Cell containt type = "+GetCellContaint_Type+ " NOT defiend");*/
			}
			if(rowNumber==-1)
				logger.error(" Row Number == -1");
			return rowNumber;
		}

		/**
		 *  Get row count . 
		 * @param startinRow --> Enter Row number, from where searching will be started.
		 * @param GetCellContaint_Type --> divU or divImg or div or span
		 * @param tableObject --> Table Object
		 * @param columnNumber -->  Column number
		 * @param contentType --> Content Type ( like -> class, value , title etc.)
		 * @param contentText --> Value  of the content
		 * @return --> Row number as Integer
		 */
		public static int kyGetRowNumber(int startinRow, String GetCellContaint_Type , String tableObject, int columnNumber, String contentType, String contentText){
			int rowNumber= -1;
			if(Constants.kyExecutionCounter == true){
				try{


					webTable.getInitialRowNo(tableObject);

					int TotalRowCount = webTable.kyGetRowCount(tableObject);

					//////System.out.println("rowCount = "+rowCount);
					//int tempRowNumber = webTable.totalEmptyRow ;
					//////System.out.println("tempRowNumber = "+tempRowNumber);
					// ###  @Bikram added "=" at rowNumber <= rowCount as when row count is only 1 function was NOT working 


					for( int tempRowNumber = startinRow; tempRowNumber < TotalRowCount; tempRowNumber++){
						String str ="";
						if (GetCellContaint_Type.equalsIgnoreCase("divU") )
							str = kyGetCellContaint_Type_divU(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("divImg") )
							str = kyGetCellContaint_Type_divImg(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("div"))
							str = kyGetCellContaint_Type_div(tableObject, tempRowNumber, columnNumber, contentType);
						else if (GetCellContaint_Type.equalsIgnoreCase("span"))
							str = kyGetCellContaint_Type_Span(tableObject, tempRowNumber, columnNumber, contentType);

						//////System.out.println("str ="+str+"and contentText ="+contentText +" rowNumber = "+rowNumber);
						if(str.contains(contentText)){
							rowNumber = tempRowNumber + webTable.totalEmptyRow ; //added webTable.totalEmptyRow to get actual row count 
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}				

					/*				}else
					reportUtil.logReport("Cell containt type = "+GetCellContaint_Type+ " NOT defiend");*/
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
			if(rowNumber==-1)
				logger.error(" Row Number == -1");
			return rowNumber;
		}

		/**
		 *  Get row count 
		 * @param startinRow --> Enter Row number, from where searching will be started.
		 * @param GetCellContaint_Type --> divU or divImg or div or span
		 * @param tableObject
		 * @param columnName
		 * @param contentType
		 * @param contentText
		 * @return
		 */
		public static int kyGetRowNumber(int startinRow , String GetCellContaint_Type , String tableObject, String columnName, String contentType, String contentText){
			int rowNumber= -1;
			if(Constants.kyExecutionCounter == true){

				rowNumber = kyGetRowNumber(startinRow, GetCellContaint_Type, tableObject, webTable.kyGetColumnNumber(tableObject, columnName), contentType, contentText);
				/*				}else
					reportUtil.logReport("Cell containt type = "+GetCellContaint_Type+ " NOT defiend");*/
			}
			if(rowNumber==-1)
				logger.error(" Row Number == -1");
			return rowNumber;
		}


		//--------------------------------------------------------------------------------------------------------
		public static void kyVerifyContaintExistanceInColumn_divImg(String tableObject, String columnName, String containt, String text, String expected_Yes_No){			
			if(Constants.kyExecutionCounter == true){
				try{
					boolean check = false;
					webTable.getInitialRowNo(tableObject);
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);

					for(int rowNumber =0; rowNumber < webTable.kyGetRowCount(tableObject); rowNumber++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);

						//@ bikram changed --> removed webTable.actualStartingRowNo
						//String str = kyGetCellContaint_Type_divImg(tableObject, rowNumber+webTable.actualStartingRowNo, columnNumber, containt);

						String str = kyGetCellContaint_Type_divImg(tableObject, rowNumber, columnNumber, containt);
						if(str.equals(text)){
							check = true;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}

					if(Constants.kyExecutionCounter == true){
						if (check == true & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = true;
						else if (check == true & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = true;

						reportUtil.logReport("Verify "+containt + " into table ", "Column Name "+ columnName+ Constants.nextLine+ containt +" = "+ text +Constants.nextLine+ "Expected = "+ expected_Yes_No);
					}
				}catch(Exception ex){					
					logger.error(" ",ex);
				}
			}
		}

		public static void kyVerifyContaintExistanceInColumn_divImg(String tableObject, int columnNumber, String containt, String text, String expected_Yes_No){			
			if(Constants.kyExecutionCounter == true){
				try{
					boolean check = false;
					webTable.getInitialRowNo(tableObject);
					//int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);

					for(int rowNumber =0; rowNumber < webTable.kyGetRowCount(tableObject); rowNumber++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
						String str = kyGetCellContaint_Type_divImg(tableObject, rowNumber, columnNumber, containt);
						if(str.equals(text)){
							check = true;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}

					if(Constants.kyExecutionCounter == true){
						if (check == true & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = true;
						else if (check == true & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = true;

						reportUtil.logReport("Verify "+containt + " into table ", "Column Number "+ columnNumber+ Constants.nextLine+ containt +" = "+ text +Constants.nextLine+ "Expected = "+ expected_Yes_No);
					}
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
		}

		//--------------------------------------------------------------------------------------------------------

		public static void kyVerifyContaintExistanceInColumn_div(String tableObject, String columnName, String containt, String text, String expected_Yes_No){			
			if(Constants.kyExecutionCounter == true){
				try{
					boolean check = false;
					webTable.getInitialRowNo(tableObject);
					int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);
					//////System.out.println("webTable.actualStartingRowNo; --> " + webTable.actualStartingRowNo);
					int maxRow = webTable.kyGetRowCount(tableObject);
					for(int rowNumber =0; rowNumber < maxRow ; rowNumber++){					
						//////System.out.println("     tempNumber = " + rowNumber);
						String str = kyGetCellContaint_Type_div(tableObject, rowNumber, columnNumber, containt);
						if(str.equals(text)){
							check = true;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}

					if(Constants.kyExecutionCounter == true){
						if (check == true & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = true;
						else if (check == true & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = true;

						reportUtil.logReport("Verify "+containt + " into table ", "Column Name "+ columnName+ Constants.nextLine+ containt +" = "+ text +Constants.nextLine+ "Expected = "+ expected_Yes_No);
					}
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
		}

		public static void kyVerifyContaintExistanceInColumn_div(String tableObject, int columnNumber, String containt, String text, String expected_Yes_No){			
			if(Constants.kyExecutionCounter == true){
				try{
					boolean check = false;
					webTable.getInitialRowNo(tableObject);
					//int columnNumber = webTable.kyGetColumnNumber(tableObject, columnName);

					for(int rowNumber =0; rowNumber < webTable.kyGetRowCount(tableObject); rowNumber++){
						//String str = kyGetCellContaint(tableObject, rowNumber, columnName, containt);
						String str = kyGetCellContaint_Type_div(tableObject, rowNumber, columnNumber, containt);
						if(str.equals(text)){
							check = true;
							break;
						}
						if (Constants.kyExecutionCounter.equals(false))
							Constants.kyExecutionCounter = true;
					}

					if(Constants.kyExecutionCounter == true){
						if (check == true & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = true;
						else if (check == true & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("yes"))
							Constants.kyExecutionCounter = false;
						else if (check == false & expected_Yes_No.equalsIgnoreCase("no"))
							Constants.kyExecutionCounter = true;

						reportUtil.logReport("Verify "+containt + " into table ", "Column Number "+ columnNumber+ Constants.nextLine+ containt +" = "+ text +Constants.nextLine+ "Expected = "+ expected_Yes_No);
					}
				}catch(Exception ex){
					logger.error(" ",ex);
				}
			}
		}


	}


	public static class robotKeyPress{

		/**
		 * Select key value from CodeOfKeys.java
		 * @param keyValue
		 */
		public static void kyPressSingleKey(int keyValue){
			if(Constants.kyExecutionCounter == true){
				try{			
					Robot r = new Robot();
					r.keyPress(keyValue); 					
					r.keyRelease(keyValue);					
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Key ="+ keyValue+" Can not be enetered");
					logger.error(" ",ex);
					//ex.printStackTrace();
				}
			}
		}


		/**
		 * Select key value from CodeOfKeys.java
		 * @param FirstKeyValue --> will be value of "Ctrl" for key sequence "Ctrl+C"(Copy)
		 * @param SecondKeyValue --> will be value of "C" for key sequence "Ctrl+C" (Copy)
		 */
		public static void kyPressDoubleKey(int FirstKeyValue, int SecondKeyValue ){
			if(Constants.kyExecutionCounter == true){
				try{			
					Robot robot = new Robot();
					robot.keyPress(FirstKeyValue);
					robot.keyPress(SecondKeyValue);
					robot.keyRelease(SecondKeyValue);
					robot.keyRelease(FirstKeyValue);				
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					logger.error(" ",ex);
					reportUtil.logReport("Key ="+ FirstKeyValue+ "+"+ SecondKeyValue +" Can not be enetered");
					//ex.printStackTrace();
				}
			}
		}


		/**
		 * Select key value from CodeOfKeys.java
		 * @param FirstKeyValue --> will be value of "Alt" for key sequence "Alt+Ctrl+Del"
		 * @param SecondKeyValue --> will be value of "Ctrl" for key sequence "Alt+Ctrl+Del"
		 * @param ThiredkeyValue --> will be value of "Del" for key sequence "Alt+Ctrl+Del"
		 */
		public static void kyPressThipleKey(int FirstKeyValue, int SecondKeyValue, int ThiredkeyValue ){
			if(Constants.kyExecutionCounter == true){
				try{			
					Robot robot = new Robot();
					robot.keyPress(FirstKeyValue);
					robot.keyPress(SecondKeyValue);
					robot.keyRelease(SecondKeyValue);
					robot.keyRelease(FirstKeyValue);				
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Key ="+ FirstKeyValue+ "+"+ SecondKeyValue +" Can not be enetered");
					//ex.printStackTrace();
					logger.error(" ",ex);
				}
			}
		}

		/**
		 * Enter sequence of text, like "Hello World"
		 * @param text
		 */
		public static void kyEnterText(String text){

			//String text = "Hello World";
			kyWait(2);
			StringSelection stringSelection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, stringSelection);
			try{
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				logger.error(" ",ex);
				reportUtil.logReport("Text ="+text+" Can not be enetered");
			}
		}

		/**
		 * Enter sequence of text, like "Hello World"
		 * @param text
		 */
		public static void kyVerifyTextFromClipboard(String text){
			kyWait(2);
			try{
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				if(!(clipboard.getData(DataFlavor.stringFlavor).toString()).contains(text))
					Constants.kyExecutionCounter = false;

				Constants.specialScreenShotFlagOn =true;
				reportUtil.logReport("Verify text="+text);

			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				logger.error(" ",ex);
				reportUtil.logReport("Text ="+text+" Can not be enetered");
			}
		}

	}
}


