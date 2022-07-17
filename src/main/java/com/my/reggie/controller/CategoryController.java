package com.my.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.reggie.common.R;
import com.my.reggie.entity.Category;
import com.my.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /*
     * 新增菜品分类
     * */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增菜品分类成功");
    }

    /*
     * 菜品分类分页
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //根据sort列属性排序
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /*
     * 根据id删除菜品分类
     * */
    @DeleteMapping
    public R<String> delete(Long id) {

        //categoryService.removeById(ids);可能管理菜品和套餐，该方法慎用
        categoryService.remove(id);
        return R.success("删除菜品分类成功");
    }

    /*
    更新菜品类别
    * */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        //由于Category里加了注解,不需要设置修改时间，修改人的id等
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /*
     *菜品分类的数据
     * */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
