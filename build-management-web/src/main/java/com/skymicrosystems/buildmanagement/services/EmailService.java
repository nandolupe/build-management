package com.skymicrosystems.buildmanagement.services;

import java.io.File;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @param attachements
	 */
	public void send(String from, String to, String subject, String body, File[] attachements) {
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
	        public void prepare(MimeMessage mimeMessage) throws Exception {

	            mimeMessage.setFrom(new InternetAddress(from));
	            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	            mimeMessage.setSubject(subject);

	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
	            helper.setText(body, true);
	            
	            Multipart multipart = new MimeMultipart();
	            //multipart.addBodyPart(messageBodyPart);
	            
	            MimeBodyPart attachmentPart = null;
	            
	            if (attachements != null) {
	    	        for (File file : attachements) {
	    	        	attachmentPart = new MimeBodyPart();
	    	        	attachmentPart.setFileName(file.getName());	        	
	    	            attachmentPart.attachFile(file);
	    	            multipart.addBodyPart(attachmentPart);
	    	        }
	            }

	            mimeMessage.setContent(multipart);
	        }
	    };

	    try {
	        mailSender.send(preparator);
	    }
	    catch (MailException e) {
	    	logger.error("Erro ao enviar e-mail: " + e.getLocalizedMessage());
	    }
	}
	
	/**
	 * @param from
	 * @param to
	 * @param subject
	 * @param nameTemplate
	 * @param parameters
	 * @param attachements
	 */
	public void send(String from, String to, String nameTemplate, Object[] parameters, File[] attachements) {
		ResourceBundle templates = ResourceBundle.getBundle("email-template");
		
		String body = templates.getString(nameTemplate + ".body");
		String subject = templates.getString(nameTemplate + ".subject");
		
		send(from, to, subject, String.format(body, parameters), attachements);
	}

}
