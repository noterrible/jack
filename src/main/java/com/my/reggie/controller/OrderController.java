package com.my.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.reggie.common.BaseContext;
import com.my.reggie.common.R;
import com.my.reggie.entity.Orders;
import com.my.reggie.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
@Api(tags="订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    @ApiOperation(value="订单分页显示接口")
    public R<Page<Orders>> page(int page, int pageSize, String number, String beginTime,String endTime) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        queryWrapper.ge(beginTime != null, Orders::getOrderTime, beginTime);
        queryWrapper.le(endTime != null, Orders::getOrderTime, endTime);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/submit")
    @ApiOperation(value="下单接口")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    @ApiOperation(value="个人中心接口")
    public R<Page> userPage(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(Orders::getCheckoutTime);
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    @ApiOperation(value="订单派送发起接口")
    public R<String> send(@RequestBody Orders orders) {
        Orders orders1 = orderService.getById(orders);
        orders1.setStatus(orders.getStatus());
        orderService.updateById(orders1);
        return R.success("已更新信息");
    }
}
