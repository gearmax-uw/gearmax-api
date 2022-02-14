package com.uw.gearmax.gearmaxapi.response;

public class CommonReturnType {

    private String status;
    private Object data;
    private Object metaData;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public static CommonReturnType create(Object result, Object additionalInfo) {
        return CommonReturnType.create(result, additionalInfo, "success");
    }

    public static CommonReturnType create(Object result, Object metaData, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        type.setMetaData(metaData);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getMetaData() {
        return metaData;
    }

    public void setMetaData(Object metaData) {
        this.metaData = metaData;
    }
}
