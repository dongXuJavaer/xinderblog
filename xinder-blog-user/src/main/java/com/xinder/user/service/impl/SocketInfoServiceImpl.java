package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.SocketInfo;
import com.xinder.api.request.SocketInfoReq;
import com.xinder.api.response.dto.SocketInfoDtoResult;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.mapper.SocketInfoMapper;
import com.xinder.user.service.SocketInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * SocketInfo服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-13
 */
@Service
public class SocketInfoServiceImpl extends ServiceImpl<SocketInfoMapper, SocketInfo> implements SocketInfoService {

    @Resource
    private SocketInfoMapper socketInfoMapper;

    @Override
    public SocketInfoListDtoResult getSocketInfoList(SocketInfoReq socketInfoReq) {
        List<SocketInfo> socketInfos = socketInfoMapper.list(socketInfoReq);

        ArrayList<SocketInfoDtoResult> list = new ArrayList<>(socketInfos.size());
        socketInfos.forEach(item -> {
            SocketInfoDtoResult dto = DtoResult.dataDtoSuccess(SocketInfoDtoResult.class);
            BeanUtils.copyProperties(item, dto);
            list.add(dto);
        });
        SocketInfoListDtoResult dtoResult = DtoResult.dataDtoSuccess(SocketInfoListDtoResult.class);
        dtoResult.setList(list);
        return dtoResult;
    }

    @Override
    public Result addSocket(Integer fromUid, Integer toId, Integer type) {
        SocketInfoReq socketInfoReq = new SocketInfoReq()
                .setFromUid(fromUid)
                .setToId(toId)
                .setType(type);
        List<SocketInfo> list = socketInfoMapper.list(socketInfoReq);
        // 如果不存在这个聊天框则添加
        if (CollectionUtils.isEmpty(list)) {
            SocketInfo socketInfo = new SocketInfo();
            BeanUtils.copyProperties(socketInfoReq, socketInfo);
            int i = socketInfoMapper.insert(socketInfo);
            if (i <= 0) {
                return Result.fail("发起失败");
            }
        }
        return Result.success();
    }

    @Override
    public Result removeSocket(Long id) {
        int i = socketInfoMapper.deleteById(id);
        return i > 0 ? Result.success("关闭成功") : Result.fail("关闭失败");
    }
}
