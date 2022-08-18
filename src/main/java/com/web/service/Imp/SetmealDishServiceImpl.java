package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.SetmealDishDao;
import com.web.domain.SetmealDish;
import com.web.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishDao, SetmealDish> implements SetmealDishService {
}
