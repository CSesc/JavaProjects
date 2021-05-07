package utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


public class EmailSender {
	public static void SendMail(String csvToAddress,String subjectLine,String message,String attachmentPath,String AttachmentName){
		try {
			System.out.println("############## SENDING EMAIL ##############\nTO :\t\t "+
			csvToAddress+"\nSubject:\t"+subjectLine+"\nAttachment:\t"+AttachmentName+"\nAttachment File:\t"+attachmentPath);
			
			
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("smilecosts00@gmail.com", "Rock-pap3r"));
			email.setSSLOnConnect(true);
			email.setFrom("	");
			email.setSubject(subjectLine);
			email.setMsg(message);
			String[] addresses=csvToAddress.split(",");
			for(int i =0;i<addresses.length;i++)
				email.addTo(addresses[i]);	
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(attachmentPath);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("COVID HELP DATA");
			System.out.println("Description: \tCOVID HELP DATA ");
			attachment.setName(AttachmentName);
			email.attach(attachment);
			System.out.println("Send ID: "+email.send());
			System.out.println("############## EMAIL SENT ##############");
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private static void sendEmail() {
//		String mailMessage="Current Status for : \nProcesed : "+workflow + 
//        		"\nCompleted At :\t"+WebUtils.getTimeStamp()+(workflow=="Vacant Beds Tracker"?"\nCity Covered :\t"+city:"")
//        		+"\nMailingList : "+mailingList;
//        		
//        EmailSender.SendMail(mailingList, workflow+ " Results",mailMessage, excelFileName,excelFileName);		
	}
	
public static void sendSuccessMail() 
{	try {
	Email email = new SimpleEmail();
	email.setHostName("smtp.googlemail.com");
	email.setSmtpPort(465);
	email.setAuthenticator(new DefaultAuthenticator("smilecosts00@gmail.com", "Rock-pap3r"));
	email.setSSLOnConnect(true);
	email.setFrom("smilecosts00@gmail.com");
	email.setSubject("Job Succeeded");
	email.setMsg("This is JobSuccess test mail ... :-)");
	email.addTo("xlautomation8@gmail.com");
	email.send();
	}
catch (EmailException e) {
	e.printStackTrace();

}
System.out.println("SUCESS MAIL SENT ");
}
}