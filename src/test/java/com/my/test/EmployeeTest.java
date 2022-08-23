package com.my.test;

import com.my.reggie.ReggieApplication;
import com.my.reggie.common.BaseContext;
import com.my.reggie.entity.Employee;
import com.my.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ReggieApplication.class)
public class EmployeeTest {
    @Autowired
    private EmployeeService employeeService;
    @Test
    public void save(){
        BaseContext.setCurrentId(1l);
        Employee employee = new Employee();
        employee.setName("testE1");
        employee.setUsername("eee");
        employee.setPassword("1234");
        employee.setPhone("15626353624");
        employee.setSex("男");
        employee.setIdNumber("41523620032625352X");
        employeeService.save(employee);
    }
    @Test
    public void getById(){
        employeeService.list();
    }
}
