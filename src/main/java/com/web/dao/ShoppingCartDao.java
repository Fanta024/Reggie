package com.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
