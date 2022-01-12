package com.uw.gearmax.gearmaxapi.error;

public enum EmBusinessError implements CommonError {
    PARAMETER_VALIDATION_ERROR(10001, "Illegal parameters");

    private int errCode;
    private String errMsg;
    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
