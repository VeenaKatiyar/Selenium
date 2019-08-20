package com.uilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class dateUtil {

	/**
	 *  Convert current date in expected format
	 * @param dateFormat --> provide format like yyyy-mm-dd or yyyy/MM or MM.dd.yyyy etc.
	 * @return Return todays date in expected format
	 */
	public static String dateNow(String dateFormat){
		String str="";
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			str = sdf.format(cal.getTime());
		}catch(Exception ex){
			Constants.kyExecutionCounter = false;
			System.out.println("wrong date format expected");
			//return str;
		}	    
		return str;
	}

	/**
	 * 
	 * @param dateFormat --> example --> "yyyy-MM-dd HH:mm:ss.SSS" z or "dd.MM.yyyy HH:mm" or "HH:mm:ss"
	 * @param date 
	 * @param NumberOfDays 
	 * @return
	 */
	public static String dateForward(String dateFormat, String date, int NumberOfDays ){
		String newDate="";
		try{

			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(date));
			c.add(Calendar.DATE, NumberOfDays);  // number of days to add
			newDate = sdf.format(c.getTime()); 
		}catch(Exception e){
			Constants.kyExecutionCounter = false;
			System.out.println();
		}
		return newDate;
	}

	/**
	 * 
	 * @param dateFormat -->--> example --> "yyyy-MM-dd HH:mm:ss.SSS" z or "dd.MM.yyyy HH:mm" or "HH:mm:ss"
	 * @param date
	 * @param days
	 * @return
	 */
	public static String dateBackword(String dateFormat, String date, int NumberOfDays ){
		String newDate="";
		try{

			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(date));
			c.add(Calendar.DATE, -NumberOfDays);  // number of days to add
			newDate = sdf.format(c.getTime()); 
		}catch(Exception e){
			Constants.kyExecutionCounter = false;
			System.out.println();
		}
		return newDate;
	}

	/**
	 * 
	 * @return Current Date
	 */
	public static String dateNow(){		
		Calendar now = Calendar.getInstance();
		int var = now.get(Calendar.DATE);		
		return  String.valueOf(var);
	}
	/**
	 * 
	 * @return Current Month
	 */
	public static String monthNow(){
		Calendar now = Calendar.getInstance();
		int var = now.get(Calendar.MONTH) + 1;
		return String.valueOf(var);
	}
	/**
	 * 
	 * @return Current Year
	 */
	public static String yearNow(){
		Calendar now = Calendar.getInstance();
		int var = now.get(Calendar.YEAR);
		return String.valueOf(var);
	}

	/**
	 * Rerurns date and/or time depend on Time Zone.
	 * Example --> "yyyy-MM-dd HH:mm:ss.SSS z" for  
	 * @param timeZone --> "Europe/Stockholm" , Europe/Oslo , etc . refer --> https://garygregory.wordpress.com/2013/06/18/what-are-the-java-timezone-ids/
	 * @param format --> example --> "yyyy-MM-dd HH:mm:ss.SSS" z or "dd.MM.yyyy HH:mm" or "HH:mm:ss"
	 * @return
	 */
	public static String dateORtimeNow_TimeZone(String timeZone, String format){
		String str="";
		Date toPrint = new Date();
		SimpleDateFormat strformat = new SimpleDateFormat(format);
		strformat.setTimeZone(TimeZone.getTimeZone(timeZone));
		str = strformat.format(toPrint); 
		return str;
	}

}


