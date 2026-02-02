package poly.edu.java5_asm.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import poly.edu.java5_asm.module.email.service.EmailService;
import poly.edu.java5_asm.module.email.service.EmailServiceImpl;
import poly.edu.java5_asm.module.email.service.SendGridEmailService;

@Configuration
public class EmailConfig {

    @Value("${app.email.provider:smtp}")
    private String emailProvider;

    @Bean
    @Primary
    public EmailService emailService(
            EmailServiceImpl smtpEmailService,
            SendGridEmailService sendGridEmailService) {
        
        if ("sendgrid".equalsIgnoreCase(emailProvider)) {
            return sendGridEmailService;
        }
        return smtpEmailService;
    }
}
