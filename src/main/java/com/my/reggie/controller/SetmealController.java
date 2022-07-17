package com.my.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.reggie.common.R;
import com.my.reggie.dto.DishDto;
import com.my.reggie.dto.SetmealDto;
import com.my.reggie.entity.Category;
import com.my.reggie.entity.Dish;
import com.my.reggie.entity.Setmeal;
import com.my.reggie.entity.SetmealDish;
import com.my.reggie.service.CategoryService;
import com.my.reggie.service.SetmealDishService;
import com.my.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        //按照name排序
        queryWrapper.orderByAsc(Setmeal::getName);
        setmealService.page(pageInfo, queryWrapper);
        //拷贝对象
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            //分类Id
            Long categoryId = item.getCategoryId();
            //根据id查询对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {

                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("保存套餐成功");
    }

    @CacheEvict(value = "setmealCache",allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("已删除该套餐");
    }
    @PostMapping("/status/0")
    public  R<String> stop(@RequestParam List<Long> ids){
        setmealService.stopByIds(ids);
        return R.success("停售成功");
    }
    @PostMapping("/status/1")
    public  R<String> start(@RequestParam List<Long> ids){
        setmealService.startByIds(ids);
        return R.success("停售成功");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        //获取套餐
        SetmealDto setmealDto = setmealService.getWithDish(id);
        if( setmealDto!=null){
            return R.success(setmealDto);
        }
        return R.error("没有改套餐");
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功");
    }
}
