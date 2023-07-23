package com.classroomassistant.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class SendMailVerify {

    @Autowired
    private JavaMailSender mailSender;

    public static JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setUsername("1002800982@qq.com");
        mailSender.setPassword("eqxzugqvxccsbbfc");
        return mailSender;
    }

    public int MailVerify(String mail) {
        boolean result = RegExpUtil.matchEmail(mail);
        int verify = 0;
        MimeMessage message = mailSender.createMimeMessage();
        if (result) {
//            SimpleMailMessage message = new SimpleMailMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                verify = (int) ((Math.random() * 9 + 1) * 100000);
                String text = "<h2>您的验证码为：</h2>" +
                        "<p style='color:red; text-align:center'> " + verify + "（有效期5分钟）</p>" +
                        "<p style='font-weight:bold; text-decoration:underline'>为了保证您的帐户安全，请勿向任何人提供此验证码。本邮件由系统自动发送，请勿直接回复。</p>";
                helper.setSubject("课堂助手绑定邮箱验证");
                helper.setText(text,true);
                helper.setTo(mail);
                helper.setFrom("1002800982@qq.com");
                mailSender.send(message);
                log.info(verify + "========");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return verify;
    }

//    public static int MailMessage(String mail, String head, String body) {
//        if (head.isEmpty() || body.isEmpty()) {
//            log.info("邮件主题或标题为空！");
//            return -1;
//        }
//        boolean result = RegExpUtil.matchEmail(mail);
//        if (result) {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setSubject(head);
//            message.setText(body);
//            message.setTo(mail);
//            message.setFrom("1002800982@qq.com");
//            javaMailSender().send(message);
//            return 1;
//        }
//        return 0;
//    }
}
