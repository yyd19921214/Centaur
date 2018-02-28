package com.yudy.centaur.netty;

import java.io.Serializable;

public class ResultMsg implements Serializable{

    private static final long serialVersionUID = 1L;

    private int errCode;

    private String errMsg;


    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


}
