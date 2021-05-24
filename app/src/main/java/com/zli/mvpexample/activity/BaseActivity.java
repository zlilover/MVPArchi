package com.zli.mvpexample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zli.mvpexample.R;
import com.zli.mvpexample.presenter.BaseEvent;
import com.zli.mvpexample.presenter.BasePresenter;
import com.zli.mvpexample.util.ActivityManager;
import com.zli.mvpexample.util.StatusBarUtil;
import com.zli.mvpexample.view.BaseView;
import com.zli.mvpexample.view.ViewManager;
import com.zli.mvpexample.widget.LoadingDialog;

/**
 * Created by lizhen on 2021/3/17.
 */

@SuppressWarnings("all") // 有部分代码后续可能使用，暂时注释导致了警告
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected String TAG = getClass().getSimpleName();
    private RelativeLayout rl_top;
    private TextView tv_title;
    private ImageView img_base_left;
    protected ImageView img_base_right;
    private LinearLayout ll_content;
    private BasePresenter[] basePresenter;
    protected LoadingDialog mLoadingDialog;
    private final int MESSAGE_DISMISS_POP = 1000;
    private View line_right;
    private View line_vertical;
    protected boolean isWindowAttach;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isWindowAttach = false;
        setContentView(R.layout.layout_base);
        setNormalTheme();
        mLoadingDialog = new LoadingDialog().createLoadingDialog(this);
        initView();
        addChildView();
        initSelf();
        basePresenter = initPresenter();
        initData();
        ActivityManager.getInstance().addActivity(this);
        ViewManager.getInstance().addToViews(this);
    }

    public void setNormalTheme() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    private void initView() {
        rl_top = findViewById(R.id.rl_top);
        tv_title = findViewById(R.id.tv_title);
        img_base_left = findViewById(R.id.img_base_left);
        img_base_left.setOnClickListener(v -> finish());
        img_base_right = findViewById(R.id.img_base_right);
        img_base_right.setOnClickListener(v -> ActivityManager.getInstance().AppExit(false));
        ll_content = findViewById(R.id.ll_content);
    }

    @Override
    public void onBackPressed() {
        img_base_left.performClick();
    }

    protected abstract BasePresenter[] initPresenter();

    protected abstract void initData();

    protected abstract void initSelf();

    protected abstract int getLayoutId();

    protected void needTop(boolean isVisible) {
        rl_top.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void needBack(boolean isVisible) {
        img_base_left.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        line_vertical.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void setLeftClick(View.OnClickListener onClickListener) {
        img_base_left.setOnClickListener(onClickListener);
    }

    protected void setImg_base_right(int resourceId, View.OnClickListener onClickListener) {
        img_base_right.setVisibility(View.VISIBLE);
        if (resourceId != -1) {
            img_base_right.setImageResource(resourceId);
        }
        img_base_right.setOnClickListener(onClickListener);
    }

    protected void setImgBaseRightVisible(int visibility) {
        img_base_right.setVisibility(visibility);
    }

    protected void setTv_title(String title) {
        tv_title.setText(title);
    }

    protected void needTitle(boolean isVisible) {
        tv_title.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void addChildView() {
        if (getLayoutId() == 0) {
            return;
        }
        LayoutInflater.from(this).inflate(getLayoutId(), ll_content, true);
    }

    public void setLoadingText(String text) {
        mLoadingDialog.setText(text);
    }

    @Override
    public void loadingDialog() {
        if (isViewAlive() && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void finishLoading() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRequestDes(String errorText) {

    }

    @Override
    protected void onDestroy() {
        finishLoading();
        ActivityManager.getInstance().removeActivity(this);
        ViewManager.getInstance().removeView(this);
        if (basePresenter != null) {
            for (BasePresenter child : basePresenter) {
                child.destroy();
            }
        }
        super.onDestroy();
        isWindowAttach = false;
    }

    @Override
    public boolean isViewAlive() {
        return !this.isFinishing() && !this.isDestroyed();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean isWindowAttach() {
        return isWindowAttach;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isWindowAttach = true;
    }

    @Override
    public void onEvent(BaseEvent baseEvent) {

    }

    @Override
    public void reportEvent(BaseEvent baseEvent) {
        ViewManager.getInstance().notifyViewChange(baseEvent);
    }
}
