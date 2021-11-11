package com.ibs.fixmtoacris.model;



public class FabricResponse {
    private int status;
    private String message;
    private Object data;

    public FabricResponse() {
        this.status = 0;
        this.message = "";
        this.data = null;
    }

    public FabricResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
