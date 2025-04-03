package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.service.interfaces.ConfirmationService;
import de.aittr.car_rent.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${host.url}")
    private String hostUrl;
    private final JavaMailSender sender;
    private final Configuration mailConfig;
    private final ConfirmationService confirmationService;

    public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig, ConfirmationService confirmationService) {
        this.sender = sender;
        this.mailConfig = mailConfig;
        this.confirmationService = confirmationService;

        mailConfig.setDefaultEncoding("UTF-8");
        mailConfig.setTemplateLoader(new ClassTemplateLoader(EmailServiceImpl.class, "/mail"));
    }

    @Async
    @Override
    public void sendConfirmationEmail(Customer customer) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        String text = generateConfirmationEmail(customer);

        try {
            helper.setFrom("el.riu.m8@gmail.com");
            helper.setTo(customer.getEmail());
            helper.setSubject("Registration Confirmation");
            helper.setText(text, true);

            sender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateConfirmationEmail(Customer customer) {
        try {
            Template template = mailConfig.getTemplate("confirm_reg_mail.ftlh");
            String code = confirmationService.generateConfirmationCode(customer);

            Map<String, Object> params = new HashMap<>();
            params.put("name", customer.getFirstName());
            params.put("link", hostUrl + "/api/auth/confirm/" + code);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
