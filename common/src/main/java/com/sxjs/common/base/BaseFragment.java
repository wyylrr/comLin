package com.sxjs.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sxjs.common.AppComponent;
import com.sxjs.common.GlobalAppComponent;
import com.sxjs.common.util.DialogUtil;
import com.sxjs.common.widget.dialog.LoadingDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by admin on 2017/3/15.
 */

public class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;
    public Dialog mDialog;

    /**
     * gif_logo进度dialog
     */
    private Dialog dialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = getAppComponent().getContext();
    }

    protected void showShortToast(String message){
        Toast.makeText(mActivity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message){
        Toast.makeText(mActivity.getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    protected AppComponent getAppComponent() {
        return GlobalAppComponent.getAppComponent();
    }

    protected void showJDLoadingDialog(){
        if(dialog == null)dialog = DialogUtil.createJDLoadingDialog(mActivity, null);
        if(!dialog.isShowing()){
            dialog.show();
        }
    }

    protected void hideJDLoadingDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

    public int compareDate(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**

     * 强制隐藏输入法键盘

     */

    public void hideInput(Context context, View view){

        InputMethodManager inputMethodManager =

                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(dialog != null){
            if(dialog.isShowing())dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 显示进度对话框
     *
     * @param message
     */
    public void showLoading(String message) {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mDialog = new LoadingDialog(mActivity, message);
            mDialog.setCancelable(true);
            mDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭对话框
     */
    public void dissmissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    //获取当天0点的时间戳
    public Long getZeroClockTimestamp(Long time) {
        return time - (time + TimeZone.getDefault().getRawOffset()) % (24 * 60 * 60 * 1000);
    }

    public Long getStartDate(Date date){
        //Calendar 是java.util下的工具类
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.add(Calendar.MONTH, 0); //获取当前月第一天
//        c.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
        c.set(Calendar.MINUTE, 0); //将分钟至0
        c.set(Calendar.SECOND,0); //将秒至0
        c.set(Calendar.MILLISECOND, 0); //将毫秒至0
        System.out.println("当月第一天时间戳："+c.getTimeInMillis());
        return c.getTimeInMillis();
    }

    public Long getStartDefDate(){
        //Calendar 是java.util下的工具类
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0); //获取当前月第一天
        c.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
        c.set(Calendar.MINUTE, 0); //将分钟至0
        c.set(Calendar.SECOND,0); //将秒至0
        c.set(Calendar.MILLISECOND, 0); //将毫秒至0
        System.out.println("当月第一天时间戳："+c.getTimeInMillis());
        return c.getTimeInMillis();
    }

    public Long getEndDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
        c.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
        c.set(Calendar.MINUTE, 59); //将分钟至59
        c.set(Calendar.SECOND,59); //将秒至59
        c.set(Calendar.MILLISECOND, 999); //将毫秒至999
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("当月最后一天时间戳："+c.getTimeInMillis());
        return c.getTimeInMillis();
    }

    public Long getEndDefDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
        c.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
        c.set(Calendar.MINUTE, 59); //将分钟至59
        c.set(Calendar.SECOND,59); //将秒至59
        c.set(Calendar.MILLISECOND, 999); //将毫秒至999
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当月最后一天时间戳："+c.getTimeInMillis());
        return c.getTimeInMillis();
    }

}
