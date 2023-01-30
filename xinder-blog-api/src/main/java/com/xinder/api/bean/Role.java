package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by sang on 2017/12/17.
 */
@Data
@TableName("roles")
public class Role implements Serializable {

    @TableId
    private Long id;
    private String name;

    public Role() {
    }


    public Role(Long id, String name) {

        this.id = id;
        this.name = name;
    }
}
