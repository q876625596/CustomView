package com.ly.customview.mvpvm.main;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ly.customview.R;
import com.ly.customview.databinding.ActivityMainBinding;
import com.ly.customview.mvp.MVPBaseActivity;
import com.ly.customview.tools.LogUtils;


//主Activity，只负责dataBinding，ViewModel，presenter的初始化与绑定，屏幕的初始化，以及各种回调
public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter, ActivityMainBinding, MainViewModel> implements MainContract.View {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //有些屏幕的设置需要放在setContentView之前
        initView();
    }


    /*
    * 初始化，这里只需要初始化dataBinding，ViewModel，具体控件的一些初始化放到ViewModel里面
    */
    @Override
    protected void initView() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(this, dataBinding);
        dataBinding.setViewModel(viewModel);
    }


    /*模拟mvp的回调成功与失败*/
    @Override
    public void getDataSuccess() {
        LogUtils.e("success");
        Snackbar.make(dataBinding.textView4, "获取成功", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void getDataFail() {
        LogUtils.e("fail");
        Snackbar.make(dataBinding.textView4, "获取失败", Snackbar.LENGTH_SHORT).show();
    }
}
