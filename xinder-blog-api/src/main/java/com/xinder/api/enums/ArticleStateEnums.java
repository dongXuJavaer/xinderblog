package com.xinder.api.enums;

/**
 * @author Xinder
 * @date 2023-03-20 14:36
 */
public enum ArticleStateEnums {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发表"),
    DELETED(2, "已删除"),
    NON_OPEN(3, "不公开"),
    AUDITING(4, "待审核");

    private int code;
    private String value;


    ArticleStateEnums(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
