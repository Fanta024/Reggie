package com.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.domain.Category;

public interface CategoryService extends IService<Category> {
    public void remove(long id);

}
