package com.uilities;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.platformLayer.selActionKeywords;


public class fileFunctions{

	public static void executeBatFile(String batFilePath)	{
		Process pross=null;
		if(Constants.kyExecutionCounter.equals(true)){
			try{
				String[] command = {"cmd.exe", "/C", "Start", batFilePath};				
				pross =  Runtime.getRuntime().exec(command);     
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				reportUtil.logReport(batFilePath + " file NOT found");
				//ex.printStackTrace();
				pross.destroyForcibly();
			}
		}
	}

	public static void  createNewBatFile(String strFileName, String strCommand){

		if(Constants.kyExecutionCounter.equals(true)){
			selActionKeywords.kyWait(2);
			if(Constants.kyExecutionCounter.equals(true)){
				try{			
					PrintWriter writer = new PrintWriter(strFileName, "UTF-8");
					//System.out.println( "createBatAndTxtfile --> " + docViewTxtFile);
					writer.println(strCommand);

					writer.close();
				} catch (Exception e) {
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("NOT able to create .bat file in the location " + strFileName);
				}				
			}
		}

	}

	public static void  appendExsistingBatFile(String strFileName, String strCommand){

		if(Constants.kyExecutionCounter.equals(true)){
			selActionKeywords.kyWait(2);
			if(Constants.kyExecutionCounter.equals(true)){
				try{			
					Files.write(Paths.get(strFileName), strCommand.getBytes(), StandardOpenOption.APPEND);
				} catch (Exception e) {
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("NOT able to create .bat file in the location " + strFileName);
				}		
			}
		}		
	}

	public static String createBatAndTxtfile(){
		String docViewTxtFile = "";
		if(Constants.kyExecutionCounter.equals(true)){
			selActionKeywords.kyWait(2);
			docViewTxtFile = createTxtfile();

			if(Constants.kyExecutionCounter.equals(true)){
				try{			
					PrintWriter writer = new PrintWriter(Constants.commonBatfile, "UTF-8");
					//System.out.println( "createBatAndTxtfile --> " + docViewTxtFile);
					String strText="if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start \"\" /max \""+docViewTxtFile+"\" %* && exit";
					writer.println(strText);
					writer.println("exit");
					writer.close();
				} catch (Exception e) {
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("NOT able to create .bat file in the location " + Constants.commonBatfile);
				}
				fileFunctions.executeBatFile(Constants.commonBatfile);
			}
		}
		return docViewTxtFile;
	}

	/**
	 * Create file with any extension 
	 * @param fileName --> File Name with Absolute Path
	 * @return
	 */
	public static String createfile(String fileName, String fileExtension){
		String newfileName ="";
		if(Constants.kyExecutionCounter.equals(true)){
			
			/*if (fileExtention.endsWith(".txt"))
				fileName = fileName + "\\.txt";*/

			//docViewTxtFile = Constants.reportTestDataFolderPath +"\\"+fileName+".txt";
			newfileName = fileName + fileExtension;
			try{			
				PrintWriter writer = new PrintWriter(newfileName, "UTF-8");
				writer.close();
			} catch (Exception e) {
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("NOT able to create file in the location " + newfileName);
			}
			selActionKeywords.kyWait(2);
		}
		return newfileName;
	}

	/**
	 * Create text file specifically inside test data folder
	 * @return
	 */
	public static String createTxtfile(){
		String fileName ="";
		if(Constants.kyExecutionCounter.equals(true)){
			fileName = "file_"+commonUtil.uniqueNumber();
			fileName= Constants.reportTestDataFolderPath +"\\"+fileName;
			fileName = createfile(fileName,".txt");			
		}
		return fileName;		
	}

	public static void appendTextInTextFile(String fileName, String text){
		/*try{
		    
		    FileWriter fw = new FileWriter(fileName,true); //the true will append the new data
		    fw.write(text);//appends the string to the file
		    //fw.write("\n");
		    fw.close();
		}catch(IOException ioe){
		    System.err.println("IOException: " + ioe.getMessage());
		}*/
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try{
		    fw = new FileWriter(fileName, true);
		    bw = new BufferedWriter(fw);
		    out = new PrintWriter(bw);
		    out.println(text);
		    out.close();
		    bw.close();
		    fw.close();
		}catch (Exception e) {
		    //exception handling left as an exercise for the reader
		}finally {
		    try {
		        if(out != null)
		            out.close();
		    }catch(Exception e) {}
		    try {
		        if(bw != null)
		            bw.close();
		    }catch (IOException e) {}
		    try {
		        if(fw != null)
		            fw.close();
		    } catch (IOException e) {}
		}
	}


	public static void pasteInTxtFile(){
		if(Constants.kyExecutionCounter.equals(true)){
			try{			
				selActionKeywords.kyWait(2);
				Robot r = new Robot();
				r.keyPress(KeyEvent.VK_CONTROL); 
				r.keyPress(KeyEvent.VK_V);
				r.keyRelease(KeyEvent.VK_V);
				r.keyRelease(KeyEvent.VK_CONTROL);
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("Key sequence not worked for key <b> Ctrl + V <\b> (Paste) ");
				//ex.printStackTrace();
			}
		}
	}

	public static void saveTextFile(){
		if(Constants.kyExecutionCounter.equals(true)){
			try{
				Robot r = new Robot();
				selActionKeywords.kyWait(2);
				r.keyPress(KeyEvent.VK_CONTROL); 
				r.keyPress(KeyEvent.VK_S);
				r.keyRelease(KeyEvent.VK_S);
				r.keyRelease(KeyEvent.VK_CONTROL);			 
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("Key sequence not worked for key <b> Ctrl + S <\b> (Save)");
				//ex.printStackTrace();
			}
		}
	}

