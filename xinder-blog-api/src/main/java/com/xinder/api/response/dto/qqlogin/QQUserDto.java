package com.xinder.api.response.dto.qqlogin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * QQ登录后返回的用户信息实体类
 *
 * @author Xinder
 * @date 2023-01-30 18:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QQUserDto implements Serializable {

    @ApiModelProperty(value = "返回码")
    private String ret;

    @ApiModelProperty(value = "如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。")
    private String msg;

    @ApiModelProperty(value = "用户在QQ空间的昵称。")
    private String nickname;

    @ApiModelProperty(value = "大小为30×30像素的QQ空间头像URL。")
    private String figureurl;

    @ApiModelProperty(value = "大小为50×50像素的QQ空间头像URL。。")
    private String figureurl_1;

    @ApiModelProperty(value = "大小为100×100像素的QQ空间头像URL。")
    private String figureurl_2;

    @ApiModelProperty(value = "大小为40×40像素的QQ头像URL。")
    private String figureurl_qq_1;

    @ApiModelProperty(value = "大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。")
    private String figureurl_qq_2;

    @ApiModelProperty(value = "性别。 如果获取不到则默认返回 男。")
    private String gender;

    @ApiModelProperty(value = "性别类型",notes = "1：女    2：男")
    private Integer gender_type;

}
