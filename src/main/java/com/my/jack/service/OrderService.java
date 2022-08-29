package com.my.jack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.jack.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
