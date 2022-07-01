package com.skymicrosystems.buildmanagement.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtils {
	
	private final String username;
    private final String password;
    private final String host;
    private final int port;

    public MailUtils(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }
	
	public Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.port", this.port);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        /*
        props.put("spring.mail.host", "smtp.gmail.com");
		props.put("spring.mail.port", "587");
		//props.put("spring.mail.username", "xxxxxx");
		//props.put("spring.mail.password", "xxxxxx");
		props.put("spring.mail.properties.mail.smtp.starttls.enable", "true");
		props.put("spring.mail.properties.mail.smtp.starttls.required", "true");
		props.put("spring.mail.properties.mail.smtp.auth", "true");
         */
        
        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
	
	public void sendMail(String from, String to, String subject, String body, File[] attachements) throws MessagingException, IOException {
		
        Message message = new MimeMessage(getSession());
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        MimeBodyPart attachmentPart = null;
        
        if (attachements != null) {
	        for (File file : attachements) {
	        	attachmentPart = new MimeBodyPart();
	        	attachmentPart.setFileName(file.getName());	        	
	            attachmentPart.attachFile(file);
	            multipart.addBodyPart(attachmentPart);
	        }
        }

        message.setContent(multipart);
        Transport.send(message);
    }
	
	public static void main(String[] args) {
		MailUtils mailUtils = new MailUtils("nandolupe@gmail.com", "yxuruakoxedkzyld", "smtp.gmail.com", 465);
		try {
			
			mailUtils.sendMail("nandolupe@gmail.com", "nandolupe@gmail.com", "Teste envio", "Teste envio body", null);
		
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
