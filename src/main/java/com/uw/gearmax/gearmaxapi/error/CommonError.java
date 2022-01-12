package com.uw.gearmax.gearmaxapi.error;

public interface CommonError {
    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);
}
