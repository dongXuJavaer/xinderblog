package com.xinder.article.controller;

import com.xinder.api.bean.Article;
import com.xinder.api.response.RespBean;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.rest.ArticleApi;
import com.xinder.article.service.ArticleService;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.common.util.FileUtils;
import com.xinder.common.util.TokenDecode;
import com.xinder.article.service.impl.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    String accessKey = "0OHdkTxmT_nnOMhVVqSre9Kqo-sSPJYIu-0RhHdz";
    String secretKey = "tuHYk95xUWWstg7OPVQYDS290CkUA7ubLTUTVgIc";
    String bucket = "xinderblog";

    @Autowired
    ArticleServiceImpl articleServiceImpl;

    @Autowired
    ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenDecode tokenDecode;

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
     * 管理端，文章内容上传图片
     *
     * @return 返回值为图片的地址
     */
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public RespBean uploadImg(@RequestParam("file") MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_ING);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespBean("error", "上传失败!");
        }
        return new RespBean("success", url);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(
            @RequestParam(value = "state", defaultValue = "-1") Integer state,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "count", defaultValue = "6") Integer count,
            String keywords) {

        int totalCount = articleServiceImpl.getArticleCountByState(state, keywords);
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

    @Override
    public BaseResponse<ArticleListDtoResult> getArticleByUid(@RequestBody ArticleDtoReq articleDtoReq) {
        ArticleListDtoResult articleListDtoResult = articleService.getList(articleDtoReq);
        return buildJson(articleListDtoResult);
    }

    @Override
    public BaseResponse<String> uploadHeadPic(@RequestParam("file") MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_HEADPIC);
        } catch (Exception e) {
            e.printStackTrace();
            return buildJson(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc());
        }
        return buildJson(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), url);
    }

    @Override
    public BaseResponse<String> uploadAttachment(MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_ATTACHMENT);
        } catch (Exception e) {
            e.printStackTrace();
            return buildJson(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc());
        }
        return buildJson(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), url);
    }

    @Override
    public BaseResponse<String> publish(Article article) {

        return null;
    }


}
