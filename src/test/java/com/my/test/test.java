package com.my.test;

import com.my.reggie.common.SendEmail;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class test {
    @Test
    public static void main(String[] args) {
        try {
            SendEmail.sendMassage("1265684394", String.valueOf(1234));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
