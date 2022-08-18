package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.ShoppingCartDao;
import com.web.domain.ShoppingCart;
import com.web.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
}
