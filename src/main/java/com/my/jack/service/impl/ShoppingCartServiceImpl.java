package com.my.jack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.entity.ShoppingCart;
import com.my.jack.mapper.ShoppingCartMapper;
import com.my.jack.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
