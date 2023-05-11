package com.db.common.exception;

import com.db.common.enums.ErrorCodeEnum;

/**
 * @Description: 项目通用基本异常，继承RuntimeException
 * @Date: 2023/5/11
 */
public class BusinessException extends RuntimeException {

    //业务异常编码
    private int code;

    //业务异常信息
    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCodeEnum.FAIL.getCode();
        this.message = message;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static BusinessException fail(String message) {
        return new BusinessException(message);
    }

    public static BusinessException failonBusinessValidation(String message) {
        return new BusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), message);
    }

    public static BusinessException failonBasicValidation(String message) {
        return new BusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), message);
    }

    public static BusinessException fail(ErrorCodeEnum errorCode) {
        return new BusinessException(errorCode.getCode(),errorCode.getDesc());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
