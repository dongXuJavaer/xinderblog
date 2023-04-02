package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.enums.PointEnums;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.mapper.PointInfoMapper;
import com.xinder.user.service.PointInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
@Service
public class PointInfoServiceImpl extends ServiceImpl<PointInfoMapper, PointInfo> implements PointInfoService {

    @Autowired
    private PointInfoMapper pointInfoMapper;

    @Override
    public Result add(PointInfo pointInfo) {
        int i = pointInfoMapper.insert(pointInfo);
        return i > 0 ? Result.success("成功") : Result.fail("失败");
    }

    @Override
    public PointInfoListDtoResult getListByUid(Long uid, PageDtoReq req) {

        Integer pageSize = req.getPageSize();
        Long currentPage = req.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        Long rows = pointInfoMapper.getCount(uid);
        List<PointInfo> pointInfoList = pointInfoMapper.getPointInfoList(req, offset, uid);
        PointInfoListDtoResult dtoListResult = DtoResult.dataDtoSuccess(PointInfoListDtoResult.class);

        long totalPage = Double.valueOf(Math.ceil((float) rows / (float) pageSize)).longValue();
        dtoListResult.setTotalCount(rows);
        dtoListResult.setTotalPage(totalPage);
        dtoListResult.setCurrentPage(currentPage);
        dtoListResult.setList(pointInfoList);

        return dtoListResult;
    }

    @Override
    public Result reduce(PointInfo pointInfo) {
        LambdaQueryWrapper<PointInfo> wrapper = new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getUid, pointInfo.getUid());
        List<PointInfo> pointInfoList = pointInfoMapper.selectList(wrapper);
        int count = 0;
        for (PointInfo info : pointInfoList) {
            count += info.getPoint();
            if (count < 0) {
                return Result.fail("积分不足");
            }
        }
        pointInfo.setType(PointEnums.RETUCE.getCode());
        pointInfoMapper.insert(pointInfo);
        return Result.success("成功");
    }
}
