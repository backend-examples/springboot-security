package com.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.common.enums.BaseErrorInfo;
import com.common.enums.CodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义数据格式
 * @param <T>
 */
@Data
@ApiModel("数据响应格式")
public class ResponseResult<T> {
    /**
     * 响应代码
     */
    @ApiModelProperty("状态码")
    private String code;

    /**
     * 响应消息
     */
    @ApiModelProperty("响应消息")
    private String message;

    /**
     * 响应结果
     */
    @ApiModelProperty("响应结果")
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(BaseErrorInfo errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResponseResult success() {
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static ResponseResult success(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(CodeEnum.SUCCESS.getResultCode());
        responseResult.setMessage(CodeEnum.SUCCESS.getResultMsg());
        responseResult.setData(data);

        return responseResult;
    }

    /**
     * 失败
     * @param errorInfo
     * @return
     */
    public static ResponseResult error(BaseErrorInfo errorInfo) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(errorInfo.getResultCode());
        responseResult.setMessage(errorInfo.getResultMsg());
        responseResult.setData(null);

        return responseResult;
    }

    /**
     * 失败
     * @param code
     * @param message
     * @return
     */
    public static ResponseResult error(String code, String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setData(null);
        return responseResult;
    }

    /**
     * 失败
     * @param message
     * @return
     */
    public static ResponseResult error(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode("500");
        responseResult.setMessage(message);
        responseResult.setData(null);

        return responseResult;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
