package com.my.jack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.jack.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{

}