package com.uilities;


import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import com.google.common.io.Files;
import com.platformLayer.driverScript;


public class SendMail {

	public static Logger logger = Logger.getLogger(driverScript.class);
	// Function Not ready 
	public static void mailling(String messageText) {
		if((Constants.totalPassTcCount+Constants.totalFailTcCount)>1)
		{
			final String username = "xxxx";
			final String password = "xxx!";
			final String recipantsTo ="axxxxthar.xx@gmai.com,xx.xxxxx@gmail.com";
	

			String mailBody = "";
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "mailrelay.xxxx.com");//"mailready.tieto.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				try {
					Inet4Address ipaddress= (Inet4Address) Inet4Address.getLocalHost();
					String ip=ipaddress.getHostAddress();
					mailBody = Files.toString(new File(Constants.commonBatfilePath +"\\emailTemplate.html"), Charsets.UTF_8);
					mailBody = mailBody.replaceAll("#TotalTC", String.valueOf((Constants.totalPassTcCount+Constants.totalFailTcCount)));
					mailBody = mailBody.replaceAll("#PassedTC", String.valueOf(Constants.totalPassTcCount));
					mailBody = mailBody.replaceAll("#FailedTC", String.valueOf(Constants.totalFailTcCount));
					mailBody = mailBody.replaceAll("#DataRow", Constants.testCaseStatus);
					
					String reportDirectory="";
					reportDirectory= "\\\\"+ip+Constants.suitReportName.substring(2);
					reportDirectory=reportDirectory.replace("\\", "\\\\");
					mailBody = mailBody.replaceAll("#TestResultLink", "<a href=" + '"' +reportDirectory+'"'+">here</a>");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = new MimeMessage(session);

				message.setFrom(new InternetAddress("xxxxx@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipantsTo));
				message.setSubject(Constants.currentTestSuiteName + " --> Night Execution report ");
				message.setContent(mailBody,"text/html; charset=utf-8");
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
				logger.error(e);
				throw new RuntimeException(e);
			}

		}
	}    

}