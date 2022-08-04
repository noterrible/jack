package com.my.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.reggie.dto.DishDto;
import com.my.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增口味数据
    public void saveWithFlavor(DishDto dishDto);
    //根据id查找菜品以及口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    void stopByIds(List<Long> ids);

    void saleByIds(List<Long> ids);
}
