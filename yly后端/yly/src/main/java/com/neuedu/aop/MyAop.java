package com.neuedu.aop;//package com.neuedu.aop;
//
//import jakarta.annotation.Resource;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//public class MyAop {
//    @Value("${spring.mail.username}")
//    String from;
//    @Resource
//    JavaMailSender javaMailSender;
////    @AfterReturning("execution(public * com.neuedu.service.impl.UmsUserServiceImpl.add(..))")
//    public void sendMail(JoinPoint joinPoint) throws MessagingException {
//        // 获取参数
//        /*UmsUser umsUser = (UmsUser)joinPoint.getArgs()[0];
//        // 获取收件人
//        String to = umsUser.getEmail();
//        // 定义标题和内容
//        String subject = "系统消息";
//        // 定义内容
//        String text = String.format( """
//                <h2>%s用户:</h2>
//                <p>你好，欢迎注册燕大实训系统，未来可以用您的电子信箱或者手机号登录<p>
//                """, umsUser.getName());
//        sendEmail(subject, to, text);*/
//    }
//    private void sendEmail(String subject, String to, String text) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//        helper.setFrom(from);
//        helper.setTo(new String[]{to});
//        helper.setSubject(subject);
//        helper.setText(text, true);
//        javaMailSender.send(mimeMessage);
//    }
//}
