package com.luding.smartoa.view;

import android.content.Context;
import com.luding.smartoa.presenter.BaseEvent;

/**
 * Created by lizhen on 2021/3/17.
 */

public interface BaseView {
    //显示对话框
    void loadingDialog();

    //加载完成
    void finishLoading();

    //view是否存活
    boolean isViewAlive();

    //获取view上下文
    Context getViewContext();

    //获取P层之间传递的信息
    void onEvent(BaseEvent baseEvent);

    //用于P层的信息传递
    void reportEvent(BaseEvent baseEvent);

    //判断当前view是否已经加载，读写本上刷新慢，activity加载慢，可能进入洁面后，数据加载了但是view没加载出来
    boolean isWindowAttach();
    
    //展示服务端返回的错误信息，基类直接实现，子类一般不需要重写
    void showRequestDes(String errorText);
}
