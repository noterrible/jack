package com.my.reggie.dto;

import com.my.reggie.entity.Setmeal;
import com.my.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
