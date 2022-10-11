package com.my.jack.utils;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
/**
 * 测试qq发送验证码邮件
 *
 */
public class SendEmail {
    public static void sendMassage(String email,String code) throws MessagingException{
        //创建Properties 类用于记录邮箱的一些属性
        final Properties props = new Properties();
// 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
//此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
//端口号，QQ邮箱给出了两个端口，但是另一个465一直使用不了，用了就是程序假死的发送失败的，所以就给出这一个587。
        props.put("mail.smtp.port", "587");
// 此处填写你的账号
        props.put("mail.user", "1265684394@qq.com");
// 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", "mbaiivtuczgufhaf");//这里要去掉从QQ邮箱中得到的16位口令中间空格的。
// 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
// 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
// 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
// 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
// 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);
        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(RecipientType.TO, to);
// 设置邮件标题
        message.setSubject("测试邮件");
// 设置邮件的内容体
        message.setContent("您的账号"+email+"@qq.com"+"验证码是"+code, "text/html;charset=UTF-8");
// 最后当然就是发送邮件啦
        Transport.send(message);
        System.out.println("至此QQ邮件发送完毕!");
    }

}


