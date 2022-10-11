package com.my.jack.controller;

import com.my.jack.service.DishService;
import com.my.jack.service.impl.DishServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.Random;

class DishControllerTest {


class A{
    public int sum(int a,int b) {
        return a+b;
    }
}

    @Test
    void page() {
        A a= Mockito.mock(A.class);
        System.out.println(a.sum(1,2));
        Mockito.verify(a).sum(1,2);

    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void stopSale() {
    }

    @Test
    void sale() {
    }

    @Test
    void list() {
    }
}