package com.ly.customview.mvpvm;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public abstract class ActivityViewModel<T extends AppCompatActivity, K extends ViewDataBinding> implements View.OnClickListener {
    protected T activity;
    protected K dataBinding;

    public ActivityViewModel(T activity, K dataBinding) {
        this.activity = activity;
        this.dataBinding = dataBinding;
    }

    abstract protected void init();

    abstract protected void setListener();

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

}
