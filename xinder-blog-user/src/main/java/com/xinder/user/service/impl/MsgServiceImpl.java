package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xinder.api.bean.MsgGroup;
import com.xinder.api.bean.MsgPrivate;
import com.xinder.api.enums.SocketMsgTypeEnums;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.api.response.dto.MsgDtoResultList;
import com.xinder.api.response.result.DtoResult;
import com.xinder.common.constant.DateConstant;
import com.xinder.user.mapper.MsgGroupMapper;
import com.xinder.user.mapper.MsgPrivateMapper;
import com.xinder.user.service.MsgService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-16 16:02
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private MsgPrivateMapper msgPrivateMapper;

    @Autowired
    private MsgGroupMapper msgGroupMapper;


    @Override
    public MsgDtoResultList getMsgList(SocketMsgReq socketMsgReq) {
        MsgDtoResultList msgDtoResultList = DtoResult.dataDtoSuccess(MsgDtoResultList.class);
        // 根据私信消息或者群聊消息判断
        if (SocketMsgTypeEnums.MSG_PRIVATE.getCode().equals(socketMsgReq.getType())) {
            List<MsgPrivate> msgPrivateList = msgPrivateMapper.selectSocketMsgList(socketMsgReq);
            msgDtoResultList.setList(buildMsgPrivateDtoList(msgPrivateList));
        } else if (SocketMsgTypeEnums.MSG_GROUP.getCode().equals(socketMsgReq.getType())) {
            MsgGroup msgGroup = new MsgGroup()
                    .setSendUid(socketMsgReq.getFromUid())
                    .setGroupId(socketMsgReq.getToId());
            BeanUtils.copyProperties(socketMsgReq, msgGroup);
            LambdaUpdateWrapper<MsgGroup> wrapper = new LambdaUpdateWrapper<MsgGroup>()
                    .eq(MsgGroup::getSendUid, socketMsgReq.getFromUid())
                    .eq(MsgGroup::getGroupId, socketMsgReq.getToId());
            List<MsgGroup> msgGroupList = msgGroupMapper.selectList(wrapper);
            msgDtoResultList.setList(buildMsgGroupDtoList(msgGroupList));
        }
        return msgDtoResultList;
    }

    // 构建私信消息dto列表
    private List<MsgDtoResult> buildMsgPrivateDtoList(List<MsgPrivate> msgPrivateList) {
        List<MsgDtoResult> list = new ArrayList<>(msgPrivateList.size());
        msgPrivateList.forEach(item -> {
            MsgDtoResult msgDtoResult = DtoResult.dataDtoSuccess(MsgDtoResult.class);
            BeanUtils.copyProperties(item, msgDtoResult);
            msgDtoResult.setToId(item.getToUid());
            msgDtoResult.setCreateTimeStr(
                    DateFormatUtils.format(Date.from(msgDtoResult.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()),
                            DateConstant.YYYY_MM_DD_HH_MM_SS)
            );
            list.add(msgDtoResult);
        });
        return list;
    }

    // 构建群聊消息dto列表
    private List<MsgDtoResult> buildMsgGroupDtoList(List<MsgGroup> msgGroupList) {
        List<MsgDtoResult> list = new ArrayList<>(msgGroupList.size());
        msgGroupList.forEach(item -> {
            MsgDtoResult msgDtoResult = DtoResult.dataDtoSuccess(MsgDtoResult.class);
            BeanUtils.copyProperties(item, msgDtoResult);
            msgDtoResult.setFromUid(item.getSendUid())
                    .setToId(item.getGroupId());
            msgDtoResult.setCreateTimeStr(
                    DateFormatUtils.format(Date.from(msgDtoResult.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()),
                            DateConstant.YYYY_MM_DD_HH_MM_SS)
            );
            list.add(msgDtoResult);
        });
        return list;
    }
}
