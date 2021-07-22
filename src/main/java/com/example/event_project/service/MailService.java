package com.example.event_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(String toWhom,
                         String subject,
                         String text) throws MessagingException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("e788b5ff86-55e885@inbox.mailtrap.io");
        mailMessage.setTo(toWhom);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}

