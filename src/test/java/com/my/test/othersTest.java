package com.my.test;

import org.junit.jupiter.api.Test;

public class othersTest {
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
        long c=1;
        int d= (int)(c);
        int e=1;
        long f=e;
        System.out.println(d);
        System.out.println(f);
    }
}
