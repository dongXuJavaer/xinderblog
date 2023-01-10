package com.xinder.api.response.base;

import com.xinder.api.enums.ResultCode;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

	private static final long serialVersionUID = 3478493193648267325L;

	@ApiModelProperty(required=true)
	private boolean success;
	@ApiModelProperty(required=false)
	private String msg;
	@ApiModelProperty(required=false)
	private T data;
	@ApiModelProperty(required=true)
	private Integer code;

	public BaseResponse() {
		super();
	}

	private BaseResponse(Integer code, String msg, T data) {
		this.code = code;
		this.msg  = msg;
		this.data = data;
	}

	private BaseResponse(T data) {
		this.code = ResultCode.SUCCESS.getCode();
		this.msg = ResultCode.SUCCESS.getDesc();
		this.setSuccess(true);
		this.data = data;
	}

    /**
     * 返回失败的JSON串
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
	public static BaseResponse fail(Integer code, String msg, Object data) {
		return new BaseResponse(code, msg, data);
	}

	/**
	 * 返回失败的JSON串,数据体为null
	 * @param code
	 * @param msg
	 * @return
	 */
	public static BaseResponse fail(Integer code, String msg) {
		return BaseResponse.fail(code, msg, null);
	}

	/**
	 * 返回系统异常的错误信息JSON串
	 * @return
	 */
	public static BaseResponse systemError() {
		return BaseResponse.fail(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc(), null);
	}

	/**
	 * 返回成功响应
	 * @return
	 */
	public static BaseResponse success() {
		BaseResponse baseResponse = new BaseResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), null);
		baseResponse.setSuccess(true);
		return baseResponse;
	}

	public static BaseResponse success(Integer code, String msg) {
		return BaseResponse.success(code, msg, null);
	}

	/**
	 * 返回成功的JSON
	 *
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public static BaseResponse success(Integer code, String msg, Object data) {
		BaseResponse baseResponse = new BaseResponse(code, msg, data);
		baseResponse.setSuccess(true);
		return baseResponse;
	}

	/**
	 * 返回失败响应
	 * @return
	 */
	public static BaseResponse fail() {
		BaseResponse baseResponse = new BaseResponse(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc(), null);
		baseResponse.setSuccess(false);
		return baseResponse;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
