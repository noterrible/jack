package com.my.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.reggie.common.R;
import com.my.reggie.dto.DishDto;
import com.my.reggie.entity.Category;
import com.my.reggie.entity.Dish;
import com.my.reggie.entity.DishFlavor;
import com.my.reggie.service.CategoryService;
import com.my.reggie.service.DishFlavorService;
import com.my.reggie.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/*
菜品管理
* */
@Api(tags="菜品相关接口")
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * 菜品分页显示
     * */
    @GetMapping("/page")
    @ApiOperation(value="菜品分页显示接口")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加分页搜索条件
        queryWrapper.like(name != null, Dish::getName, name);
        //按照更新时间排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);
        //拷贝对象
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        //在菜品管理中显示菜品分类
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据Id查询分类对象
            Category category = categoryService.getById(categoryId);
            //SQL文件可能数据有问题，差不到，空指针
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @PostMapping
    @ApiOperation(value="保存新增的菜品信息接口")
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);

        /*//后台更新菜品，清理所有菜品缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/

        //清理对应分类菜品
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("添加菜品成功");
    }

    @DeleteMapping
    @ApiOperation(value="删除菜品接口")
    public R<String> delete(@RequestParam List<Long> ids) {
        //获取菜品分类id，删除redis缓存
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            //清理redis对应分类菜品缓存
            String key = "dish_" + dish.getCategoryId() + "_1";
            redisTemplate.delete(key);
        }

        //删除菜品
        dishService.removeByIds(ids);

        /*//后台更新菜品，清理所有菜品缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/


        return R.success("删除菜品成功");
    }


    @GetMapping("/{id}")
    @ApiOperation(value="修改菜品信息接口")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if (dishDto != null) {
            return R.success(dishDto);
        }
        return R.error("没有该菜品");
    }

    @PutMapping
    @ApiOperation(value="保存修改的菜品信息接口")
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);

        /*//后台更新菜品，清理所有菜品缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/
        //清理对应分类菜品
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("更新菜品成功");
    }
    /*
    *停售启售
    * */
    @PostMapping("/status/0")
    @ApiOperation(value="停售菜品接口")
    public R<String> stopSale(@RequestParam List<Long> ids){
        dishService.stopByIds(ids);
        return R.success("停售成功");
    }
    @PostMapping("/status/1")
    @ApiOperation(value="起售菜品接口")
    public R<String> sale(@RequestParam List<Long> ids){
        dishService.saleByIds(ids);
        return R.success("启售成功");
    }
    /* @GetMapping("/list")
     public R<List<Dish>> list(Dish dish){
         LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
         //条件
         queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
         queryWrapper.eq(Dish::getStatus,1);

         //排序条件sort
         queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
         List<Dish> list = dishService.list(queryWrapper);
         return R.success(list);
     }*/
    /*
     * 菜品按分类显示
     * */
    @GetMapping("/list")
    @ApiOperation(value="菜品按菜品分类显示接口")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        ///设置key
        String key = "dish_" + dish.getCategoryId() + "_1";
        //从redis中查询是否有，有则返回redis中数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);

        //排序条件sort
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据Id查询分类对象
            Category category = categoryService.getById(categoryId);
            //SQL文件可能数据有问题，差不到，空指针
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            //设置口味
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        //如果redis中不存在数据，将菜品数据写入缓存，60分钟过期
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
