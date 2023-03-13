package com.xinder.api.enums;

public enum SocketMsgTypeEnums {


    MSG_PRIVATE(1,"私信消息"),
    MSG_GROUP(2,"群聊消息")

    ;
    private Integer code;
    private String desc;


    SocketMsgTypeEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SocketMsgTypeEnums getMsgType(Integer msgType) {
        for (SocketMsgTypeEnums value : SocketMsgTypeEnums.values()) {
            if (value.getCode().equals(msgType)) {
                return value;
            }
        }
        // 理论上不会走进这里
        throw new IllegalArgumentException("unknown msgType:" + msgType);
    }
}
