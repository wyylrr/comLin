package com.sxjs.common.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxjs.common.R;

/**
 * Created by admin on 2017/3/13.
 */

public class DialogUtil {

    private static ObjectAnimator refreshAnimator;
    /**
     * 有取消回调的进度dialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Activity context, String msg, DialogInterface.OnCancelListener listener) {
        final Dialog dialog = new Dialog(context , R.style.NoBackGroundDialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        if(listener != null) dialog.setOnCancelListener(listener);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        int width = ScreenUtil.getWidth(context) * 2 / 3;
        window.setLayout(width,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = context.getLayoutInflater().inflate(
                R.layout.loading_dialog, null);
        TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);// 提示文字
        if(!TextUtils.isEmpty(msg)){
            tipTextView.setText(msg);// 设置加载信息
        }

        window.setContentView(view);//
        return dialog;

    }

    /**
     * gif动画进度
     * @param context
     *
     * @return
     */
    public static Dialog createJDLoadingDialog(Activity context, DialogInterface.OnCancelListener listener) {
        final Dialog dialog = new Dialog(context , R.style.NoBackGroundDialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        if(listener != null) dialog.setOnCancelListener(listener);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = context.getLayoutInflater().inflate(
                R.layout.jd_loading_dialog, null);
        window.setContentView(view);//
        return dialog;

    }


    /**
     * gif动画进度
     * @param context
     *
     * @return
     */
    public static Dialog createAnimatorLoadingDialog(Activity context, DialogInterface.OnCancelListener listener) {
        final Dialog dialog = new Dialog(context , R.style.NoBackGroundDialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        if(listener != null) dialog.setOnCancelListener(listener);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = context.getLayoutInflater().inflate(
                R.layout.animator_loading_dialog, null);
        ImageView ivRefresh = (ImageView) view.findViewById(R.id.ivRefresh);
        ivRefresh.setImageResource(R.drawable.refresh_loading);
//新建动画.属性值从1-10的变化
        refreshAnimator = ObjectAnimator.ofInt(ivRefresh, "imageLevel", 1, 8);
//设置动画的播放数量为一直播放.
        refreshAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        //设置一个速度加速器.让动画看起来可以更贴近现实效果.
        refreshAnimator.setInterpolator(new LinearInterpolator());
        refreshAnimator.setRepeatMode(ObjectAnimator.RESTART);
        refreshAnimator.setDuration(1500);
        refreshAnimator.start();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshAnimator.cancel();
            }
        });
        window.setContentView(view);
        return dialog;

    }

    public static Dialog TipDialog(Activity activity, String title,String msg, View.OnClickListener listener){
        final Dialog dialog = new Dialog(activity , R.style.NoBackGroundDialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = activity.getLayoutInflater().inflate(
                R.layout.dialog_tip, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        TextView tvCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView tvQuery = (TextView) view.findViewById(R.id.btnQuery);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvQuery.setOnClickListener(v->{
            dialog.dismiss();
            listener.onClick(v);
        });
        window.setContentView(view);
        return dialog;
    }

    public static Dialog TipCancelDialog(Activity activity, String title,String msg){
        final Dialog dialog = new Dialog(activity , R.style.NoBackGroundDialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = activity.getLayoutInflater().inflate(
                R.layout.dialog_tip, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        TextView tvCancel = (TextView) view.findViewById(R.id.btnCancel);
        tvCancel.setText("关闭");
        TextView tvQuery = (TextView) view.findViewById(R.id.btnQuery);
        tvQuery.setVisibility(View.GONE);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvTitle.setText(title);
        tvMsg.setText(msg);
        window.setContentView(view);
        return dialog;
    }

}
