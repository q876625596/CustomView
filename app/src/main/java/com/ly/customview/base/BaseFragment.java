package com.ly.customview.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kumi on 2017/3/16 0016.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onCreateView,View的初始化方法后方法后即为true
     */
    protected boolean initComplete;

    /**
     * 初始化
     * 执行顺序：3
     * 执行次数：1
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化根view
        mRootView = inflater.inflate(setLayoutResourceId(), container, false);
        //初始化view
        initView();
        //设置监听
        setListener();
        //获取activity传来的数据，如果没有则不实现该方法
        getData(getArguments());
        //初始化数据，如果需要懒加载，则可以不实现该方法
        initData();
        //初始化view之后将initComplete设置为true
        initComplete = true;
        return mRootView;
    }

    /**
     * 贴附的activity
     * 执行顺序：1
     * 执行次数：每次显示时执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser) {
            onVisibleToUser();
        }
    }

    //懒加载数据,如果不需要则不实现onLazyLoad()
    protected void onVisibleToUser() {
        if (initComplete && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 绑定Activity
     * 执行顺序：2
     * 执行次数：1
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
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


    //获取activity传来的数据，如果没有则不实现该方法
    abstract protected void getData(Bundle bundle);

    //初始化view
    abstract protected void initView();

    //设置监听
    abstract protected void setListener();

    //设置layout
    protected abstract int setLayoutResourceId();

    //懒加载数据,如果不需要则不实现该方法
    abstract protected void onLazyLoad();

    //初始化数据，如果需要懒加载，则可以不实现该方法
    abstract protected void initData();
}
