package com.xinder.file.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Resources;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.dto.ResourcesListDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.common.util.Util;
import com.xinder.file.feign.PointInfoFeignClient;
import com.xinder.file.feign.UserFeignClient;
import com.xinder.file.mapper.ResourcesMapper;
import com.xinder.file.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
@Service
@Slf4j
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements ResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private PointInfoFeignClient pointInfoFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public void downloadResources(Long rid, Long uid) {
        Resources resources = resourcesMapper.selectById(rid);

//        // 查询用户是否下载过
//        BaseResponse<DtoResult> dtoResultBaseResponse = pointInfoFeignClient.getByUidAndRid(uid, rid);
//        if (!dtoResultBaseResponse.isSuccess()) {
//            // 如果用户没有下载过
//            BaseResponse<DtoResult> baseResponse = pointInfoFeignClient.getPointCount(uid);
//            Integer count = (Integer) baseResponse.getData().getData();
//
//        }

//        String urlStr = resources.getUrl();
        resources.setCount(resources.getCount() + 1);
        log.info("请求下载");
        String urlStr = "http://img.blog.xinder.top/blog/attachment/2023-02-19---631bfd17-31b0-42c0-9333-d52e7e83c7f6_8C9ADDAC68B188C0BB5403F0444E3BA7_1676166839224.zip";
        downLoad(urlStr, resources.getName() + "_" + resources.getCreateTime());

    }

    @Override
    public Result add(Resources resources) {

        boolean flag = false;
        if (resources.getId() == null) {
            // 上传
            resources.setCount(0)
                    .setCreateTime(new Date());
            flag = this.save(resources);
        } else {
            // 修改
            flag = resourcesMapper.updateById(resources) > 0;
        }

        return flag ? Result.success("成功") : Result.fail("失败");
    }

    private void downLoad(String urlStr, String filename) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ServletOutputStream servletOutputStream = null;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setContentType(conn.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "utf-8"));

            //将输入流的事件写出到输出事件
            servletOutputStream = response.getOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = 0;
            while ((length = inStream.read(buffer)) != -1) {
                servletOutputStream.write(buffer, 0, length);
            }
            System.out.println("下载完成");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                servletOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResourcesListDtoResult getPageList(PageDtoReq req, String uidStr) {

        Integer pageSize = req.getPageSize();
        Long currentPage = req.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        Long uid = null;
        if (!"null".equals(uidStr) && uidStr != null) {
            uid = Long.parseLong(uidStr);
        }
        Long rows = resourcesMapper.getCount(uid);
        List<Resources> resourcesList = resourcesMapper.pageList(req, offset, uid);

        long totalPage = Double.valueOf(Math.ceil((float) rows / (float) pageSize)).longValue();
        ResourcesListDtoResult dtoListResult = DtoResult.dataDtoSuccess(ResourcesListDtoResult.class);
        dtoListResult.setTotalCount(rows);
        dtoListResult.setTotalPage(totalPage);
        dtoListResult.setCurrentPage(currentPage);
        dtoListResult.setList(resourcesList);
        return dtoListResult;
    }
}