	public static void closeFile(){
		if(Constants.kyExecutionCounter.equals(true)){
			try{
				Robot r = new Robot();
				selActionKeywords.kyWait(2);
				r.keyPress(KeyEvent.VK_ALT);
				r.keyPress(KeyEvent.VK_F);
				selActionKeywords.kyWait(1);
				r.keyPress(KeyEvent.VK_X);
				r.keyRelease(KeyEvent.VK_X);
				r.keyRelease(KeyEvent.VK_F);
				r.keyRelease(KeyEvent.VK_ALT);	
				selActionKeywords.kyWait(2);
			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("Key sequence not worked for key <b> Alt + X <\b> (Close)");
				//ex.printStackTrace();
			}finally{
				executeBatFile(System.getProperty("user.dir")+"\\otherRequiredFiles\\kill_Notepad.bat");
			}
			
		}
	}

	public static void verifyText(String fileName , String text){		
		if(Constants.kyExecutionCounter.equals(true)){
			BufferedReader br = null;
			try 
			{
				String sCurrentLine;
				//System.out.println(" fileName ---> "+ fileName);

				br = new BufferedReader(new FileReader(fileName));
				selActionKeywords.kyWait(2);
				boolean check = false;

				//System.out.println("text ="+text+"=");
				while ((sCurrentLine = br.readLine()) != null) {
					//System.out.println(sCurrentLine);

					if (sCurrentLine.toUpperCase().contains(text.toUpperCase())){
						//if(sCurrentLine.trim().toUpperCase().contains(text.trim().toUpperCase())){
						//System.out.println(" Found it found it");
						reportUtil.logReport("Text = "+text+" Found in the file"+ Constants.nextLine + fileName);
						check = true;
						break;	
					}						
				}				
				if(check == false){
					Constants.kyExecutionCounter = false;
					reportUtil.logReport("Text = "+text+" NOT found in the file"+ Constants.nextLine + fileName);
				}

			} catch (IOException e) {
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("Text = "+text+" NOT found in the file"+ Constants.nextLine + fileName);
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					//ex.printStackTrace();
				}
			}

		}
	}

	public static void verifyTextNotPresent(String fileName , String text){		
		if(Constants.kyExecutionCounter.equals(true)){
			BufferedReader br = null;
			try 
			{
				String sCurrentLine;
				System.out.println(" fileName ---> "+ fileName);

				br = new BufferedReader(new FileReader(fileName));
				selActionKeywords.kyWait(2);
				boolean check = false;

				//System.out.println("text ="+text+"=");
				while ((sCurrentLine = br.readLine()) != null) {
					//System.out.println(sCurrentLine);

					if (sCurrentLine.toUpperCase().contains(text.toUpperCase())){
						//if(sCurrentLine.trim().toUpperCase().contains(text.trim().toUpperCase())){
						//System.out.println(" Found it found it");

						Constants.kyExecutionCounter = false;
						reportUtil.logReport("Text = "+text+" found in the file"+ Constants.nextLine + fileName);
						check = true;
						break;	
					}	

				}				
				if(check == false){
					Constants.kyExecutionCounter = true;
					reportUtil.logReport("Text = "+text+" NOT found in the file"+ Constants.nextLine + fileName);
				}

			} catch (IOException e) {
				//Constants.kyExecutionCounter = false;
				//reportUtil.logReport("Text = "+text+" NOT found in the file"+ Constants.nextLine + fileName);
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					//ex.printStackTrace();
				}
			}

		}
	}

	/**
	 * Replace some string/text in a exsisting file
	 * @param fileName --> Provide file name with extention. i.e.  "try_1.txt" or "try_1.xml" etc.
	 * @param stringToReplace 
	 * @param replacingString
	 */
	public static void updateFile(String fileName, String stringToReplace, String replacingString) {
		try {
			// Open a temporary file to write to.
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName+"_new.temp")));

			// ... then inside your loop ...
			FileReader reader =  new FileReader(fileName);;
			BufferedReader br = new BufferedReader(reader);
			String line;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if (line.contains(stringToReplace)) {
					line = line.replace(stringToReplace,replacingString);	    	        
				}
				// Always write the line, whether you changed it or not.
				writer.println(line);
			}

			br.close();
			reader.close();
			writer.close();

			File realName = new File(fileName);
			realName.delete(); // remove the old file	    	
			new File(fileName+"_new.temp").renameTo(realName); // Rename temp file

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem reading file.");
		}
	}

	/**
	 * The function returns the entire line depend on t first occurrence of the searching string 
	 * @param fileName
	 * @param searchingString
	 * @return
	 */
	public static String getSpeificLine(String fileName, String searchingString){
		String strLine = null;
		if(Constants.kyExecutionCounter.equals(true)){
			try{
				FileReader reader =  new FileReader(fileName);;
				BufferedReader br = new BufferedReader(reader);
				String line;

				while ((line = br.readLine()) != null) {

					if (line.contains(searchingString)) {
						strLine= line;
						break;		    	    	
					}
				}		    	
				br.close();
				reader.close();	

				if(strLine.equals(null)){
					System.out.println(searchingString+" NOT found in the file="+fileName);
					reportUtil.logReport(searchingString+" NOT found in the file="+fileName);
				}

			}catch(Exception ex){
				Constants.kyExecutionCounter = false;
				reportUtil.logReport("Error occard in function getSpeificLine");
				ex.printStackTrace();
			}
		}
		return strLine;
	}

}
