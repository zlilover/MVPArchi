package com.zli.mvpexample.http.request;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.collection.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Created by lizhen on 2021/3/17.
 */

public class HttpRequestManager {
    private static HttpRequestManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;

    public static HttpRequestManager getInstance() {

        if (sInstance == null) {
            synchronized (HttpRequestManager.class) {
                if (sInstance == null) {
                    sInstance = new HttpRequestManager();
                }
            }
        }
        return sInstance;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private HttpRequestManager() {
        maps = new ArrayMap<>();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void add(Object tag, Disposable subscription) {
        maps.put(tag, subscription);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void cancelByTags(int[] tags) {
        if (tags != null) {
            for (int tag: tags) {
                cancel(tag);
            }
        }
    }
}
