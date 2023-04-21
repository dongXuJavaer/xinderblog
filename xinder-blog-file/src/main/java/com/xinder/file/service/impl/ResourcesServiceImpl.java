package com.xinder.file.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.bean.Resources;
import com.xinder.api.enums.PointEnums;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ResourcesListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.file.feign.PointInfoFeignClient;
import com.xinder.file.feign.UserFeignClient;
import com.xinder.file.mapper.ResourcesMapper;
import com.xinder.file.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void downloadResources(Long rid, Long uid, HttpServletResponse response) {
        Resources resources = resourcesMapper.selectById(rid);

        // 查询用户是否下载过, 查询成功是下载过，没有查询成功则是没有下载过
        // 没有下载过则需要扣减积分（扣减积分的记录就是下载记录）
        BaseResponse<DtoResult> dtoResultBaseResponse = pointInfoFeignClient.getByUidAndRid(uid, rid);
        if (!dtoResultBaseResponse.isSuccess()) {

            PointInfo pointInfo = new PointInfo()
                    .setPoint(resources.getPoint())
                    .setType(PointEnums.RETUCE.getCode())
                    .setContent("下载资源，扣减积分")
                    .setRid(rid)
                    .setUid(uid);

            double downPoint = resources.getCount().doubleValue();
            int num = (int) (downPoint* 0.6 + 0.5);
            System.out.println("-----" + num);
            PointInfo addPoint = new PointInfo()
                    .setUid(resources.getUid())
                    .setRid(rid)
                    .setContent("资源被别人下载，活获得 60% 奖励")
                    .setType(PointEnums.ADD.getCode())
                    .setPoint(num);

            AtomicBoolean reducePointFlag = new AtomicBoolean(false); // 扣减积分标志
            transactionTemplate.executeWithoutResult(status -> {
                BaseResponse<Result> reduceBaseResponse = pointInfoFeignClient.reduce(pointInfo);
                if (reduceBaseResponse.isSuccess()) {
                    reducePointFlag.set(true);

                    // 给资源拥有者增加积分
                    pointInfoFeignClient.add(addPoint);
                    // 下载次数+1
                    resources.setCount(resources.getCount() + 1);
                    resourcesMapper.updateById(resources);
                }
            });

            // 扣减积分不成功，不能下载
            if (!reducePointFlag.get()) {
                return;
            }
        }

        String urlStr = resources.getUrl();
        resources.setCount(resources.getCount() + 1);
//        String urlStr = "http://img.blog.xinder.top/blog/attachment/2023-02-19---631bfd17-31b0-42c0-9333-d52e7e83c7f6_8C9ADDAC68B188C0BB5403F0444E3BA7_1676166839224.zip";

        String filename = resources.getName() + "_" + new Date().getTime();
        filename = filename.replaceAll(" ", "_");
        // 获取文件后缀
        String[] split = urlStr.split("\\.");
        String suffix = "." + split[split.length - 1];

        downLoad(urlStr, filename + suffix, response);

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

    /**
     * 下载文件
     *
     * @param urlStr   文件地址
     * @param filename 文件名
     * @param response 响应
     */
    private void downLoad(String urlStr, String filename, HttpServletResponse response) {
        URL url = null;
        ServletOutputStream servletOutputStream = null;

        log.info("请求下载");
        try {
            url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

            response.setContentType(conn.getContentType());
//            response.setContentType("application/octet-stream;charset=gb2312");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
//            response.setHeader("Content-Disposition", "inline;filename=" + filename);

            //将输入流的事件写出到输出事件
            servletOutputStream = response.getOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int length = 0;
            while ((length = inStream.read(buffer)) != -1) {
                servletOutputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                servletOutputStream.flush();
                System.out.println("下载完成");
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
