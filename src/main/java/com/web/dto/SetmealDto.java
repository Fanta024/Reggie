package com.web.dto;

import com.web.domain.Setmeal;
import com.web.domain.SetmealDish;
import lombok.Data;

import java.util.List;
@Data
public class SetmealDto extends Setmeal {
  private List<SetmealDish> setmealDishes;

}
