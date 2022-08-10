package com.my.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {
    //全局变量初值为0
    int a;

    @Test
    public void test(){
        System.out.println(a);
        //测试String地址
        String b="1";
        System.out.println(b.hashCode());
        b="2";
        System.out.println(b.hashCode());
        
    }
}
