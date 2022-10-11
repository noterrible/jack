package com.my.jack.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.jack.common.BaseContext;
import com.my.jack.common.R;
import com.my.jack.entity.ShoppingCart;
import com.my.jack.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Api(tags="购物车相关接口")
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
     * 添加到购物车
     * */
    @PostMapping("/add")
    @ApiOperation(value="添加菜品到购物车接口")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //通过basecontext设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询购物车是否有当前菜品
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            //添加的为菜品,添加条件dish_id=dishId
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加的为套餐,添加条件setmeal_id=setmealId
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart getOne = shoppingCartService.getOne(queryWrapper);
        //商品在购物车则数量加1
        if (getOne != null) {
            Integer number = getOne.getNumber();
            getOne.setNumber(number + 1);
            shoppingCartService.updateById(getOne);
        } else {
            //商品不在购物车则为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            getOne = shoppingCart;
        }
        return R.success(getOne);
    }

    /*
     *减少商品
     * */
    @PostMapping("/sub")
    @ApiOperation(value = "从购物车减少菜品接口")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        if (shoppingCart.getDishId() != null) {
            //查菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //查套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart getOne = shoppingCartService.getOne(queryWrapper);
        if(getOne==null)
            return R.error("菜品已经移除！");
        Integer number = getOne.getNumber();
        getOne.setNumber(number - 1);
        if (number != 1) {
            shoppingCartService.updateById(getOne);
        } else {
            shoppingCartService.removeById(getOne);
        }
        return R.success(getOne);
    }

    /*
     * 清空购物车
     * */
    @DeleteMapping("/clean")
    @ApiOperation(value="清空购物车接口")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("成功清除购物车");
    }

    /*
     * 显示购物车
     * */
    @GetMapping("/list")
    @ApiOperation(value="显示购物车所有菜品接口")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

}
