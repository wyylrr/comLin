package com.sxjs.common.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sxjs.common.R;


public class DialogTip extends BaseDialog {

    private TextView tvTitle;
    private Context context;

    public DialogTip(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 0x01) {
                dismissDialog();
                cancel();
                handler.removeCallbacksAndMessages(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip_layout);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_tip);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        Window window = getWindow();
//        window.setWindowAnimations(R.style.ScaleAnim);
        window.setGravity(Gravity.CENTER);
        //默认的Dialog只有5/6左右的宽度，改为全屏宽度,由dialog的布局自己来决定实际显示宽度
        window.getDecorView().setPadding(60, 0, 60, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
    }

    public void setText(String str) {
        if (tvTitle != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(str);
        }
    }

    @Override
    public void show() {
        super.show();
        handler.sendEmptyMessageDelayed(0x01, 5000);
    }

    public void showTime(long time) {
        super.show();
        handler.sendEmptyMessageDelayed(0x01, time);
    }

    public void showLongTime() {
        super.show();
    }

    public void dismissDialog(){
        super.dismiss();
        dismiss();
    }

    //对话框事件 拦截去掉，给activity
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (context instanceof Activity){
            ((Activity) context).dispatchKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }
}
