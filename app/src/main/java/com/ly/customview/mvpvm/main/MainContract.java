package com.ly.customview.mvpvm.main;

import android.content.Context;

import com.ly.customview.mvp.BasePresenter;
import com.ly.customview.mvp.BaseView;


public class MainContract {
    interface View extends BaseView {
        void getDataSuccess();

        void getDataFail();
    }

    interface Presenter extends BasePresenter<View> {
        void getData(int i);
    }
}
