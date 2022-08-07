package com.my.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.reggie.common.R;
import com.my.reggie.entity.Employee;
import com.my.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
@Api(tags="员工相关接口")
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /*
     * 员工登录
     * */
    @PostMapping("/login")
    @ApiOperation(value="员工登录接口")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //根据页面获取的psaaword进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的用户名密码进行数据库查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //对数据库用户名查询结果进行判断
        if (emp == null) {
            return R.error("登陆失败，没有该用户");
        }
        //密码校验，密码不一致则返回“密码错误”
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //查看员工状态是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("账号被禁用");
        }
        //登陆成功，将员工id存入Session并返回登陆成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /*
     * 员工退出
     * */
    @PostMapping("/logout")
    @ApiOperation(value="员工退出登录接口")
    public R<String> loginout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /*
     * 添加员工
     * */
    @PostMapping()
    @ApiOperation(value="保存员工接口")
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //添加员工，设置员工默认信息
        employee.setPassword(DigestUtils.md5DigestAsHex("111111".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //获得当前用户ID，设置ID,ID雪花算法自动生成
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /*
     * 分页数据
     * */
    @GetMapping("/page")
    @ApiOperation(value="员工分页显示接口")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //过滤器条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //根据更新时间排序
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /*
     * 修改员工在帐号状态
     * */
    @PutMapping
    @ApiOperation(value="修改员工账号状态接口")
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        //由于Long型数据为19位，js处理精确为前16位，后几位相当于四舍五入，导致获取数据与数据库数据不一致
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息已修改");
    }

    /*
     * 修改员工账号
     * */
    @GetMapping("/{id}")
    @ApiOperation(value="编辑员工信息接口")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到该员工");
    }
}

