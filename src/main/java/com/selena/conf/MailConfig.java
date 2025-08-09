package com.selena.conf;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    //MailConfig = bưu cục Gmail
    //Nơi bạn khai host smtp.gmail.com, cổng 587, user/pass và bật STARTTLS.
    //Nó tạo ra cái “máy gửi thư” tên là JavaMailSender.
	
    @Bean
    public JavaMailSender getJavaMailSender() {
//        Tạo instance JavaMailSenderImpl, cài đặt properties cho JavaMail.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        Địa chỉ và cổng SMTP (Gmail mặc định dùng port 587 với STARTTLS).
        mailSender.setHost("smtp.gmail.com"); // Gửi qua Gmail SMTP
        mailSender.setPort(587);              // Cổng STARTTLS phổ biến

        mailSender.setUsername(MyConstants.MY_EMAIL);    // Tài khoản Gmail
        mailSender.setPassword(MyConstants.MY_PASSWORD); // Mật khẩu (App Password nếu bật 2FA)

        Properties props = mailSender.getJavaMailProperties();
//        mail.transport.protocol: xài SMTP.
        props.put("mail.transport.protocol", "smtp");
//        mail.smtp.auth: yêu cầu auth.
        props.put("mail.smtp.auth", "true");
//        mail.smtp.starttls.enable: bật STARTTLS.
        props.put("mail.smtp.starttls.enable", "true");
//        mail.smtp.ssl.enable: tắt SSL port (nếu muốn dùng SSL port 465 thì bật).
        props.put("mail.smtp.ssl.enable", "false");
//        mail.debug: bật log chi tiết JavaMail.
        props.put("mail.debug", "true");

        // Trả về "bưu cục" để chỗ khác dùng
        return mailSender;
    }
}

