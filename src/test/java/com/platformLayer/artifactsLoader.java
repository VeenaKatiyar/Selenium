package com.platformLayer;

import java.util.Hashtable;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.uilities.Constants;
import com.uilities.objPropType;


public class artifactsLoader {

	public static Hashtable<String, String> objectLoader(String fileName, String objectName){
		Fillo fillo=new Fillo();

		Hashtable<String,String> hashTable=new Hashtable<String,String>();  
		Connection connection=null;
		Recordset recordset=null;
		String strQuery =null;


		if (Constants.kyExecutionCounter.equals(true)) {
			try {

				fileName = System.getProperty("user.dir") + "\\InputFiles\\OR\\" + fileName;

				if (objectName.trim().equals(""))
					strQuery = "Select Object_Name,Property,Value from ObjectContainer";

				else
					strQuery = "Select Object_Name,Property,Value from ObjectContainer where Object_Name='" + objectName
					+ "'";

				connection = fillo.getConnection(fileName);
				recordset = connection.executeQuery(strQuery);

				String temp = "";

				while (recordset.next()) {

					if (!(recordset.getField("Object_Name").equals(""))) {
						if (recordset.getField("Property").trim().equals("id")) {
							temp = recordset.getField("Value") + objPropType.id;
						} else if (recordset.getField("Property").trim().equals("name")) {
							temp = recordset.getField("Value") + objPropType.name;
						} else if (recordset.getField("Property").trim().equals("xpath")) {
							temp = recordset.getField("Value") + objPropType.xpath;
						} else if (recordset.getField("Property").trim().equals("linkText")) {
							temp = recordset.getField("Value") + objPropType.linkText;
						} else if (recordset.getField("Property").trim().equals("className")) {
							temp = recordset.getField("Value") + objPropType.className;
						} else if (recordset.getField("Property").trim().equals("partialLinkText")) {
							temp = recordset.getField("Value") + objPropType.partialLinkText;
						} else if (recordset.getField("Property").trim().equals("tagName")) {
							temp = recordset.getField("Value") + objPropType.tagName;
						} else if (recordset.getField("Property").trim().equals("cssSelector")) {
							temp = recordset.getField("Value") + objPropType.cssSelector;
						}
					}

					if (!temp.trim().equals("")) {
						if (objectName.trim().equals("")) {
							hashTable.put(recordset.getField("Object_Name"), temp);
							//System.out.println(recordset.getField("Object_Name")+"-->"+temp);
							temp = "";
						} else {
							hashTable.put(objectName, temp);
							break;
						}
					} else
						break;
				}

				recordset.close();
				connection.close();
			} catch (Exception ex) {
				Constants.kyExecutionCounter = false;
				//reportUtil.logReport("Object loading fail ");
				System.out.println("Object loading fail");
				recordset.close();
				connection.close();
			}
		}
		return hashTable;
	}

	public static String valueSetFinder(String fileName, String currentTestCaseID, String testSet){

		String value = "";
		if(Constants.kyExecutionCounter.equals(true)){

			fileName = System.getProperty("user.dir")+"\\InputFiles\\TestData\\"+ fileName;
			String strQuery="Select "+testSet+" from TestData where Test_Case_ID='"+currentTestCaseID+"' and Variable='Runmode' ";					
			value = getValue(fileName, strQuery, testSet);
		}	
		return value;
	}

	public static String getValue(String fileName , String strQuery, String fieldName){
		String strValue ="";
		Connection connection=null;
		Recordset recordset=null;

		if(Constants.kyExecutionCounter.equals(true)){
			Fillo fillo=new Fillo();

			try{
				connection=fillo.getConnection(fileName);
				recordset= connection.executeQuery(strQuery);
				while(recordset.next()){
					strValue = recordset.getField(fieldName);
					break;
				}
				recordset.close();
				connection.close();	
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				recordset.close();
				connection.close();		
				//reportUtil.logReport("Error found in function getValue" +fileName );
				System.out.println("Error found in function getValue");

			}
		}
		return strValue;				
	}

	public static Hashtable<String, String> testDataLoader(String fileName, String testCaseID,String setValue){

		Fillo fillo=new Fillo();

		Hashtable<String,String> hashTable=new Hashtable<String,String>();  
		Connection connection=null;
		Recordset recordset=null;
		String strQuery = null;

		if(Constants.kyExecutionCounter.equals(true)){
			fileName = System.getProperty("user.dir")+"\\InputFiles\\TestData\\"+ fileName;
			String dataRepoPath= fileName.substring(0, fileName.lastIndexOf("\\"));

			if (setValue.trim().equals(""))
				setValue= "Value_Set-1";
			try{
				for( int count = 1; count <=2; count++){
					if(Constants.kyExecutionCounter.equals(false)){
						//reportUtil.logReport(" Test case specific data loding fail");
						System.out.println("Test case specific data loding fail");
						break;
					}

					if (count == 1)
						strQuery="Select Variable,"+setValue+" from TestData where Test_Case_ID='COMMON'";			
					else
						strQuery="Select Variable,"+setValue+" from TestData where Test_Case_ID='"+testCaseID+"'";

					connection=fillo.getConnection(fileName);							
					recordset= connection.executeQuery(strQuery);

					while(recordset.next()){
						String strVal = recordset.getField(setValue);
						//System.out.println("strVal #### --> "+strVal);
						if (strVal.contains("key_")){
							String MDfileName = dataRepoPath+ "\\DataRepository.xlsx";
							String strQuery1 = "Select Value from MasterTestData where KeyWord='"+strVal+"'";
							strVal = getValue(MDfileName, strQuery1, "Value");
							//System.out.println("          strVal  data repository @@@@@@@ --> "+strVal);
						}

						hashTable.put(recordset.getField("Variable"), strVal );			
					}
				}
			}catch(Exception ex){
				Constants.kyExecutionCounter=false;
				System.out.println("Test case specific data loding fail");
				//reportUtil.logReport(" Test case specific data loding fail");
				ex.printStackTrace();			
			}finally{
				recordset.close();
				connection.close();
			}			
		}		
		return hashTable;
	}

}
