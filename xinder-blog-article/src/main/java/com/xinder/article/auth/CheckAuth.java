package com.xinder.article.auth;

import com.alibaba.fastjson.JSONArray;
import com.xinder.api.enums.PermissionsEnums;
import com.xinder.api.enums.ResultCode;
import com.xinder.common.constant.CommonConstant;
import com.xinder.common.exception.CheckAuthException;
import com.xinder.common.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 权限校验
 *
 * @author Xinder
 * @date 2023-02-02 16:25
 */
@Component
public class CheckAuth extends com.xinder.common.auth.CheckAuth {


}
