package com.uilities;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class ExecutionLogger{
	public static void initiateLogger(String suitName) {
		try {
			String filePath = Constants.reportParentPath+"\\"+suitName+".log" ;
			//PatternLayout layout = new PatternLayout("%2p %d{yyyy-MM-dd HH:mm:ss} [%c] [%t]:%L - %m%n");
			PatternLayout layout = new PatternLayout("%2p %d{yyyy-MM-dd HH:mm:ss} [%c] :%L - %m%n");
			RollingFileAppender appender = new RollingFileAppender(layout, filePath);
			appender.setMaxFileSize("100MB");
			appender.activateOptions();
			Logger.getRootLogger().setLevel(Level.INFO);
			Logger.getRootLogger().addAppender(appender);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}