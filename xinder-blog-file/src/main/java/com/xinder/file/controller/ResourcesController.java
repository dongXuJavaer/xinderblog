package com.xinder.file.controller;

import com.xinder.api.bean.Resources;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.RespBean;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ResourcesListDtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.ResourcesApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.common.util.FileUtils;
import com.xinder.file.mapper.ResourcesMapper;
import com.xinder.file.service.ResourcesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Xinder
 * @date 2023-04-11 15:57
 */
@RestController
@RequestMapping("/resources")
public class ResourcesController extends AbstractController implements ResourcesApi {

    @Autowired
    private ResourcesService resourcesService;

    @Override
    public BaseResponse<String> uploadResources(MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_RESOURCE);
        } catch (Exception e) {
            e.printStackTrace();
            return buildJson(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc());
        }
        return buildJson(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), url);
    }

    @Override
    public BaseResponse<ResourcesListDtoResult> list(PageDtoReq pageDtoReq) {
        ResourcesListDtoResult listDtoResult = resourcesService.getPageList(pageDtoReq);
        return buildJson(listDtoResult);
    }

    @Override
    public void downloadResources(Long rid, Long uid) {
        resourcesService.downloadResources(rid, uid);
    }

    @Override
    public BaseResponse<Result> add(Resources resources) {
        Result result = resourcesService.add(resources);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> delete(Long rid) {
        boolean b = resourcesService.removeById(rid);
        Result result = b ? Result.success("删除成功") : Result.fail("删除失败");
        return buildJson(result);
    }

    @Override
    public Resources getById(Long rid) {
        Resources resources = resourcesService.getById(rid);
        return resources;
    }

}
