package com.xinder.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.History;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.enums.PointEnums;
import com.xinder.api.enums.QQLoginEnums;
import com.xinder.api.enums.UserEnums;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.HistoryListDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoResult;
import com.xinder.api.response.dto.qqlogin.IdsDto;
import com.xinder.api.response.dto.qqlogin.QQUserDto;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.common.constant.CommonConstant;
import com.xinder.common.util.TokenDecode;
import com.xinder.user.feign.HistoryFeignClient;
import com.xinder.user.mapper.PointInfoMapper;
import com.xinder.user.mapper.RolesMapper;
import com.xinder.user.mapper.UserMapper;
import com.xinder.common.util.AuthToken;
import com.xinder.common.util.CookieUtils;
import com.xinder.common.util.Util;
import com.xinder.user.service.AuthService;
import com.xinder.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xinder on 2022/01/17.
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private HistoryFeignClient historyFeignClient;

    @Autowired
    private PointInfoMapper pointInfoMapper;


    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;


//    @Autowired
//    private AbstractTokenGranter abstractTokenGranter;

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(User user) {
        User loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);//用户可用
        long result = userMapper.reg(user);
        //配置用户的角色，默认都是普通用户
        String[] roles = new String[]{"2"};
        int i = rolesMapper.addRoles(roles, user.getId());
        boolean b = i == roles.length && result == 1;
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, Util.getCurrentUser(tokenDecode, redisTemplate).getId());
    }

    /**
     * 【管理端】  查询用户
     *
     * @param nickname
     * @return
     */
    public UserListDtoResult getUserByNickname(String nickname) {
        List<User> userList = userMapper.getUserByNickname(nickname);
        List<UserDtoResult> userDtoResultList = new ArrayList<>(userList.size());
        userList.forEach(item -> {
            UserDtoResult userDtoResult = DtoResult.dataDtoSuccess(UserDtoResult.class);
            BeanUtils.copyProperties(item, userDtoResult);
            userDtoResultList.add(userDtoResult);
        });

        UserListDtoResult dtoResult = DtoResult.dataDtoSuccess(UserListDtoResult.class);
        dtoResult.setList(userDtoResultList);
        return dtoResult;
    }

    public List<Role> getAllRole() {
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long uid) {
        return userMapper.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Long uid) {
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids, Long id) {
        int i = userMapper.deleteUserRolesByUid(id);
        return userMapper.setUserRoles(rids, id);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }


    @Override
    public UserDtoSimpleResult getUserByIdFront(Long uid) {
        User user = userMapper.getUserById(uid);
        UserDtoSimpleResult dtoSimpleResult = DtoResult.dataDtoSuccess(UserDtoSimpleResult.class);
        BeanUtils.copyProperties(user, dtoSimpleResult);
        return dtoSimpleResult;
    }

    @Override
    public UserDtoResult login(String username, String password, HttpServletResponse response) {
        UserDtoResult userDtoResult = DtoResult.dataDtoFail(UserDtoResult.class);
        //判断用户是否为空
        if (StringUtils.isEmpty(username)) {
            userDtoResult.setMsg("用户名不能为空");
            return userDtoResult;
        }
        //判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            userDtoResult.setMsg("密码不能为空");
            return userDtoResult;
        }

        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        if (authToken.getError() == null) {
            User user = userMapper.loadUserByUsername(username);
            redisTemplate.opsForValue().set(UserEnums.USER_ONLINE_PREFIX_KEY.getValue() + user.getUsername(),
                    user, Duration.ofMinutes(30));
            log.info("登录用户 {} 信息存入redis", user.getUsername());
            userDtoResult = DtoResult.dataDtoSuccess(UserDtoResult.class);
            BeanUtils.copyProperties(user, userDtoResult);

            syncHistoryList(response, user.getId());

            response.addHeader("Token", authToken.getAccessToken());
            CookieUtils.addCookie(response, cookieDomain,
                    "/", "Authorization", authToken.getAccessToken(), cookieMaxAge, false);
        } else {
            log.info("登录用户 {} 密码错误", username);
            userDtoResult.setMsg(authToken.getErrorDescription());
        }


        return userDtoResult;
    }

    @Override
    public UserDtoResult qqLogin(String token, HttpServletResponse response) {
        if (StringUtils.isEmpty(token) || "null".equals(token)) {
            return DtoResult.dataDtoFail(UserDtoResult.class);
        }
        IdsDto idsDto = this.getOpenId(token);
        User user = userMapper.selectByOpenid(idsDto.getOpenId());
        if (user == null) {
            // 未注册的用户，QQ登录自动注册
            QQUserDto qqUserDto = this.getUserInfo(idsDto, token);
            user = new User();

            user.setNickname(qqUserDto.getNickname())
                    .setGender(qqUserDto.getGender_type())
                    .setUserface(qqUserDto.getFigureurl_qq_2())
                    .setOpenid(idsDto.getOpenId())
                    .setUsername(idsDto.getOpenId()) // 使用qq注册，username的默认值为openid
                    .setPassword(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()))
            ;
            User finalUser = user;
            int count = transactionTemplate.execute(status -> {
                int insert = userMapper.insert(finalUser);
                return insert;
            });
        }
        UserDtoResult userDtoResult = this.login(user.getUsername(), user.getPassword(), response);
