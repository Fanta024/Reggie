package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.EmployeeDao;
import com.web.domain.Employee;
import com.web.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
}
