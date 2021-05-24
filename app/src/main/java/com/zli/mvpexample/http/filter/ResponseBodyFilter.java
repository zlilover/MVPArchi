package com.zli.mvpexample.http.filter;

import com.zli.mvpexample.presenter.BasePresenter;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by lizhen on 2021/3/17.
 * 返回responsebody时的回调
 */
public class ResponseBodyFilter implements Consumer<ResponseBody> {

    private final WeakReference<BasePresenter> mBasePresenter;
    private final int tag;

    public ResponseBodyFilter(BasePresenter basePresenter,int tag) {
        this.mBasePresenter = new WeakReference<>(basePresenter);
        this.tag = tag;
    }

    @Override
    public void accept(ResponseBody responseBody) {
        if (mBasePresenter.get() != null) {
            mBasePresenter.get().successWithResponseBody(responseBody,tag);
        }
    }
}
