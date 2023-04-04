package com.xinder.api.response.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.bean.Role;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 管理员查询的用户信息
 * @author Xinder
 * @date 2023-01-08 10:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDtoResult extends DtoResult {

    @TableId
    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "是否启用", notes = "0:禁用    1: 启用")
    private Integer enabled = 1;

    @ApiModelProperty(value = "用户权限")
    private List<Role> roles;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像链接")
    private String userface;

    @ApiModelProperty(value = "上次修改时间")
    private Date regTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "性别" , notes = "1:女    2:男")
    private Integer gender;

    @ApiModelProperty(value = "openid（qq登录的对应的）")
    @TableField(value = "openid")
    private String openid;


}
