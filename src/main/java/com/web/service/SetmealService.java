package com.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.domain.Setmeal;
import com.web.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    SetmealDto getByIdWithDish(long id);

    void updateWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);

    void updateStatus(Integer s,List<Long> ids);
}
