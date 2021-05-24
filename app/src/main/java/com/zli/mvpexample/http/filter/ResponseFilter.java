package com.zli.mvpexample.http.filter;



import com.zli.mvpexample.http.response.BaseResponse;
import com.zli.mvpexample.presenter.BasePresenter;
import com.zli.mvpexample.util.JsonUtils;
import com.zli.mvpexample.util.Logger;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Created by lizhen on 2021/3/17.
 */
public class ResponseFilter<T> implements Consumer<BaseResponse<T>> {
    private final WeakReference<BasePresenter> mBasePresenter;

    public ResponseFilter(BasePresenter basePresenter) {
        this.mBasePresenter = new WeakReference<>(basePresenter);
    }

    @Override
    public void accept(BaseResponse<T> tResponse) {
        if (mBasePresenter.get() == null) {
            return;
        }
        Logger.d("RequestMessageReturn===", JsonUtils.toString(tResponse));
        if (tResponse.getHttpResponseCode() != 200) {
            mBasePresenter.get().error(tResponse,tResponse.getHttpTag());
        } else {
            mBasePresenter.get().successFilter(tResponse);
        }
    }
}
