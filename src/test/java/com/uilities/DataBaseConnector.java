package com.uilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.WebElement;

import com.platformLayer.selActionKeywords;

public class DataBaseConnector {
	public static ResultSet rs;
	public static Connection connection;

	public static class sqlDB{

		/**
		 *  Connect to SQL Database
		 * @param dbusername
		 * @param dbpassword
		 * @param DBURL
		 */
		public static void connectDB(String dbusername,String dbpassword,String DBURL){
			if(Constants.kyExecutionCounter.equals(true)){
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				}catch(ClassNotFoundException e) {
					e.printStackTrace();
				}

				try {	
					connection = DriverManager.getConnection(DBURL,dbusername,dbpassword);
					if(connection!=null) {
						//System.out.println("Connected to the sql database...");
						//reportUtil.logReport("Connected to the database..." );
					}else {
						reportUtil.logReport("SQL Database connection failed. " );
						System.out.println("SQL Database connection failed ...");
					}

				}catch(SQLException sqlEx) {
					Constants.kyExecutionCounter =false;
					reportUtil.logReport("SQL Database connection failed" );
					System.out.println("SQL Exception:" +sqlEx.getStackTrace() );					          
				}
			}
		}

		/**
		 * Get resultset from SQL DB
		 * @param dbusername
		 * @param dbpassword
		 * @param DBURL
		 * @param sqlQuery
		 * @return --> ResultSet 
		 */
		public static ResultSet kyGetResuletSet(String dbusername,String dbpassword,String DBURL,String sqlQuery)
		{
			connectDB(dbusername, dbpassword, DBURL);
			if(Constants.kyExecutionCounter.equals(true)){
				try{
					/*
	        	try {
			            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			        }catch(ClassNotFoundException e) {
			            e.printStackTrace();
			        }

				 try {	
		            connection = DriverManager.getConnection(DBURL,dbusername,dbpassword);
		            if(connection!=null) {
		               System.out.println("Connected to the database...");
		            	//reportUtil.logReport("Connected to the database..." );
		            }else {
		            	reportUtil.logReport("Database connection failed to Environment" );
		                System.out.println("Database connection failed to Environment");
		            }

					 */

					Statement stmt = connection.createStatement();
					rs=stmt.executeQuery(sqlQuery);

					//stmt.close();

				}catch(SQLException sqlEx) {
					Constants.kyExecutionCounter =false;
					reportUtil.logReport("SQL Exception:" +sqlEx.getStackTrace() );					          
				}
			}
			return rs;
		}

		/**
		 * Close Sql database
		 */
		public static void closeDb(){
			try{
				rs.close();
				connection.close();
			}catch(Exception e){

			}
		}

		/**
		 * Description --> Fetch the data/text/visible text from any objects 
		 */
		public static String kyGetTextFromDB(String dbusername,String dbpassword,String DBURL,String sqlQuery){		
			ResultSet rs;
			rs=kyGetResuletSet(dbusername, dbpassword, DBURL, sqlQuery);		
			String strVal = null;
			if(Constants.kyExecutionCounter.equals(true)){
				try{
					while(rs.next()){
						strVal = rs.getString(1).toString();
						break;
					}				
				} catch (SQLException e) {
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("No Records obtained for this specific query in SQL DB"  );
				}finally{
					closeDb();
				}
			}		
			return strVal;	
		}

		/**
		 * Description --> Fetch the string from the object and verify with expected string
		 */
		public static void kyObjectVerifyTextFromDB(String objName,String dbusername,String dbpassword,String DBURL,String sqlQuery){	
			String varValueFromDB=kyGetTextFromDB(dbusername,dbpassword,DBURL,sqlQuery);			
			WebElement e = selActionKeywords.getElement(objName);			
			if(Constants.kyExecutionCounter.equals(true)){
				try{
					String varExpectedVal=e.getText();
					if(varExpectedVal.trim().equalsIgnoreCase(varValueFromDB)){
						reportUtil.logReport("Value  got successfully verified from SQL DataBase" + varExpectedVal);
					}else{
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Value  NOT successfully verified from SQL DataBase" + varExpectedVal);
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Verification gets failed with SQL database" );
				}
			}		

		}

		/**
		 * Verify text/entry in db depend on Query string
		 * @param text --> Text/entry to be verified
		 * @param dbusername
		 * @param dbpassword
		 * @param DBURL
		 * @param sqlQuery
		 */
		public static void kyVerifyTextFromDB(String text,String dbusername,String dbpassword,String DBURL,String sqlQuery){	
			String varValueFromDB=kyGetTextFromDB(dbusername,dbpassword,DBURL,sqlQuery);			
			//System.out.println("varValueFromDB = "+varValueFromDB);
			if(Constants.kyExecutionCounter.equals(true)){
				try{				
					if(text.trim().equalsIgnoreCase(varValueFromDB.trim())){
						reportUtil.logReport("Value "+text+" got successfully verified from SQL DataBase");
					}else{
						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Value "+text+" verification with SQL DataBase NOT successfull");
					}
				}catch(Exception ex){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Verification gets failed with SQL database" );
				}
			}		

		}

		/**
		 * Get an integer value from data base based on QUery string
		 * @param dbusername
		 * @param dbpassword
		 * @param DBURL
		 * @param sqlQuery
		 * @return 
		 */
		public static int kyGetInteger(String dbusername,String dbpassword,String DBURL,String sqlQuery){
			int strRs= 0;
			ResultSet rs;
			rs=kyGetResuletSet(dbusername, dbpassword, DBURL, sqlQuery);
			if(Constants.kyExecutionCounter.equals(true)){
				try{
					if(!rs.equals(null)){					
						while(rs.next()){
							String str =  rs.getString(1).toString();
							strRs  = Integer.valueOf(str);
							//System.out.println(str);						
						}
					}
				} catch (SQLException e) {
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("No Records obtained for this specific SQL query"  );
				}finally{
					closeDb();
				}
			}		
			return strRs;	
		}


		/**
		 * Update DatabaseTable in SQLDB
		 * @param dbusername
		 * @param dbpassword
		 * @param DBURL
		 * @param sqlQuery
		 * @return --> rowCount updated 
		 */
		public static int kyUpdateDBTable(String dbusername,String dbpassword,String DBURL,String sqlQuery)
		{
			int rowCount=0;
			Statement stmt= null;
			try{
				if(Constants.kyExecutionCounter.equals(true)){
					connectDB(dbusername, dbpassword, DBURL);	
					stmt = connection.createStatement();
					rowCount = stmt.executeUpdate(sqlQuery);
					stmt.close();
				}
			} catch(SQLException sqlEx) {
				sqlEx.printStackTrace();
				Constants.kyExecutionCounter =false;
				reportUtil.logReport("SQL Exception:" +sqlEx.getStackTrace() );					          
			}finally{
				closeDb();
			}			
			return rowCount;
		}
	}

	public static class mAccessDB{

	}

	public static class sybaseDB{

	}
}





