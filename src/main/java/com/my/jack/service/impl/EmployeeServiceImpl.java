package com.my.jack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.entity.Employee;
import com.my.jack.mapper.EmployeeMapper;
import com.my.jack.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
