package com.web.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.common.CustomException;
import com.web.dao.CategoryDao;
import com.web.domain.Category;
import com.web.domain.Dish;
import com.web.domain.Setmeal;
import com.web.service.CategoryService;
import com.web.service.DishService;
import com.web.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(long id) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount>0){
            //todo throwException
            log.info("dishCount={}",dishCount);
            throw new CustomException("已存在关联菜单，无法删除");
        }


        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount>0){
            //todo throwException
            log.info("setmealCount={}",setmealCount);
            throw new CustomException("已存在关联套餐，无法删除");
        }

        removeById(id);

    }
}
