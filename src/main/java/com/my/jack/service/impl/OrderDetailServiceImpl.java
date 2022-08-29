package com.my.jack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.entity.OrderDetail;
import com.my.jack.mapper.OrderDetailMapper;
import com.my.jack.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
