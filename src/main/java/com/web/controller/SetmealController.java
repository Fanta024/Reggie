package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.common.R;
import com.web.domain.Setmeal;
import com.web.domain.SetmealDish;
import com.web.dto.SetmealDto;
import com.web.service.SetmealDishService;
import com.web.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<IPage> list(long page,long pageSize){
        IPage pageInfo=new Page(page,pageSize);

        setmealService.page(pageInfo);
        return R.success(pageInfo,"获取成功");

    }
    @GetMapping("/{id}")
    public R<Setmeal> update(@PathVariable long id){
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto,"获取成功");
    }

    @PostMapping
    R<String> test(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

    return R.success(null,"添加成功");
    }

    @PutMapping
    R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("{}",setmealDto);
        setmealService.updateWithDish(setmealDto);

        return R.success(null,"更新成功");
    }

    @DeleteMapping
    R<String> delete(@RequestParam List<Long> ids){

        log.warn("{}",ids);
        setmealService.deleteWithDish(ids);
        return null;
    }

    @PostMapping("/status/{s}")
    R<String> status(@PathVariable Integer s,@RequestParam List<Long> ids){
        log.info("{}",s);
        setmealService.updateStatus(s,ids);
    return R.success(null,"成功");
    }

    @GetMapping("/list")
    R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        return R.success(setmealList,"");

    }

}
