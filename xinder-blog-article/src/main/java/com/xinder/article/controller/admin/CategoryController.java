package com.xinder.article.controller.admin;

import com.xinder.api.bean.Category;
import com.xinder.api.response.RespBean;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.CategoryListDtoResult;
import com.xinder.api.rest.CategoryApi;
import com.xinder.article.service.impl.CategoryServiceImpl;
import com.xinder.common.abstcontroller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 超级管理员专属Controller
 */
@RestController
public class CategoryController extends AbstractController implements CategoryApi {

    @Autowired
    CategoryServiceImpl categoryServiceImpl;

    @RequestMapping(value = "/admin/category{ids}", method = RequestMethod.DELETE)
    public RespBean deleteById(@PathVariable String ids) {
        boolean result = categoryServiceImpl.deleteCategoryByIds(ids);
        if (result) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping(value = "/admin/category", method = RequestMethod.POST)
    public RespBean addNewCate(Category category) {

        if ("".equals(category.getCateName()) || category.getCateName() == null) {
            return new RespBean("error", "请输入栏目名称!");
        }

        int result = categoryServiceImpl.addCategory(category);

        if (result == 1) {
            return new RespBean("success", "添加成功!");
        }
        return new RespBean("error", "添加失败!");
    }

    @RequestMapping(value = "/admin/category", method = RequestMethod.PUT)
    public RespBean updateCate(Category category) {
        int i = categoryServiceImpl.updateCategoryById(category);
        if (i == 1) {
            return new RespBean("success", "修改成功!");
        }
        return new RespBean("error", "修改失败!");
    }

    @Override
    public BaseResponse<CategoryListDtoResult> getAllCategories() {
        CategoryListDtoResult dtoResult = categoryServiceImpl.getAllCategories();
        return buildJson(dtoResult);
    }

}
