package com.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.dao.DishDao;
import com.web.dao.EmployeeDao;
import com.web.dto.DishDto;
import com.web.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;

@SpringBootTest
class SpringbootWaimaiApplicationTests {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void getPage() {
        IPage iPage = new Page(1, 2);
        IPage page = employeeService.page(iPage, null);
        System.out.println(page.getRecords());
        System.out.println(page.getTotal());
        System.out.println(page.getSize());
    }

    @Test
    void  test2(){
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }


    @Test

    void  test3() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("name", "xm");
        String name = stringStringValueOperations.get("name");
        System.out.println(name);
    }
}