//        Optional.ofNullable(user).orElse()
        return userDtoResult;
    }

    /**
     * 主要是获取openId
     *
     * @param token
     * @return
     */
    private IdsDto getOpenId(String token) {
        String url = QQLoginEnums.OPENID_URL.getValue() + "?access_token={token}&unionid=1&fmt=json";
        String ids = restTemplate.getForObject(url, String.class, token);
        IdsDto idsDto = JSONObject.parseObject(ids, IdsDto.class);
        return idsDto;
    }

    /**
     * 根据openid和token请求qq，获取用户信息
     *
     * @param idsDto
     * @param token
     * @return
     */
    private QQUserDto getUserInfo(IdsDto idsDto, String token) {
        String url = QQLoginEnums.USERINFO_URL.getValue()
                + "?access_token={token}&oauth_consumer_key={clientId}&openid={openid}";
        String userInfo = restTemplate.getForObject(url, String.class,
                token, idsDto.getClientId(), idsDto.getOpenId());
        QQUserDto qqUserDto = JSONObject.parseObject(userInfo, QQUserDto.class);
        return qqUserDto;
    }

    @Override
    public UserDtoResult getCurrentUser() {
        User user = Util.getCurrentUser(tokenDecode, redisTemplate);
        UserDtoResult userDtoResult = DtoResult.dataDtoSuccess(UserDtoResult.class);
        BeanUtils.copyProperties(user, userDtoResult);
        return userDtoResult;
    }

    @Override
    public DtoResult logout() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        session.invalidate();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtils.deleteCookie(response, cookieDomain,
                "/", "Authorization", false);
        return DtoResult.success();
    }

    @Override
    @Transactional
    public Result updateUserInfo(UserDtoReq userDtoReq) {
        Long id = userDtoReq.getId();
        String username = userDtoReq.getUsername();
        String userKey = UserEnums.USER_ONLINE_PREFIX_KEY.getValue() + username;
        User user = userMapper.selectById(id);
        BeanUtils.copyProperties(userDtoReq, user);
        transactionTemplate.execute(status -> {
            redisTemplate.opsForValue().set(userKey, user, 30, TimeUnit.MINUTES);
            int i = userMapper.updateById(user);
            // TODO: 2023-03-10 修改个人账号后，token里面的username要同步修改
            return i;
        });

        return Result.success();
    }

    @Override
    public Result uploadHeadImg(MultipartFile file) {
        return null;
    }

    @Override
    public DtoResult getUserCount() {
        int count = count();
        DtoResult result = DtoResult.success();
        result.setData(count);
        return result;
    }

    @Override
    public String getCookieDomain() {
        return cookieDomain;
    }


    /**
     * 同步浏览历史记录
     *
     * @param response 响应
     */
    private void syncHistoryList(HttpServletResponse response, Long uid) {
        List<History> historyList = Util.getHistoryListByCookie();
        if (!CollectionUtils.isEmpty(historyList)) {
            historyFeignClient.batchSave(historyList, uid);
            CookieUtils.deleteCookie(response, cookieDomain, "/", CommonConstant.HTTP_HEADER_HISTORY, false);
        }
    }

    @Override
    public Result register(UserDtoReq userDtoReq) {
        if (userDtoReq == null) {
            return Result.fail("数据为空");
        }
        if (userDtoReq.getUsername() == null) {
            return Result.fail("用户名不能为空");
        }
        if (userDtoReq.getNickname() == null) {
            return Result.fail("昵称不能为空");
        }
        if (userDtoReq.getPassword() == null) {
            return Result.fail("密码不能为空");
        }
        String encode = new BCryptPasswordEncoder().encode(userDtoReq.getPassword());
        User user = new User()
                .setUsername(userDtoReq.getUsername())
                .setPassword(encode)
                .setNickname(userDtoReq.getNickname())
                .setEnabled(1)
                .setGender(1);


        userMapper.insert(user);
        PointInfo pointInfo = new PointInfo().setContent("注册赠送").setPoint(30).setType(PointEnums.ADD.getCode()).setUid(user.getId());
        pointInfoMapper.insert(pointInfo);

        return Result.success("注册成功");
    }
}
