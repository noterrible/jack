package com.my.jack.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@ApiModel("地址")
@Data
public class AddressBook implements Serializable {
    @ApiModelProperty("")
    private static final long serialVersionUID = 1L;

    private Long id;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("收货人")
    private String consignee;


    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("性别 0 女 1 男")
    private String sex;


    @ApiModelProperty("省级区划编号")
    private String provinceCode;


    @ApiModelProperty("省级名称")
    private String provinceName;


    @ApiModelProperty("市级区划编号")
    private String cityCode;


    @ApiModelProperty("市级名称")
    private String cityName;


    @ApiModelProperty("区级区划编号")
    private String districtCode;


    @ApiModelProperty("区级名称")
    private String districtName;


    @ApiModelProperty("详细地址")
    private String detail;


    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("是否默认 0 否 1是")
    private Integer isDefault;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    @ApiModelProperty("是否删除")
    private Integer isDeleted;
}
