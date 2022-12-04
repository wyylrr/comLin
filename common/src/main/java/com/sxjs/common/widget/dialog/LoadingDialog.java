package com.sxjs.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sxjs.common.R;


/**
 * 描述：自定义加载对话框
 * Created by 夏超 on 2019/6/14
 * email:    ah_xiachao@126.com
 * version:  v1.0
 */
public class LoadingDialog extends Dialog {
    private static final String TAG = "LoadingDialog";
    private String mMessage; // 加载中文字
    private int mImageId; // 旋转图片id
    private RotateAnimation mRotateAnimation;

    public LoadingDialog(@NonNull Context context, String message) {
        this(context, R.style.LoadingDialog, message, 0);
    }

    public LoadingDialog(@NonNull Context context, String message, int imageId) {
        this(context, R.style.LoadingDialog, message, imageId);
    }

    public LoadingDialog(@NonNull Context context, int themeResId, String message, int imageId) {
        super(context, themeResId);
        if (!TextUtils.isEmpty(message)) {
            mMessage = message;
        } else {
            mMessage = "正在加载...";
        }
        if (imageId != 0) {
            mImageId = imageId;
        } else {
            mImageId = R.mipmap.loading;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        // 设置窗口大小
        WindowManager windowManager = getWindow().getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        // 设置窗口背景透明度
        attributes.alpha = 0.8f;
        // 设置窗口宽高为屏幕的三分之一（为了更好地适配，请别直接写死）
        attributes.width = screenWidth / 3;
        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
        ImageView iv_loading = (ImageView) findViewById(R.id.iv_loading);
        tv_loading.setText(mMessage);
        iv_loading.setImageResource(mImageId);
        // 先对imageView进行测量，以便拿到它的宽高（否则getMeasuredWidth为0）
        iv_loading.measure(0, 0);
        // 设置选择动画
        mRotateAnimation = new RotateAnimation(0, 360, iv_loading.getMeasuredWidth() / 2, iv_loading.getMeasuredHeight() / 2);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(1000);
        mRotateAnimation.setRepeatCount(-1);
        iv_loading.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        mRotateAnimation.cancel();
        super.dismiss();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // 屏蔽返回键
//            return mCancelable;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}