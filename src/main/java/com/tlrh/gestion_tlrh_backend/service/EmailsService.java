package com.tlrh.gestion_tlrh_backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class EmailsService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void SendEmail(String toEmail,String body,String subject) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setFrom("oumnialk27@gmail.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        javaMailSender.send(mimeMessage);
    }
}
