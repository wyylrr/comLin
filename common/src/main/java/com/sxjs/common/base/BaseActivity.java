package com.sxjs.common.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.barlibrary.ImmersionBar;
import com.sxjs.common.AppComponent;
import com.sxjs.common.GlobalAppComponent;
import com.sxjs.common.R;
import com.sxjs.common.events.CommonEvent;
import com.sxjs.common.receiver.NetWorkChangeBroadcastReceiver;
import com.sxjs.common.util.DialogUtil;
import com.sxjs.common.widget.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by admin on 2017/3/12.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Dialog loadingDialog;
    private NetWorkChangeBroadcastReceiver receiver;
    private ImmersionBar mImmersionBar;
    private Activity mActivity;
    public Dialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onWindowFocusChanged(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffe22222));
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar
//                .supportActionBar(true) //支持ActionBar使用
//                .fitsSystemWindows(true)
//                .statusBarDarkFont(true, 0.2f)
//                .statusBarColor(R.color.search_corner_bg)
//                .init();
        mContext = getAppComponent().getContext();
        mActivity = this;
        registerNetChangeReceiver();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void showCenterToast(String msg){
        LayoutInflater inflater=LayoutInflater.from(this);
        View toast_view = inflater.inflate(R.layout.toast_layout,null);
        TextView tvToast = (TextView) toast_view.findViewById(R.id.tvToast);
        tvToast.setText(msg);
        Toast toast=new Toast(this);
        toast.setView(toast_view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    private void registerNetChangeReceiver() {
        receiver = new NetWorkChangeBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver , intentFilter);
    }

    protected AppComponent getAppComponent() {
        return GlobalAppComponent.getAppComponent();
    }

    protected void addFragment(int containerViewId, Fragment fragment , String tag) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(containerViewId, fragment , tag);
        fragmentTransaction.commit();
    }

    protected void showShortToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    protected void showProgressDialog(){
        this.showProgressDialog(null,null);
    }

    protected void showProgressDialog(String msg){
        this.showProgressDialog(msg , null);
    }

    protected void showProgressDialog(DialogInterface.OnCancelListener listener){
        this.showProgressDialog(null ,listener);
    }

    protected void showProgressDialog(String msg , DialogInterface.OnCancelListener listener){
        if(loadingDialog == null){
            loadingDialog = DialogUtil.createLoadingDialog(this, msg, listener);
        }else if(!loadingDialog.isShowing()){
            loadingDialog.show();
        }

    }

    protected void hiddenProgressDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }


    private CompositeDisposable disposables;

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
            mDialog = new LoadingDialog(this, message);
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

    /**
     * 添加观察者
     * @param disposable d
     */
    public void addDisposable(Disposable disposable){
        if(disposables == null){
            disposables = new CompositeDisposable();
        }
        disposables.add(disposable);

    }

    /**
     * 注销观察者，防止泄露
     */
    public void clearDisposable(){
        if(disposables != null){
            disposables.clear();
            disposables = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        clearDisposable();

        if(null != receiver){
            receiver.onDestroy();
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonEvent event) {
//            finish();
    }

}
