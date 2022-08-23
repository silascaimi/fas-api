package com.silascaimi.fasapi.mail;

//import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class AppMailSender {

	@Autowired
	private JavaMailSender mailSender;
	
	/* Teste de envio de email simples
	 * 
	 * @EventListener public void teste(ApplicationReadyEvent event) {
	 * this.enviarEmail( "remetente@gmail.com",
	 * Arrays.asList("destinatario@live.com"), "Teste de email", "Teste OK"); }
	 */
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			messageHelper.setFrom(remetente);
			messageHelper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			messageHelper.setSubject(assunto);
			messageHelper.setText(mensagem, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problema com o envio de email!", e);
		}
	}
}
