package com.my.reggie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@ApiModel("订单详情")
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("订单id")
    private Long orderId;


    @ApiModelProperty("菜品id")
    private Long dishId;


    @ApiModelProperty("套餐id")
    private Long setmealId;


    @ApiModelProperty("口味")
    private String dishFlavor;


    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("图片")
    private String image;
}
