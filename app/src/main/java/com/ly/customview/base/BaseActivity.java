package com.ly.customview.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ly.customview.mvpvm.ActivityViewModel;
import com.ly.customview.tools.LogUtils;
import com.othershe.nicedialog.NiceDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public abstract class BaseActivity<T extends ViewDataBinding,K extends ActivityViewModel> extends RxAppCompatActivity implements View.OnClickListener {

    protected T dataBinding;
    protected K viewModel;

    //销毁Activity的tag
    private final String FINISH_ACTION = "finish_action";

    //键盘是否弹出
    //建议在activity中对贴底的控件进行ui监听setOnSystemUiVisibilityChangeListener()
    //根据底部控件的位置变化来判断软键盘的弹出和收起
    private boolean inputMethodIsShow;

    public boolean isInputMethodIsShow() {
        return inputMethodIsShow;
    }

    //设置输入法是否打开
    public void setInputMethodIsShow(boolean inputMethodIsShow) {
        this.inputMethodIsShow = inputMethodIsShow;
    }

    /**
     * 在需要销毁的activity中注册广播
     */
    protected void registerQuitBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ACTION);
        this.registerReceiver(finishAppReceiver, filter);
    }

    /**
     * 取消注册广播
     */
    protected void unregisterQuitBroadcast() {
        this.unregisterReceiver(finishAppReceiver);
    }

    /**
     * 启动广播，销毁注册过的activity
     */
    protected void startQuitBroadcast() {
        Intent intent = new Intent();
        intent.setAction(FINISH_ACTION);
        this.sendBroadcast(intent);
    }

    /**
     * 用于关闭Activity的广播
     */
    protected BroadcastReceiver finishAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    //设置隐藏状态栏（全屏），必须在setContentView之前调用
    protected void setFixScreen() {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //设置预留statusBar
    protected void setFitsSystemWindows(boolean isFits) {
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(isFits);
    }

    /**
     * 设置透明状态栏
     *
     * @param isFits 是否为状态栏预留高度
     */
    protected void setTransparentStatusBar(boolean isFits) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option;
            option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(option);
            ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(isFits);
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= 19) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Miui系统设置字体
     *
     * @param isBlack 是否设置为黑色字体
     * @return 设置成功返回true
     */
    private boolean setStatusTextColorForMiui(boolean isBlack) {
        Class clazz = getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (isBlack) {
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(getWindow(), 0, darkModeFlag);//清除黑色字体
            }
            return true;
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return false;
    }

    /**
     * Flyme系统设置字体
     *
     * @param isBlack 是否设置为黑色字体
     * @return 设置成功返回true
     */
    private boolean setStatusTextColorForFlyme(boolean isBlack) {
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (isBlack) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return false;
    }

    /**
     * 6.0以上设置字体颜色
     *
     * @param isBlack 是否设置为黑色字体
     * @return 设置成功返回true
     */
    private boolean setStatusTextColorForMPlus(boolean isBlack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (isBlack) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
            return true;
        }
        return false;
    }

    /**
     * 设置字体颜色方法汇总
     *
     * @param isBlack 是否设置为黑色字体
     */
    protected void setStatusTextColorMain(boolean isBlack) {
        Boolean setStatus = setStatusTextColorForMPlus(isBlack)
                && setStatusTextColorForMiui(isBlack)
                && setStatusTextColorForFlyme(isBlack);
        LogUtils.e(setStatus ? "设置字体成功" : "设置字体失败");
    }


    /**
     * 设置状态栏颜色
     *
     * @param color      颜色
     * @param isRootView 是否使用根布局背景色
     */
    protected void setStatusColor(int color, boolean isRootView) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //set child View not fill the system window
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            //要将FitsSystemWindows设置为true
            ViewCompat.setFitsSystemWindows(mChildView, true);
            if (mChildView == null) {
                return;
            }

            //*********************这里分为两种情况，一种设置根布局的BackgroundColor为statusBar的颜色********************
            //********************另一种则是在状态栏位置动态添加一个View，该View的背景色则为statusBar的颜色**************

            //当使用根布局的背景色
            if (isRootView) {
                //此时的状态栏的颜色相当于xml中根布局的颜色
                //所以在这里设置根布局的颜色
                mChildView.setBackgroundColor(color);
                return;
            }
            //当创建一个与statusBar大小相同的View
            ViewGroup decorViewGroup = (ViewGroup) getWindow().getDecorView();
            View statusBarView = new View(this);
            int statusBarHeight = getTopHeight(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarView);
        }
    }

    protected int getTopHeight(Context context) {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
            LogUtils.e(statusBarHeight1 + "");
        }
        return statusBarHeight1;
    }

    /**
     * 页面跳转
     *
     * @param activityClass 需要跳转的Activity.class
     */
    protected void startActivity(Class<?> activityClass) {
        startActivity(new Intent(BaseActivity.this, activityClass));
    }

    /**
     * 携带数据的页面跳转
     *
     * @param activityClass 需要跳转的Activity.class
     * @param bundle        数据
     */
    protected void startActivity(Class<?> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 回调页面跳转
     *
     * @param activityClass 需要跳转的Activity.class
     * @param bundle        数据（如果为null，那么没有数据传输）
     * @param requestCode   请求码
     */
    protected void startActivityForResult(Class<?> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    //避免用户重复点击，一秒内只允许触发控件一次
    private long lastTime = 0;

    protected synchronized boolean couldClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 1000) {
            return false;
        } else {
            lastTime = currentTime;
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        //防止短时间内多次点击
        if (!couldClick()) {
            return;
        }
        //如需双击则继承时删除此方法的super
    }

    //初始化控件
    abstract protected void initView();


    //强制隐藏软键盘
    protected void hideSoftKeyboard(EditText editText) {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            LogUtils.e("hide");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //关闭软键盘
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            setInputMethodIsShow(false);
        }
    }

    /**
     * 打开或者关闭软键盘
     */
    protected void showOrHideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            //如果软键盘已经打开，那么会关闭软键盘
            //反之打开软件盘
            //所以需要设置flag来判断
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //打开软键盘
    protected void showSoftKeyboard(EditText editText) {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);
            setInputMethodIsShow(true);
        }
    }


    //显示加载中的dialog，自定义布局
    protected void showLoadingDialog(int resId) {
        NiceDialog.init()
                .setLayoutId(resId)
                .setWidth(100)
                .setHeight(100)
                .setDimAmount(0.2f)
                .show(getSupportFragmentManager());
    }

}
