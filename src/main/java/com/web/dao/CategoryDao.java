package com.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.domain.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
}