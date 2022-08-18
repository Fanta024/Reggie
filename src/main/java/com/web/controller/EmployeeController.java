package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.common.R;
import com.web.domain.Employee;
import com.web.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
        public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){

        //加密

        String password=employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp==null){
            return R.error("登录失败");
        }

        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }


        if(emp.getStatus().equals(0)){
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("employee",emp.getId());


        return R.success(emp,"登录成功");

    }

    @PostMapping("/logout")
    public R<Employee> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success(null,"登录成功");
    }

    @PostMapping
    public R<Employee> addEmp(@RequestBody Employee employee, HttpServletRequest request){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));


//        Long UserId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(UserId);
//        employee.setUpdateUser(UserId);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.save(employee);

        return R.success(null,"添加成功") ;
    }

    @GetMapping("/page")
    public R<IPage> select(long page,long pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        IPage pageInfo=new Page(page,pageSize);


        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper();
        employeeLambdaQueryWrapper.like(name!=null,Employee::getName,name);
        employeeLambdaQueryWrapper.orderByDesc(Employee::getCreateTime);


        employeeService.page(pageInfo,employeeLambdaQueryWrapper);
        return R.success(pageInfo,"成功");
    }

    @PutMapping
    public R<Employee> status(@RequestBody Employee employee,HttpServletRequest request){

//        Long UserId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(UserId);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        long id = Thread.currentThread().getId();
        log.info("线程id{}",id);

        return R.success(null,"更新成功");
    }
    @GetMapping("/{id}")
    public R<Employee> edit(@PathVariable long id){

        Employee employee = employeeService.getById(id);
        if (employee==null){
            return R.error("获取失败");
        }
        return R.success(employee,"获取成功");
    }
}
