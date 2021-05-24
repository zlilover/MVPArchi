package com.zli.mvpexample.http.filter;
import com.zli.mvpexample.http.request.RequestConfig;
import com.zli.mvpexample.http.response.BaseResponse;
import com.zli.mvpexample.presenter.BasePresenter;
import com.zli.mvpexample.util.Logger;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by lizhen on 2021/3/17.
 */
public class ErrorFilter implements Consumer<Throwable> {
    private final BasePresenter basePresenter;
    private final int tag;
    public ErrorFilter(BasePresenter basePresenter, int tag) {
        this.basePresenter = basePresenter;
        this.tag = tag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(Throwable throwable) {
        Logger.d("RequestMessageError","error happen during the observable subscribe, http tag is:" + tag + "--->" + throwable.toString());
        for (StackTraceElement element:throwable.getStackTrace()) {
            Logger.d("Exception",element.toString());
        }
        retrofit2.Response body = null;
        if (throwable instanceof HttpException) {
            body = ((HttpException)throwable).response();
        }
        BaseResponse response = new BaseResponse(null,throwable.toString());
        if (body != null) {
            response.setHttpResponseCode(body.code());
        }
        if (throwable instanceof SocketTimeoutException) {
            response.setMsg(RequestConfig.SOCKET_TIME_OUT_EXCEPTION);
        } else if (throwable instanceof ConnectException) {
            response.setMsg(RequestConfig.CONNECT_TIME_OUT_EXCEPTION);
        } else if (throwable instanceof UnknownHostException) {
            response.setMsg(RequestConfig.UNKNOWN_HOST_EXCEPTION);
        } else if (throwable instanceof NoRouteToHostException) {
            response.setMsg(RequestConfig.NOROUTE_TO_HOST_EXCEPTION);
        }
        basePresenter.error(response,tag);
    }
}
