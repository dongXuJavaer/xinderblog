package com.xinder.user.websocket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xinder.api.bean.*;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.enums.SocketMsgTypeEnums;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.common.constant.DateConstant;
import com.xinder.common.util.ContentFilterUtils;
import com.xinder.user.mapper.*;
import com.xinder.user.service.SocketInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author Xinder
 * @date 2023-02-14 17:37
 */
/*
    @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
    注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/websocket/{fromUid}/{toId}/{type}")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WebSocketService {

    private Integer fromUid;
    private Integer toId;
    private Session session;

    //用来存放每个客户端对应的MyWebSocket对象。 (方便用来群发？)
    private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    //用来记录uid和该session进行绑定
    private static Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * userId关联sid（解决同一用户id，在多个web端连接的问题）
     */
    public static Map<String, Set<String>> conns = new ConcurrentHashMap<>();

    private static MsgPrivateMapper msgPrivateMapper;
    private static MsgGroupMapper msgGroupMapper;
    private static TransactionTemplate transactionTemplate;
    private static UserMapper userMapper;
    private static GroupMapper groupMapper;
    private static SocketInfoService socketInfoService;
    private static GroupUserMapper groupUserMapper;


    public static void initBean(ApplicationContext applicationContext) {
        msgPrivateMapper = applicationContext.getBean(MsgPrivateMapper.class);
        msgGroupMapper = applicationContext.getBean(MsgGroupMapper.class);
        transactionTemplate = applicationContext.getBean(TransactionTemplate.class);
        userMapper = applicationContext.getBean(UserMapper.class);
        groupMapper = applicationContext.getBean(GroupMapper.class);
        socketInfoService = applicationContext.getBean(SocketInfoService.class);
        groupUserMapper = applicationContext.getBean(GroupUserMapper.class);
    }


    /**
     * 连接建立成功调用的方法
     * <p>
     * 1. 用户连接后，会根据用户id保存用户的session，方便后续给用户发消息
     * </p>
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("fromUid") Integer fromUid,
                       @PathParam("toId") Integer toId,
                       @PathParam("type") Integer type) {
        this.session = session;
        this.fromUid = fromUid;
        this.toId = toId;
        // 记录用户session
        sessionMap.put(this.fromUid, session);
        webSocketSet.add(this);
        log.info("有新连接加入:{},当前在线人数为{}", this, webSocketSet.size());
        Map<String, Object> message = buildConnectMsg(type);
        log.info("socket连接反馈消息{}", message);
        this.session.getAsyncRemote().sendText(JSON.toJSONString(message));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        System.out.println(this);
        webSocketSet.remove(this); //从set中删除
        log.info("有人断开连接，当前在线人数为{}", webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,
                          Session session,
                          @PathParam("fromUid") Integer fromUid,
                          @PathParam("toId") Integer toId) {
        SocketMsgReq socketMsgReq = JSON.parseObject(message, SocketMsgReq.class);
        // 过滤
        socketMsgReq.setContent(ContentFilterUtils.filter(socketMsgReq.getContent()));
        if (SocketMsgTypeEnums.MSG_PRIVATE.getCode().equals(socketMsgReq.getType())) {
            // ======> 发送私信消息
            sendPrivateMsg(socketMsgReq);
        } else if (SocketMsgTypeEnums.MSG_GROUP.getCode().equals(socketMsgReq.getType())) {
            // ======> 发送群聊消息
            sendGroupMsg(socketMsgReq);
        }
    }

    // 构建socket连接返回消息
    private Map<String, Object> buildConnectMsg(Integer type) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("isConn", true);  // 连接标识
        message.put("people", webSocketSet.size()); //在线人数
        message.put("fromUid", this.fromUid); //发送者id
        message.put("toId", this.toId); //接收者id
        message.put("aisle", this.session.getId()); //频道号
        if (SocketMsgTypeEnums.MSG_PRIVATE.getCode().equals(type)) {
            User toUser = userMapper.getUserById(Long.parseLong(toId.toString()));
            message.put("toName", toUser.getNickname()); //接收者名称
            message.put("toPic", toUser.getUserface()); //接收者头像
        } else {
            Group group = groupMapper.selectById(toId);
            message.put("toName", group.getName()); //接收者名称
            message.put("toPic", group.getPic()); //接收者头像
        }
        return message;
    }

    // 发送私信
    /*
        1. 信息入库
        2. 给发送者与接收者都转发消息
    */
    private void sendPrivateMsg(SocketMsgReq socketMsgReq) {
        MsgPrivate msgPrivate = new MsgPrivate()
                .setFromUid(socketMsgReq.getFromUid())
                .setContent(socketMsgReq.getContent())
                .setToUid(socketMsgReq.getToId())
                .setCreateTime(LocalDateTime.now());
        transactionTemplate.execute(status -> {
            int insert = msgPrivateMapper.insert(msgPrivate);
            MsgDtoResult msgDtoResult = new MsgDtoResult()
                    .setToId(msgPrivate.getToUid())
                    .setType(SocketMsgTypeEnums.MSG_PRIVATE.getCode());
            BeanUtils.copyProperties(msgPrivate, msgDtoResult);   // 主要是设置id、content、createTime
            log.info("返回消息{}", msgDtoResult);
            sendMsg(sessionMap.get(msgPrivate.getToUid()), msgDtoResult);
            sendMsg(session, msgDtoResult);
            // 打开接收者与发送者的socket小聊天列表
            socketInfoService.addSocket(socketMsgReq.getToId(), socketMsgReq.getFromUid(),
                    SocketMsgTypeEnums.MSG_PRIVATE.getCode());
            return insert > 0;
        });
    }

    /*
        1. 信息入库
        2. 给所有群成员转发消息
     */
    private void sendGroupMsg(SocketMsgReq socketMsgReq) {
        MsgGroup msgGroup = new MsgGroup()
                .setSendUid(socketMsgReq.getFromUid())
                .setContent(socketMsgReq.getContent())
                .setGroupId(socketMsgReq.getToId())
                .setCreateTime(LocalDateTime.now());
        Group group = groupMapper.selectById(msgGroup.getGroupId());
        transactionTemplate.execute(status -> {
            int insert = msgGroupMapper.insert(msgGroup);
            MsgDtoResult msgDtoResult = new MsgDtoResult()
                    .setFromUid(msgGroup.getSendUid())
                    .setToId(msgGroup.getGroupId())
                    .setType(SocketMsgTypeEnums.MSG_PRIVATE.getCode())
                    .setToName(group.getName());
            BeanUtils.copyProperties(msgGroup, msgDtoResult);
            // 找到所有群成员，给所有群成员发消息
            LambdaQueryWrapper<GroupUser> queryWrapper = new LambdaQueryWrapper<GroupUser>()
                    .eq(GroupUser::getGroupId, msgGroup.getGroupId())
                    .select(GroupUser::getUid);
            List<Integer> groupUserIdList = groupUserMapper.selectList(queryWrapper)
                    .stream()
                    .map(GroupUser::getUid).collect(Collectors.toList());
            groupUserIdList.add(group.getCreateUser());

            groupUserIdList.forEach(uid ->
                    Optional.ofNullable(sessionMap.get(uid))
                            .ifPresent(session -> sendMsg(session, msgDtoResult)));
            socketInfoService.addSocket(socketMsgReq.getToId(), socketMsgReq.getFromUid(),
                    SocketMsgTypeEnums.MSG_GROUP.getCode());
            return insert > 0;
        });
    }

    // 发送消息
    private void sendMsg(Session session, MsgDtoResult msgDtoResult) {
        Optional.ofNullable(session).ifPresent(item -> {
            User user = userMapper.getUserById(Long.parseLong(msgDtoResult.getFromUid().toString()));
            msgDtoResult.setCreateTimeStr(
                    DateFormatUtils.format(Date.from(msgDtoResult.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()),
                            DateConstant.YYYY_MM_DD_HH_MM_SS))
                    .setFromNickname(user.getNickname())
                    .setFromPic(user.getUserface()); // 这个参数主要是在群聊时使用
            session.getAsyncRemote().sendText(JSON.toJSONString(msgDtoResult));
        });
    }
}

