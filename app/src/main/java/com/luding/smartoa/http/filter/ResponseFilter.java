package com.luding.smartoa.http.filter;



import com.luding.smartoa.http.response.BaseResponse;
import com.luding.smartoa.presenter.BasePresenter;
import com.luding.smartoa.util.JsonUtils;
import com.luding.smartoa.util.Logger;

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
