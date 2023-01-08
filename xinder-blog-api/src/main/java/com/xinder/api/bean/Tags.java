package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by sang on 2017/12/21.
 */
@Data
@NoArgsConstructor
@TableName("tasg")
public class Tags implements Serializable {

    @TableId
    private Long id;
    private String tagName;

}
