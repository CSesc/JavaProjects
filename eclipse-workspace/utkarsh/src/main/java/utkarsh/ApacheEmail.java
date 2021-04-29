package utkarsh;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class ApacheEmail {
	public static void SendMail(String csvToAddress,String subjectLine,String message,String attachmentPath,String AttachmentName){
		try {
			System.out.println("############## SENDING EMAIL ##############\nTO :\t\t "+csvToAddress+"\nSubject:\t"+"\nAttachment:\t"+AttachmentName+"\nAttachment File:\t"+attachmentPath);
			
			
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("smilecosts00@gmail.com", "Kriti7333"));
			email.setSSLOnConnect(true);
			email.setFrom("smilecosts00@gmail.com");
			email.setSubject(subjectLine);
			email.setMsg(message);
			String[] addresses=csvToAddress.split(",");
			for(int i =0;i<addresses.length;i++)
				email.addTo(addresses[i]);	
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(attachmentPath);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("Picture of John");
			attachment.setName(AttachmentName);
			email.attach(attachment);
			email.send();
			System.out.println("############## EMAIL SENT ##############");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
