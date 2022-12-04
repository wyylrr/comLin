package com.sxjs.common.events;

/**
 * Created by yjy on 17/5/25.
 */

public class CommonEvent {

    private int type;

    private Object data;

    public CommonEvent() { }

    public CommonEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
