package com.web.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.common.CustomException;
import com.web.dao.SetmealDao;
import com.web.domain.Dish;
import com.web.domain.DishFlavor;
import com.web.domain.Setmeal;
import com.web.domain.SetmealDish;
import com.web.dto.SetmealDto;
import com.web.service.SetmealDishService;
import com.web.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal,"setmealDishes");

        save(setmeal);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    public SetmealDto getByIdWithDish(long id) {
        Setmeal setmeal = getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);


        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        updateById(setmeal);

        Long setmealId = setmeal.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        //select count(*) form xx  where id  in (ids)  and status=1

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        long count = count(setmealLambdaQueryWrapper);
        System.out.println(count);

        if (count>0){
            throw new CustomException("有套餐正在启售");
        }

        removeBatchByIds(ids);

        //delete * from xx where xxid in (ids)
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.removeBatchByIds(ids);

    }

    @Override
    public void updateStatus(Integer s,List<Long> ids) {

        //todo  s=0  判断是否有东西在售




        //update setmeal set status = !s where id in isd

//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(Setmeal::getId,ids);

        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaUpdateWrapper.in(Setmeal::getId,ids);
//        setmealLambdaUpdateWrapper.setSql("status = "+ s);
        setmealLambdaUpdateWrapper.set(Setmeal::getStatus,s);
        update(setmealLambdaUpdateWrapper);
    }


}
