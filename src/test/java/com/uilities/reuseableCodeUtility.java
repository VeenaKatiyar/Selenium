package com.uilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.platformLayer.selActionKeywords;

public class reuseableCodeUtility {

	public static void get_All_Frames_in_Window(){
		//---------------------------------------------------------------------------------------------------
		selActionKeywords.frameHandelling.kyDefaultContent();
		try{
			final List<WebElement> iframes = selActionKeywords.driver.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				System.out.println(iframe.getAttribute("id"));
			}

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("No iframe found");
		}


		try{
			final List<WebElement> frames = selActionKeywords.driver.findElements(By.tagName("frame"));
			for (WebElement frame : frames) {
				System.out.println(frame.getAttribute("name"));			        
			}
		}catch(Exception e){
			e.printStackTrace();			    	
			System.out.println("No frame found");
		}
		//---------------------------------------------------------------------------------------------------

	}

}
