package com.xinder.api.rest;

import com.xinder.api.bean.Group;
import com.xinder.api.bean.GroupUser;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.GroupDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xinder
 * @date 2023-02-11 13:48
 */
@Api(tags = "GroupApi")
@RequestMapping("/group")
public interface GroupApi {

    /**
     * 创建的群聊列表
     * @param group
     * @return
     */
    @RequestMapping("/list")
    BaseResponse<GroupDtoListResult> list(@RequestBody Group group);

    /**
     * 用户加入群聊
     * @param groupUser
     * @return
     */
    @RequestMapping("/user/add")
    BaseResponse<Result> userAdd(@RequestBody GroupUser groupUser);

    /**
     * 用户退出群聊
     * @param groupUser
     * @return
     */
    @RequestMapping("/user/quit")
    BaseResponse<Result> userQuit(@RequestBody GroupUser groupUser);

    /**
     * 用户加入的群聊列表
     * @param uid
     * @return
     */
    @RequestMapping("/user/add/list/{uid}")
    BaseResponse<GroupDtoListResult> userAddList(@PathVariable("uid") Long uid);

    /**
     * 上传群头像
     * @return
     */
    @RequestMapping("/upload/head")
    @ApiOperation(value = "上传头像", notes = "上传头像", tags = {"GroupApi"})
    BaseResponse<String> uploadHeadPic(@RequestParam("file") MultipartFile multipartFile);

    /**
     * 创建群聊
     * @return
     */
    @RequestMapping("/add")
    @ApiOperation(value = "创建群聊", notes = "创建群聊", tags = {"GroupApi"})
    BaseResponse<Result> createGroup(@RequestBody Group group);

    /**
     * 根据id获取群信息
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "根据id获取群信息", notes = "根据id获取群信息", tags = {"GroupApi"})
    BaseResponse<DtoResult> getGroupById(@PathVariable("id") Integer id);

    /**
     * 修改群聊
     * @return
     */
    @RequestMapping("/update")
    @ApiOperation(value = "修改群聊", notes = "修改群聊", tags = {"GroupApi"})
    BaseResponse<Result> updateGroup(@RequestBody Group group);

    /**
     * 解散群聊
     * @return
     */
    @RequestMapping("/disband/{id}")
    @ApiOperation(value = "解散群聊", notes = "解散群聊", tags = {"GroupApi"})
    BaseResponse<Result> disband(@PathVariable("id") Integer id);
}
