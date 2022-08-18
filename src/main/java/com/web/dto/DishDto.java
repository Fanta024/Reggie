package com.web.dto;

import com.web.domain.Dish;
import com.web.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors=new ArrayList<>();
    private String categoryName;
}
