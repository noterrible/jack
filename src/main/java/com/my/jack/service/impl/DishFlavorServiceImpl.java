package com.my.jack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.entity.DishFlavor;
import com.my.jack.mapper.DishFlavorMapper;
import com.my.jack.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
