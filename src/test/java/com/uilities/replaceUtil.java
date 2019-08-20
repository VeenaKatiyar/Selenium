package com.uilities;

public class replaceUtil {

	public static String replaceBlank(String str){		
		if (str.contains(" "))
			str = str.replaceAll(" ", "_");
		return str;		
	}

	public static String replaceDot(String str){	
		if (str.contains("."))
			str = str.replaceAll("\\.", "_");
		return str;		
	}

	public static String replaceAnd(String str){		
		if (str.contains("&"))
			str = str.replaceAll("&", "_");
		return str;		
	}

	public static String replaceFrontSlace(String str){		
		if (str.contains("/"))
			str = str.replaceAll("/", "_");
		return str;		
	}

	public static String replaceBackSlash(String str){		
		if (str.contains("\\"))
			str = str.replaceAll("\\", "_");
		return str;		
	}	

	public static String replaceHash(String str){		
		if (str.contains("#"))
			str = str.replaceAll("#", "_");
		return str;		
	}

	public static String replaceAT(String str){		
		if (str.contains("@"))
			str = str.replaceAll("@", "_");
		return str;		
	}

	public static String replaceComma(String str){		
		if (str.contains(","))
			str = str.replaceAll(",", "_");
		return str;		
	}

	public static String replaceSemicolon(String str){		
		if (str.contains(";"))
			str = str.replaceAll(";", "_");
		return str;		
	}

	public static String replaceColon(String str){		
		if (str.contains(":"))
			str = str.replaceAll(":", "_");
		return str;		
	}

	public static String replaceDash(String str){		
		if (str.contains("-"))
			str = str.replaceAll("-", "_");
		return str;		
	}	

	public static String replaceAllSpecialCharWithUnderscore(String str){
		str = replaceUtil.replaceBlank(str);
		str = replaceUtil.replaceDot(str);
		str = replaceUtil.replaceAnd(str);
		str = replaceUtil.replaceFrontSlace(str);
		str = replaceUtil.replaceBackSlash(str);
		str = replaceUtil.replaceHash(str);
		str = replaceUtil.replaceAT(str);
		str = replaceUtil.replaceComma(str);
		str = replaceUtil.replaceSemicolon(str);
		str = replaceUtil.replaceColon(str);
		str = replaceUtil.replaceDash(str);
		return str;
	}

	public static String replaceAllSpecialChar(String str){
		str = replaceUtil.replaceAllSpecialCharWithUnderscore(str);
		if (str.contains("_"))
			str = str.replaceAll("_", "");
		return str;
	}

	/*public static void main(String args[]){
		System.out.println(replaceUtil.replaceAllSpecialChar("12-Bikram&Ranjan/Sinha#1@gmail,com;semicolon:colon"));
	}*/
}
