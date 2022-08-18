package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.web.common.BaseContext;
import com.web.common.R;
import com.web.domain.ShoppingCart;
import com.web.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    R<List<ShoppingCart>> shoppingCar(){
        long userId = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list,"");
    }

    @PostMapping("/add")
    R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //TODO 口味会不同
        //获取当前用户id
        long userId = BaseContext.getId();
        shoppingCart.setUserId(userId);

        //查询添加的物品是否在购物车   count

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);
        if(shoppingCart.getDishId()!=null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if (cart!=null){
            cart.setNumber(cart.getNumber()+1);
            shoppingCartService.updateById(cart);
        }else {
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }

        return R.success(cart,"");
    }

    @DeleteMapping("/clean")
    public R<String> cleanCart(){
        long userId = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(queryWrapper);
        return R.success(null,"");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        long userId = BaseContext.getId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        if (shoppingCart.getDishId()!=null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());

        }
        else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if(one.getNumber()==1){
            shoppingCartService.removeById(one);
            one=null;
        }else {
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);

        }

        return R.success(one,"删除成功");
    }
}
