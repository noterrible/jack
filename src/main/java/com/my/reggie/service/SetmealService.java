package com.my.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.reggie.dto.SetmealDto;
import com.my.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);


    void stopByIds(List<Long> ids);

    void startByIds(List<Long> ids);

    SetmealDto getWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
