package com.silascaimi.fasapi.mail;

import java.util.HashMap;
//import java.util.Arrays;
//import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.model.Usuario;

//import com.silascaimi.fasapi.model.Lancamento;
//import com.silascaimi.fasapi.repository.LancamentoRepository;

@Component
public class AppMailSender {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
//	@Autowired
//	private LancamentoRepository lancamentoRepository;
	
	/* Teste de envio de email simples
	 * 
	 * @EventListener public void teste(ApplicationReadyEvent event) {
	 * this.enviarEmail( "remetente@gmail.com",
	 * Arrays.asList("destinatario@live.com"), "Teste de email", "Teste OK"); }
	 */
	
	// Teste de envio de email com template
//	@EventListener
//	public void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-lancamentos-vencidos";
//		
//		List<Lancamento> lista = lancamentoRepository.findAll();
//		
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("lancamentos", lista);
//		
//		this.enviarEmail("silascaimi.br@gmail.com", 
//				Arrays.asList("silascaimi@live.com"), 
//				"Teste de email", 
//				template, variaveis);
//	}
	
	// Email simples
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
	
	// Email com template thymeleaf
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, 
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = thymeleaf.process(template, context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamento", vencidos);
		
		List<String> emails = destinatarios
				.stream()
				.map(u -> u.getEmail())
				.collect(Collectors.toList());
		
		String template = "mail/aviso-lancamentos-vencidos";
		
		enviarEmail("remetente@email.com", emails, "Lançamentos Vencidos", template, variaveis);
	}
}
