package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xinder.api.bean.MsgGroup;
import com.xinder.api.bean.MsgPrivate;
import com.xinder.api.bean.User;
import com.xinder.api.enums.SocketMsgTypeEnums;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.api.response.dto.MsgDtoResultList;
import com.xinder.api.response.result.DtoResult;
import com.xinder.common.constant.CommonConstant;
import com.xinder.common.constant.DateConstant;
import com.xinder.user.mapper.MsgGroupMapper;
import com.xinder.user.mapper.MsgPrivateMapper;
import com.xinder.user.mapper.UserMapper;
import com.xinder.user.service.MsgService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    private void initMsg(){
        List<MsgGroup> msgGroupList = msgGroupMapper.selectList(null);
        Map<Integer, List<MsgGroup>> msgGroupMap = new HashMap<>();
        msgGroupList.forEach(msgGroup -> {
            List<MsgGroup> list = msgGroupMap.computeIfAbsent(msgGroup.getGroupId(), k -> new ArrayList<>());

            User fromUser = userMapper.getUserById(msgGroup.getSendUid().longValue());
            fromUser = fromUser != null ? fromUser : new User().setNickname("（用户已删除）");
            msgGroup.setFromNickname(fromUser.getNickname())
                    .setFromPic(fromUser.getUserface());
            list.add(msgGroup);
        });
        msgGroupMap.forEach((k,v)-> redisTemplate.opsForValue().set(CommonConstant.REDIS_KEY_MSG_GROUP + ":" + k , v));
        // TODO: 2023-04-30 私信消息存入redis
//        List<MsgPrivate> msgPrivates = msgPrivateMapper.selectList(null);
//        redisTemplate.opsForValue().set

    }

    @Override
    public MsgDtoResultList getMsgList(SocketMsgReq socketMsgReq) {
        MsgDtoResultList msgDtoResultList = DtoResult.dataDtoSuccess(MsgDtoResultList.class);
        // 根据私信消息或者群聊消息判断
        if (SocketMsgTypeEnums.MSG_PRIVATE.getCode().equals(socketMsgReq.getType())) {
            List<MsgPrivate> msgPrivateList = msgPrivateMapper.selectSocketMsgList(socketMsgReq);
            msgDtoResultList.setList(buildMsgPrivateDtoList(msgPrivateList));
        } else if (SocketMsgTypeEnums.MSG_GROUP.getCode().equals(socketMsgReq.getType())) {
            List<MsgGroup> msgGroupList =
                    (List<MsgGroup>)redisTemplate.opsForValue().get(CommonConstant.REDIS_KEY_MSG_GROUP + ":" + socketMsgReq.getToId());
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
        if (CollectionUtils.isEmpty(msgGroupList)){
            return null;
        }
        List<MsgDtoResult> list = new ArrayList<>(msgGroupList.size());
        msgGroupList.forEach(msgGroup -> {
            MsgDtoResult msgDtoResult = DtoResult.dataDtoSuccess(MsgDtoResult.class);
            BeanUtils.copyProperties(msgGroup, msgDtoResult);
//            User fromUser = userMapper.getUserById(msgGroup.getSendUid().longValue());
//            fromUser = fromUser != null ? fromUser : new User().setNickname("（用户已删除）");
            msgDtoResult
                    .setFromUid(msgGroup.getSendUid())
//                    .setFromNickname(fromUser.getNickname())
//                    .setFromPic(fromUser.getUserface())
                    .setToId(msgGroup.getGroupId())
                    .setCreateTimeStr(
                            DateFormatUtils.format(Date.from(msgDtoResult.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()),
                                    DateConstant.YYYY_MM_DD_HH_MM_SS)
                    );
            list.add(msgDtoResult);
        });
        return list;
    }
}
