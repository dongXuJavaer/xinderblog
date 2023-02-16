package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.MsgPrivate;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.user.mapper.MsgPrivateMapper;
import com.xinder.user.service.MsgPrivateService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-15
 */
@Service
public class MsgPrivateServiceImpl extends ServiceImpl<MsgPrivateMapper, MsgPrivate> implements MsgPrivateService {

}
