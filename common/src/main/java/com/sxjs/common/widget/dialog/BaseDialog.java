package com.sxjs.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sxjs.common.R;


public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
    }

    public void init(Context context) {
//        Window window = getWindow();
//        window.setWindowAnimations(R.style.BottomAnim);
//        window.setGravity(Gravity.CENTER);
//        //默认的Dialog只有5/6左右的宽度，改为全屏宽度,由dialog的布局自己来决定实际显示宽度
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        init(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(isCancelAble());
        setCanceledOnTouchOutside(isCancelAble());
    }

    public boolean isCancelAble() {
        return true;
    }

}