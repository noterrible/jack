package com.my.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@ApiModel("菜品")
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    @ApiModelProperty("菜品名称")
    private String name;


    @ApiModelProperty("菜品分类id")
    private Long categoryId;


    @ApiModelProperty("菜品价格")
    private BigDecimal price;


    @ApiModelProperty("商品码")
    private String code;


    @ApiModelProperty("图片")
    private String image;


    @ApiModelProperty("描述信息")
    private String description;


    @ApiModelProperty("售卖状态 0 停售 1 起售")
    private Integer status;


    @ApiModelProperty("顺序")
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    @ApiModelProperty("是否删除")
    private Integer isDeleted;

}
