package com.xinder.article.controller.admin;

import com.xinder.api.bean.Article;
import com.xinder.api.bean.RespBean;
import com.xinder.article.service.impl.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ArticleServiceImpl articleServiceImpl;

    /**
     * 管理端  博客管理
     * @param page
     * @param count
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByStateByAdmin(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "count", defaultValue = "6") Integer count,
            String keywords) {
        List<Article> articles = articleServiceImpl.getArticleByState(-2, page, count, keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("articles", articles);
        map.put("totalCount", articleServiceImpl.getArticleCountByState(-2,  keywords));
        return map;
    }

    @RequestMapping(value = "/article/dustbin", method = RequestMethod.PUT)
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleServiceImpl.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }
}
