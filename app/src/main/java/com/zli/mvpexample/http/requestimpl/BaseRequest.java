package com.zli.mvpexample.http.requestimpl;

import com.zli.mvpexample.http.request.HttpRequestBuilder;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhen on 2021/3/17.
 */

public class BaseRequest<T extends BaseRequest.BaseService> {

    public BaseRequest() {
    }

    public BaseRequest(Class<T> tClass) {
        mService = HttpRequestBuilder.getInstance().createService(tClass);
    }

    T mService;
    interface BaseService {

    }

    protected <T> Observable<T> observe(Observable<T> observable) {
//                单元测试时开启
//        return observable.subscribeOn(Schedulers.io()).
//                unsubscribeOn(Schedulers.io());
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> Observable<T> observe(Observable<T> observable, Scheduler schedulers) {
        return observable.subscribeOn(Schedulers.io()).
                unsubscribeOn(Schedulers.io()).
                observeOn(schedulers);
    }
}
