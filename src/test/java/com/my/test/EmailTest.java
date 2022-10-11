package com.my.test;

import com.my.jack.utils.SendEmail;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class EmailTest {
    @Test
    public static void main(String[] args) {
        try {
            SendEmail.sendMassage("1265684394@qq.com", String.valueOf(1234));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
