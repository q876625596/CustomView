package com.ly.customview.entity;

import android.databinding.ObservableField;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class UserBean {
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<Integer> userAge = new ObservableField<>();
    public ObservableField<String> userGender = new ObservableField<>();

    public UserBean() {
    }

    public UserBean(String userName, Integer userAge, String userGender) {
        this.userName.set(userName);
        this.userAge.set(userAge);
        this.userGender.set(userGender);
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userName=" + userName.get() +
                ", userAge=" + userAge.get() +
                ", userGender=" + userGender.get() +
                '}';
    }
}
