package com.luding.smartoa.model;

import com.luding.smartoa.http.filter.ErrorFilter;
import com.luding.smartoa.http.filter.ResponseBodyFilter;
import com.luding.smartoa.http.filter.ResponseFilter;
import com.luding.smartoa.http.response.BaseResponse;
import com.luding.smartoa.presenter.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by lizhen on 2021/3/17.
 */
public abstract class BaseModel {
    protected BasePresenter mBasePresenter;

    public BaseModel(BasePresenter basePresenter){
        mBasePresenter = basePresenter;
    }


    public abstract Disposable subscribe(int httpRequestTag);

    public <T> Disposable subscribe(Observable<BaseResponse<T>> responseObservable, int httpTag) {
        return responseObservable.subscribe(new ResponseFilter<>(mBasePresenter),new ErrorFilter(mBasePresenter,httpTag));
    }

    public Disposable subscribeBody(Observable<ResponseBody> responseObservable, int httpTag) {
        return responseObservable.subscribe(new ResponseBodyFilter(mBasePresenter,httpTag),new ErrorFilter(mBasePresenter,httpTag));
    }
}
