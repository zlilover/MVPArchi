package com.luding.smartoa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luding.smartoa.activity.BaseActivity;
import com.luding.smartoa.presenter.BaseEvent;
import com.luding.smartoa.presenter.BasePresenter;
import com.luding.smartoa.view.BaseView;
import com.luding.smartoa.view.ViewManager;


/**
 * Created by lizhen on 2021/3/17.
 */
public abstract class BaseFragment extends Fragment implements BaseView {
    protected BasePresenter basePresenter;
    protected String TAG = getClass().getSimpleName();
    protected View mView;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter = setPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(getLayoutId(), container, false);
        initView(mView);
        return mView;
    }

    protected abstract void initView(View view);

    protected abstract int getLayoutId();

    public void setLoadingText(String text) {
        if (getContext() != null) {
            ((BaseActivity)getContext()).setLoadingText(text);
        }
    }


    @Override
    public void finishLoading() {
        if (getContext() != null) {
            ((BaseActivity)getContext()).finishLoading();
        }
    }

    @Override
    public void loadingDialog() {
        if (getContext() != null) {
            ((BaseActivity)getContext()).loadingDialog();
        }
    }

    protected void refreshWithBundle(@NonNull Bundle bundle) {

    }

    protected abstract BasePresenter setPresenter();

    @Override
    public boolean isViewAlive() {
        return !isDetached();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public boolean isWindowAttach() {
        return !isDetached();
    }

    @Override
    public void onEvent(BaseEvent baseEvent) {

    }

    @Override
    public void reportEvent(BaseEvent baseEvent) {
        ViewManager.getInstance().notifyViewChange(baseEvent);
    }
}
