package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Resources对象", description="")
public class Resources implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "文件链接")
    private String url;

    @ApiModelProperty(value = "上传用户")
    private Long uid;

    @ApiModelProperty(value = "下载所需积分")
    private Integer point;

    @ApiModelProperty(value = "被下载次数")
    private Integer count;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;

    @ApiModelProperty(value = "0 不公开；   1 公开")
    private Integer state;

    @ApiModelProperty(value = "分享资源作者信息")
    @TableField(exist = false)
    private User user;




}
