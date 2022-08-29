package com.my.jack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.jack.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
