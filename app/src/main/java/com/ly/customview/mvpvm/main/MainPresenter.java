package com.ly.customview.mvpvm.main;

import com.ly.customview.mvp.BasePresenterImpl;


public class MainPresenter extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter {


    @Override
    public void getData(int i) {
        if (i > 10) {
            mView.getDataSuccess();
        } else {
            mView.getDataFail();
        }
    }
}
