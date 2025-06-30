package com.iiitg.election.services.EmailService;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	private final JavaMailSender mailSender;
	
    private final SpringTemplateEngine templateEngine;
	
    private final EmailBuilderFactory builderFactory;
    
    @Value("${mail.from}")
    private String from;
	
	public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, EmailBuilderFactory builderFactory) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.builderFactory = builderFactory;
    }
	
	public boolean sendEmail(EmailRequest request) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			// Use Thymeleaf to render template
            Context context = new Context();
            context.setVariables(request.getTemplateModel());
            
            
            // Get the template from builderfactory
            String templateName = builderFactory.getTemplateName(request.getEmailType());
            System.err.println("builder");
            String html = templateEngine.process(templateName, context);
            
            System.err.println("Created html");
            
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setFrom(from);
            helper.setText(html, true);
            
            mailSender.send(message);
            
            return true;
		}
		catch (MessagingException e) {
			System.err.println("Error");
            throw new RuntimeException("Failed to send email", e);
        }
		catch (Exception e) {
			throw new RuntimeException("Internal server error", e);
		}
	}
	
	@Async
	public CompletableFuture<Boolean> sendEmailAsync(EmailRequest request) {
        boolean result = sendEmail(request);
        return CompletableFuture.completedFuture(result);
    }
}
