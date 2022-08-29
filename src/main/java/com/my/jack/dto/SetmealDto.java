package com.my.jack.dto;

import com.my.jack.entity.Setmeal;
import com.my.jack.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
