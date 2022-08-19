package com.web.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.common.CustomException;
import com.web.dao.DishDao;
import com.web.domain.Dish;
import com.web.domain.DishFlavor;
import com.web.dto.DishDto;
import com.web.service.DishFlavorService;
import com.web.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    public RedisTemplate redisTemplate;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) -> {
            //todo  有可能是空  口味   （name）

//            if(!(item.getName().isEmpty()))
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish,"flavors","categoryName");
        updateById(dish);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            //todo  有可能是空  口味   （name）

//            if(!(item.getName().isEmpty()))
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        //清理redis缓存
        //全部
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        //单个
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
        //判断是否在售
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        dishLambdaQueryWrapper.in(Dish::getId, ids);
        Dish dish = getOne(dishLambdaQueryWrapper);
        if (dish != null) {
            throw new CustomException(dish.getName() + "商品在售");
        }
        removeBatchByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(queryWrapper);


    }

    @Override
    public List<DishDto> listWithFlavor(Dish dish) {
        List<DishDto> dishDtoList;

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            return dishDtoList;
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);

        List<Dish> dishList = list(queryWrapper);
        dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishDtoLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            if (category!=null) {
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }


            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key, dishDtoList, 1, TimeUnit.HOURS);
        return dishDtoList;
    }

    @Override
    public void updateStatus(Integer s, List<Long> ids) {
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, s);
        update(updateWrapper);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

    }

}
