package com.xinder.api.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xinder.api.bean.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-04 19:03
 */
@Data
public class UserDtoReq implements Serializable {

    @TableId( type = IdType.AUTO)
    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    @TableField(value = "username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField(value = "password")
    private String password;

    @ApiModelProperty(value = "昵称")
    @TableField(value = "nickname")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;

    @ApiModelProperty(value = "头像地址")
    @TableField(value = "userface")
    private String userface;

    @ApiModelProperty(value = "用户等级")
    @TableField(value = "level")
    private Integer level;

    @ApiModelProperty(value = "性别" , notes = "1:女    2:男")
    @TableField(value = "gender")
    private Integer gender;

    @ApiModelProperty(value = "用户权限")
    private List<Role> roles;
}
