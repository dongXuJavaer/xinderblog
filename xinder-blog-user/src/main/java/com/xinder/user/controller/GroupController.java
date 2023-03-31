package com.xinder.user.controller;


import com.xinder.api.bean.Group;
import com.xinder.api.bean.GroupUser;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.GroupDtoListResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.FollowApi;
import com.xinder.api.rest.GroupApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.common.util.FileUtils;
import com.xinder.user.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xinder
 * @since 2023-02-11
 */
@RestController
@RequestMapping("/group")
public class GroupController extends AbstractController implements GroupApi {

    @Autowired
    private GroupService groupService;


    @Override
    public BaseResponse<GroupDtoListResult> list(@RequestBody Group group) {
        GroupDtoListResult groupDtoListResult = groupService.getList(group);
        return buildJson(groupDtoListResult);
    }

    @Override
    public BaseResponse<Result> userAdd(@RequestBody GroupUser groupUser) {
        Result result = groupService.userAdd(groupUser);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> userQuit(@RequestBody GroupUser groupUser) {
        Result result = groupService.userQuit(groupUser);
        return buildJson(result);
    }

    @Override
    public BaseResponse<GroupDtoListResult> userAddList(@PathVariable("uid") Long uid) {
        GroupDtoListResult groupDtoListResult = groupService.userAddList(uid);
        return buildJson(groupDtoListResult);
    }

    @Override
    public BaseResponse<String> uploadHeadPic(@RequestParam("file") MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_GROUP_HEAD);
        } catch (Exception e) {
            e.printStackTrace();
            return buildJson(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc());
        }
        return buildJson(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), url);
    }

    @Override
    public BaseResponse<Result> createGroup(@RequestBody Group group) {
        Result result = groupService.createGroup(group);
        return buildJson(result);
    }

    @Override
    public BaseResponse<DtoResult> getGroupById(Integer id) {
        DtoResult dtoResult = groupService.getGroupById(id);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<Result> updateGroup(Group group) {
        Result result = groupService.updateGroup(group);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> disband(Integer id) {
        Result result = groupService.disband(id);
        return buildJson(result);
    }

    @Override
    public BaseResponse<UserListDtoResult> userList(Integer id) {
        return buildJson(groupService.userList(id));
    }
}

