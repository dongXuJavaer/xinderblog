package com.xinder.api.request;

import com.xinder.api.bean.Tags;
import com.xinder.api.request.comm.PageDtoReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-06 23:10
 */
@Data
public class ArticleDtoReq extends PageDtoReq {

    private static final long serialVersionUID = 965132976922378L;

    private String mdContent;

    private String htmlContent;

    private Long cid;

    private Integer state;

}
