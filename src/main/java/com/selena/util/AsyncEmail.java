package com.selena.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component  // Đăng ký bean để Spring quản lý
public class AsyncEmail {
    public final JavaMailSender emailSender;


    @Autowired  // Spring tự bơm JavaMailSender (từ MailConfig) vào đây
    public AsyncEmail(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

//    @Async("asyncExecutor"): khi gọi sendEmail,
//    nó không chạy ở thread hiện tại; Spring sẽ đưa task vào thread pool tên asyncExecutor.
//  chạy hàm này trên đội shipper
    @Async("asyncExecutor")
//  Trả về void vì task được chạy bất đồng bộ, caller không nhận giá trị trả về.
    public void sendEmail(SimpleMailMessage message) {
         // Gửi thư đi
// emailSender.send(...): gọi xuống thư viện JavaMail để thực sự kết nối SMTP và gửi message.
        this.emailSender.send(message);
    }
}
