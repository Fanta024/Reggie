package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.DishFlavorDao;
import com.web.domain.DishFlavor;
import com.web.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {
}
