package atos.manolito.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import atos.manolito.Messages;
import atos.manolito.entity.UserData;

@Service
public class MailServiceImpl implements IMailService {
	
	@Autowired
    Messages messages;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private IMailService mailService;	
	
	@Override
	public void send(String from, String to, String subject, String text) throws Exception {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
		
		mimeMessageHelper.setFrom(from);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(text,true);
		javaMailSender.send(mimeMessage);

	}
	
	@Override
	public void sendMail(UserData userData,String msgMailSent) throws Exception {
		try {
			
			String htmlActivationMail = "<!DOCTYPE html>\r\n" + 
					"<html lang=\"en\">\r\n" + 
					"\r\n" + 
					"<head>\r\n" + 
					"    <meta charset=\"UTF-8\">\r\n" + 
					"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
					"    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" + 
					"    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\r\n" + 
					"    <link rel=\"stylesheet\" href=\"pruebas.css\">\r\n" + 
					"    <title>" + messages.get("ACTIVATION_MAIL_TITLE", null) + "</title>\r\n" + 
					"</head>\r\n" + 
					"\r\n" + 
					"<body>\r\n" + 
					"\r\n" + 
					"    <div class=\"container text-center my-5\">\r\n" + 
					"\r\n" + 
					"        <h1 style=\"margin:30px 0px;\">"+ messages.get("ACTIVATION_MAIL_TITLE", null) +"</h1>\r\n" + 
					" <img src=\"http://localhost:8080/img/logoAtos.png\">\r\n" +
					"        <h4 style=\"margin:30px 0px;\">"+ messages.get("ACTIVATION_MAIL_BODY", null) + "</h4>\r\n" + 
					"        <hr style=\"background-color:blue; width: 100%;\">\r\n" + 
					"\r\n" + 
					"        <a href=\"http://localhost:4200/manolito/activar/"+userData.getDasId()+"\"+ target=\"_blank\" alt=\"" + messages.get("ACTIVATE_ACCOUNT", null) + "\" style=\"color:white;text-decoration: none;\"><button class=\"btn btn-primary\" style=\"width: 50%;height: 100px;font-size: 1.5em;font-family: Arial, Helvetica, sans-serif;\">" + messages.get("ACTIVATE_ACCOUNT", null) + "</button></a>\r\n" + 
					"        <div class=\"row\">\r\n" + 
					"        </div>\r\n" + 
					"\r\n" + 
					"    </div>\r\n" + 
					"</body>\r\n" + 
					"\r\n" + 
					"</html>";
			
			mailService.send("admin0@foo.com", userData.getEmail(), messages.get("ACTIVATION_MAIL_SUBJECT_TITLE",null),
					htmlActivationMail);
		} catch (MailSendException  msex) {
			msgMailSent=messages.get("ERROR_SEND_MAIL",null);
		}
	}
	

}
