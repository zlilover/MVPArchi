package com.luding.smartoa.presenter;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.luding.smartoa.common.App;
import com.luding.smartoa.R;
import com.luding.smartoa.http.request.HttpRequestManager;
import com.luding.smartoa.http.request.RequestConfig;
import com.luding.smartoa.http.response.BaseResponse;
import com.luding.smartoa.util.NetUtil;
import com.luding.smartoa.view.BaseView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by lizhen on 2021/3/17.
 */
public class BasePresenter<T extends BaseView> {
    protected final WeakReference<T> mBaseView;
    final String TAG = getClass().getSimpleName();
    public int[] tags;
    private final ArrayList<Integer> refuseLoading = new ArrayList<>();

    public BasePresenter(T baseView) {
        mBaseView = new WeakReference<>(baseView);
        PresenterManager.getInstance().addToPresenters(this);
    }


    /**
     * 不希望出弹框时，重写时去除super.startRequestByCode，或者添加至非显示弹框的数组
     * @param httpCode http请求码
     */
    public void startRequestByCode(int httpCode) {
        for (int i = 0; i < refuseLoading.size(); i++) {
            if (httpCode == refuseLoading.get(i)) {
                return;
            }
        }
        if (mBaseView.get() != null && mBaseView.get().isViewAlive() && mBaseView.get().isWindowAttach()) {
            mBaseView.get().loadingDialog();
        }
    }


    /**
     *
     * @param tags 网络请求TAG，如果不需要展示loading，调用此方法
     */
    public void addToRefuseLoading(int... tags) {
        if (tags == null) {
            return;
        }
        for (int tag : tags) {
            if (!refuseLoading.contains(tag)) {
                refuseLoading.add(tag);
            }
        }
    }

    /**
     * 成功并且需要界面处理的回调的走success方法，用来做一些后台的保存工作，不需要界面处理的走successWithNoViewAlive
     * @param data 返回数据
     */
    public final void successFilter(BaseResponse data) {
        if (data == null) {
            return;
        }
        if (mBaseView.get() != null) {
            if (mBaseView.get().isViewAlive()) { // 需要界面处理回调的时候，判断当前view是否依旧存活
                if (!RequestConfig.REQUEST_SUCCESS.equals(data.getCode()) && !TextUtils.isEmpty(data.getMsg())) {
                    mBaseView.get().showRequestDes(data.getMsg());
                }
                mBaseView.get().finishLoading();
                success(data);
                return;
            }
        }
        successWithNoViewAlive(data);
    }


    /**
     *  在此方法中不允许处理与界面相关的回调
     * @param data 返回数据
     */
    protected void successWithNoViewAlive(BaseResponse data) {

    }

    protected void success(BaseResponse data) {

    }

    public void successWithResponseBody(ResponseBody responseBody,int tag) {
    }

    public void error(BaseResponse response, int tag) {
        BaseView baseView = mBaseView.get();
        if (baseView == null) {
            return;
        }
        boolean isHttpError = false;
        for (String error: RequestConfig.EXCEPTION) {
            if (error.equals(response.getMsg())) {
                if (!NetUtil.isConnected(baseView.getViewContext())) {
                    isHttpError = true;
                    showErrorToast(R.string.net_error);
                    break;
                }
            }
        }
        if (!isHttpError) {
            showErrorToast(R.string.service_error);
        }
        baseView.finishLoading();
        int code = response.getHttpResponseCode();
        if (code == 401 || code == 403) {   // UNAUTHORIZED,FORBIDDEN
            permissionError(tag);
        } else if (code == 404) {   // NOT_FOUND
            apiPathError(tag);
        } else if (code == 408 || code == 500
            || code == 502 || code == 503 || code == 504) { // REQUEST_TIMEOUT,INTERNAL_SERVER_ERROR,BAD_GATE_WAY,SERVICE_UNAVAILABLE,GATEWAY_TIMEOUT
            serviceEndError(tag);
        } else {
            appError(tag);
        }
    }

    void permissionError(int tag) {
    }

    void apiPathError(int tag) {
    }

    void serviceEndError(int tag) {
    }

    void appError(int tag) {
    }

    public void finishRequest() {
        if (mBaseView.get() != null) {
            mBaseView.get().finishLoading();
        }
    }

    public void subscribe(Object tag, Disposable subscription) {
        HttpRequestManager.getInstance().add(tag, subscription);
    }

    public void cancelAll() {
        if (tags != null) {
            HttpRequestManager.getInstance().cancelByTags(tags);
        }
    }

    public void cancelByTag(int tag) {
        HttpRequestManager.getInstance().cancel(tag);
    }

    public void destroy() {
        PresenterManager.getInstance().removePresenter(this);
    }

    @SuppressLint("CheckResult")
    private void showErrorToast(int stringId) {
        BaseView baseView = mBaseView.get();
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        if (isMainThread) {
            Toast.makeText(App.getInstance(), baseView.getViewContext().getString(stringId),Toast.LENGTH_LONG).show();
            return;
        }
        Observable.create((ObservableOnSubscribe<Boolean>) e -> e.onNext(true)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(o -> Toast.makeText(App.getInstance(), baseView.getViewContext().getString(stringId),Toast.LENGTH_LONG).show());
    }

    public void onEvent(BaseEvent baseEvent) {

    }

    public void reportEvent(BaseEvent baseEvent) {
        PresenterManager.getInstance().notifyPresenterChange(baseEvent,this);
    }
}
