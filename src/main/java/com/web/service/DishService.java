package com.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.domain.Dish;
import com.web.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    /**
     *  储存菜单和口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(long id);

    List<DishDto> listWithFlavor(Dish dish);

    void updateStatus(Integer s, List<Long> ids);
}
