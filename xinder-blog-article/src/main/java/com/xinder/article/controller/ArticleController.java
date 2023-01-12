package com.xinder.article.controller;

import com.xinder.api.bean.Article;
import com.xinder.api.bean.RespBean;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.rest.ArticleApi;
import com.xinder.article.service.ArticleService;
import com.xinder.common.abstcontroller.AbstractController;
import org.apache.commons.io.IOUtils;
import com.xinder.article.service.impl.ArticleServiceImpl;
import com.xinder.common.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Xinder on 2023-1-6 23:43:03.
 */
@RequestMapping("/article")
@RestController
@CrossOrigin
public class ArticleController extends AbstractController implements ArticleApi {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    ArticleServiceImpl articleServiceImpl;

    @Autowired
    ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean addNewArticle(Article article) {
        int result = articleServiceImpl.addNewArticle(article);
        if (result == 1) {
            return new RespBean("success", article.getId() + "");
        } else {
            return new RespBean("error", article.getState() == 0 ? "文章保存失败!" : "文章发表失败!");
        }
    }

    /**
     * 上传图片
     *
     * @return 返回值为图片的地址
     */
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public RespBean uploadImg(HttpServletRequest req, MultipartFile image) {
        StringBuffer url = new StringBuffer();
        String filePath = "/blogimg/" + sdf.format(new Date());
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            url.append("/").append(imgName);
            return new RespBean("success", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespBean("error", "上传失败!");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "count", defaultValue = "6") Integer count, String keywords) {
        int totalCount = articleServiceImpl.getArticleCountByState(state, Util.getCurrentUser(redisTemplate).getId(), keywords);
        List<Article> articles = articleServiceImpl.getArticleByState(state, page, count, keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    //    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public Article getArticleById1(@PathVariable Long aid) {
        return articleServiceImpl.getArticleById(aid);
    }

    @RequestMapping(value = "/dustbin", method = RequestMethod.PUT)
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleServiceImpl.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping(value = "/restore", method = RequestMethod.PUT)
    public RespBean restoreArticle(Integer articleId) {
        if (articleServiceImpl.restoreArticle(articleId) == 1) {
            return new RespBean("success", "还原成功!");
        }
        return new RespBean("error", "还原失败!");
    }

    @RequestMapping("/dataStatistics")
    public Map<String, Object> dataStatistics() {
        Map<String, Object> map = new HashMap<>();
        List<String> categories = articleServiceImpl.getCategories();
        List<Integer> dataStatistics = articleServiceImpl.getDataStatistics();
        map.put("categories", categories);
        map.put("ds", dataStatistics);
        return map;
    }


    @Override
    public BaseResponse<ArticleListDtoResult> articleList(
            @RequestBody ArticleDtoReq articleDtoReq,
            @RequestParam(value = "keywords", required = false) String keywords) {


        ArticleListDtoResult articleListDtoResult = articleService.getArticleByState(articleDtoReq, keywords);
        return buildJson(articleListDtoResult);
    }

    @Override
    public BaseResponse<DtoResult> getArticleById(@PathVariable("aid") Long aid) {
        DtoResult result = articleService.getById(aid);
        return buildJson(result);
    }

}
