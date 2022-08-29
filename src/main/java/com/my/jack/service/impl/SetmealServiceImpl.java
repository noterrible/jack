package com.my.jack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.common.CustomException;
import com.my.jack.dto.SetmealDto;
import com.my.jack.entity.Setmeal;
import com.my.jack.entity.SetmealDish;
import com.my.jack.mapper.SetmealMapper;
import com.my.jack.service.SetmealDishService;
import com.my.jack.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        })).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖，不能删除");
        }
        //可以删除套餐
        this.removeByIds(ids);
        //删除套餐的菜品
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

    @Override
    public void stopByIds(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(0);
            this.updateById(setmeal);
        }
    }

    @Override
    public void startByIds(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(1);
            this.updateById(setmeal);
        }
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐信息
        this.updateById(setmealDto);
        //清理该套餐的菜品的信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //新增该套餐的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //设置套餐id,计算套餐价格
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //拷贝数据
        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询套餐菜品,并设置
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }
}
