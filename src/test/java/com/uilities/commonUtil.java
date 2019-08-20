package com.uilities;

import java.util.Random;

public class commonUtil {

	/*public static void main(String[] args) {

		System.out.println(commonUtil.uniqueNumber());
		System.out.println(commonUtil.uniqueNumber(8));
		System.out.println(commonUtil.uniqueNumber("9"));

	}*/

	public static String uniqueNumber(String length){		
		String str =  dateUtil.dateNow("dd.MM.yyyy hh.mm.ss");
		str=replaceUtil.replaceAllSpecialChar(str);

		if(!length.trim().equals(""))
			str = str.substring(0, Integer.valueOf(length));

		return str;
	}

	public static String uniqueNumber(int length){		
		if (length > 14 ){
			System.out.println("Length should be between 0 to 14");
		}
		String str = commonUtil.uniqueNumber(String.valueOf(length));
		return str;
	}

	public static long uniqueNumber(){
		long number = Long.parseLong(uniqueNumber(""));
		return number;
	}

	/**
	 * This function returns random number between specific range
	 * e.g. Random (-5,20)
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumber(int min, int max) {
		int randomNumber =0;
		if (min >= max) {
			Constants.kyExecutionCounter=false;
			reportUtil.logReport("Max number="+max +"must be greater than Min="+min);
		}

		Random ran = new Random();
		int temp=min-1;
		while(temp < min){
			temp= ran.nextInt((max - min) + 1) + min;
			randomNumber = temp;			
		}
		return randomNumber;
	}

	/**
	 * This function returns positive random number including zero
	 * @param max_Number
	 * @return
	 */
	public static int getRandomNumber(int max_Number) {
		return getRandomNumber(0,max_Number);
	}

}
