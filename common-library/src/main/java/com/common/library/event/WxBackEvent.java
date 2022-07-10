package com.common.library.event;

public class WxBackEvent {

    public String wx_code;
    public boolean cancel;

    public WxBackEvent(String wx_code, boolean cancel) {
        this.wx_code = wx_code;
        this.cancel = cancel;
    }
}
