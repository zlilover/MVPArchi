package com.luding.smartoa.activity;

import com.luding.smartoa.R;
import com.luding.smartoa.presenter.BasePresenter;

public class MainActivity extends BaseActivity {

    @Override
    protected BasePresenter[] initPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initSelf() {
        needTop(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}