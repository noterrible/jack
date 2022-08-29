package com.my.jack.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 */
@ApiModel("用户")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    @ApiModelProperty("姓名")
    private String name;


    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("性别 0 女 1 男")
    private String sex;


    @ApiModelProperty("身份证号")
    private String idNumber;


    @ApiModelProperty("头像")
    private String avatar;


    @ApiModelProperty("状态 0:禁用，1:正常")
    private Integer status;
}
