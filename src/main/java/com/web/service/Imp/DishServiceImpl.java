package com.web.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.DishDao;
import com.web.domain.Category;
import com.web.domain.Dish;
import com.web.domain.DishFlavor;
import com.web.dto.DishDto;
import com.web.service.CategoryService;
import com.web.service.DishFlavorService;
import com.web.service.DishService;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
//todo The dependencies of some of the beans in the application context form a cycle:
//
//   categoryController (field private com.web.service.CategoryService com.web.controller.CategoryController.categoryService)
//┌─────┐
//|  categoryServiceImpl (field private com.web.service.DishService com.web.service.Imp.CategoryServiceImpl.dishService)
//↑     ↓
//|  dishServiceImpl (field private com.web.service.CategoryService com.web.service.Imp.DishServiceImpl.categoryService)
//└─────┘
//
//
//Action:
//
//Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans. As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.
////    @Lazy
//    @Autowired
//    private CategoryService categoryService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item)->{
            //todo  有可能是空  口味   （name）

//            if(!(item.getName().isEmpty()))
                item.setDishId(dishId);
            return item;}).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
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
        flavors.stream().map((item)->{
            //todo  有可能是空  口味   （name）

//            if(!(item.getName().isEmpty()))
            item.setDishId(dishDto.getId());
            return item;}).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public void deleteWithFlavor(long id) {
        removeById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);

    }

    @Override
    public List<DishDto> listWithFlavor(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);

        List<Dish> dishList = list(queryWrapper);
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
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

        return dishDtoList;
    }

    @Override
    public void updateStatus(Integer s, List<Long> ids) {
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, s);
        update(updateWrapper);

    }

}
