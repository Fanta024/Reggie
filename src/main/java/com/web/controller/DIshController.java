package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.common.R;
import com.web.domain.Category;
import com.web.domain.Dish;
import com.web.dto.DishDto;
import com.web.service.CategoryService;
import com.web.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DIshController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> list(long page,long pageSize,String name){
        Page<Dish> pageInfo=new Page(page,pageSize);

        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝   除去records
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //处理records   添加categoryName
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            //将records拷贝到dishDto
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        //设置dishDtoPage的records
        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage,"获取成功");

    }
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto,"获取成功");
    }

    @PostMapping
   public R<String> save(@RequestBody DishDto dishDto) {
        log.info("{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success(null,"添加成功");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success(null,"修改成功");
    }
    //todo 批量删除  setmeal可参考
    @DeleteMapping
    public R<String> delete(long id){

        dishService.deleteWithFlavor(id);


        return R.success(null,"删除成功");
    }

//    @GetMapping("/list")
//    R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list,"获取成功");
//    }

//    @GetMapping("/list")
//    R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
//        //启售的商品
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//        DishDto dishDto = new DishDto();
//
//        list.stream().map((itme)->{
//            Long dishId = itme.getId();
//        }).collect(Collectors.toList());
//
//
//        return R.success(list,"获取成功");
//    }

    @GetMapping("/list")
    R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtos = dishService.listWithFlavor(dish);
        return R.success(dishDtos,"获取成功");
    }
}
