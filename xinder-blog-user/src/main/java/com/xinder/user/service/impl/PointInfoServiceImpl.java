package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.enums.PointEnums;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.mapper.PointInfoMapper;
import com.xinder.user.service.PointInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
        PointInfo now = pointInfoMapper.selectNow(pointInfo);
        if (now != null) {
            return Result.fail("当日已签到");
        }

        // 近七天的签到记录
        List<PointInfo> pointInfoList = null;
        // 从近1天开始查询，判断是否连续签到
        for (int i = 1; i <= 7; i++) {
            List<PointInfo> list = pointInfoMapper.selectNearDay(pointInfo, i);
            // 前一天没有签到时，查询为空的情况
            if (list == null) {
                break;
            }
            // 查询到的签到记录不是连续时
            if (list.size() != i) {
                break;
            }
            // 记录上一次的循环结果
            pointInfoList = list;
        }
        // 是连续签到
        if (!CollectionUtils.isEmpty(pointInfoList)) {
            PointInfo info = pointInfoList.get(pointInfoList.size() - 1);
            String str = info.getContent();
            String day = str.substring(2, 3);
            int dayInt = Integer.parseInt(day);
            pointInfo.setContent("签到" + (dayInt + 1) + "天");
            if (dayInt >= 7) {
                pointInfo.setPoint(7);
            } else {
                pointInfo.setPoint(dayInt + 1);
            }
        } else {
            // 不是连续签到
            pointInfo.setContent("签到1天");
            pointInfo.setPoint(1);
        }
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

    @Override
    public DtoResult getByUidAndRid(Long uid, Long rid) {

        LambdaQueryWrapper<PointInfo> wrapper = new LambdaQueryWrapper<PointInfo>()
                .eq(PointInfo::getUid, uid)
                .eq(PointInfo::getRid, rid);

        PointInfo pointInfo = pointInfoMapper.selectOne(wrapper);
        DtoResult dtoResult = null;
        if (pointInfo != null) {
            dtoResult = DtoResult.success();
            dtoResult.setData(pointInfo);
        } else {
            // 未查询到，则返回失败
            dtoResult = DtoResult.fail(ResultCode.FAIL);
        }
        return dtoResult;
    }

    @Override
    public DtoResult getPointCount(Long uid) {
        List<PointInfo> pointInfoList = pointInfoMapper.getPointInfoByUid(uid);
        int count = 0;
        for (PointInfo pointInfo : pointInfoList) {
            if (PointEnums.ADD.getCode() == pointInfo.getType()) {
                count += pointInfo.getPoint();
            } else {
                count -= pointInfo.getPoint();
            }
        }
        DtoResult dtoResult = DtoResult.success();
        dtoResult.setData(count);
        return dtoResult;
    }
}
