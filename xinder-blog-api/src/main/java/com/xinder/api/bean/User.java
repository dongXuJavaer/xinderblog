package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
//import lombok.ToString;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by xinder on 2022/12/17.
 */
@TableName("user")
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 7456453555365L;

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

    @ApiModelProperty(value = "是否启用", notes = "0:禁用    1: 启用")
    @TableField(value = "enabled")
    private Integer enabled = 1;

    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;

    @ApiModelProperty(value = "头像地址")
    @TableField(value = "userface")
    private String userface;

    @ApiModelProperty(value = "上次修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "reg_time")
    private Date regTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "用户等级")
    @TableField(value = "level")
    private Integer level;

    @ApiModelProperty(value = "用户积分")
    @TableField(value = "point")
    private Integer point;

    @ApiModelProperty(value = "openid（qq登录的对应的）")
    @TableField(value = "openid")
    private String openid;

    @ApiModelProperty(value = "性别" , notes = "1:女    2:男")
    @TableField(value = "gender")
    private Integer gender;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户权限")
    private List<Role> roles;



//    @Override
//    @JsonIgnore
//    public boolean isAccountNonExpired() {
//        return true;
//    }

//    @Override
//    @JsonIgnore
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    @JsonIgnore
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled == 1;
//    }

//    @Override
//    @JsonIgnore
//    public List<GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        }
//        return authorities;
//    }


//    @Override
//    public String getUsername() {
//        return username;
//    }


//    @Override
//    public String getPassword() {
//        return password;
//    }
}
