package com.ly.customview.mvpvm.main;

import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ly.customview.R;
import com.ly.customview.databinding.ActivityMainBinding;
import com.ly.customview.entity.UserBean;
import com.ly.customview.mvpvm.ActivityViewModel;
import com.ly.customview.tools.LogUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class MainViewModel extends ActivityViewModel<MainActivity, ActivityMainBinding> {

    //绑定数据的实体类
    private UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    //如有多个dataBinding，可以重载构造方法，并声明其余的dataBinding
    MainViewModel(MainActivity activity, ActivityMainBinding dataBinding) {
        super(activity, dataBinding);
        init();
        setListener();
    }


    //初始化
    @Override
    protected void init() {
        LogUtils.e("init");
        userBean = new UserBean("kumi", 23, "男");
        LogUtils.e(userBean.toString());
    }

    //设置一系列监听
    @Override
    protected void setListener() {
        dataBinding.commit.setOnClickListener(this);
    }

    //点击监听
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.commit:
                LogUtils.e("asasas");
                updateInfo(v);
                break;
        }
    }

    //如果是绑定点击事件，那么就要把该方法的参数，返回值写的与事件的原方法相同
    //比如这里的updateInfo是绑定的onClick
    //那么参数要和onClick一样为View，返回值为void
    //建议按常规onClick来设置点击事件
    public void updateInfo(View view) {
        activity.mPresenter.getData(10);
        userBean.userName.set(dataBinding.editTextName.getText().toString());
        userBean.userAge.set(Integer.valueOf(dataBinding.editTextAge.getText().toString()));
        userBean.userGender.set(dataBinding.editTextGender.getText().toString());
    }
}
