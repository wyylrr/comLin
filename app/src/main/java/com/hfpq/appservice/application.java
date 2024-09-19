package com.hfpq.appservice;

import com.hjq.toast.ToastUtils;
import com.sxjs.common.base.BaseApplication;

public class application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 Toast 框架
        ToastUtils.init(this);
    }
}
