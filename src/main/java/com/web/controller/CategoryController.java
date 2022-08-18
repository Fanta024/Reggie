package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.common.R;
import com.web.domain.Category;
import com.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<Category> add(@RequestBody Category category){
        categoryService.save(category);
        return R.success(null,"添加成功");
    }

    @GetMapping("/page")
    public R<IPage> list(long page,long pageSize){
        IPage pageInfo=new Page(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo,"查询成功");
    }
    @PutMapping
    public R<Category> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success(null,"修改成功");
    }

    @DeleteMapping
    public R<Category> delete(long id){


        //todo   查看绑定关系
//        categoryService.removeById(id);
        categoryService.remove(id);


        return R.success(null,"删除分类成功");
    }

    @GetMapping("/list")
    public R<List<Category>> listType(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList,"获取成功");
    }
}

