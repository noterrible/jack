package com.my.jack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.jack.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
