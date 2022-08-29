package com.my.jack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.jack.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
