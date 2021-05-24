package com.zli.mvpexample.view;

import com.zli.mvpexample.presenter.BaseEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lizhen on 2021/3/17.
 * 用来管理view之间的通信
 */
public class ViewManager {

    public static ViewManager getInstance() {
        return Holder.INSTANCE;
    }

    private static final  class Holder {
        private static final ViewManager INSTANCE = new ViewManager();
    }

    private final ArrayList<WeakReference<BaseView>> views = new ArrayList<>();

    /**
     * 当前view存活并且不包含的情况下加入管理
     * @param view baseview实例
     */
    public void addToViews(BaseView view) {
        synchronized (this) {
            if (view == null || !view.isViewAlive()) {
                return;
            }
            boolean isContains = false;
            Iterator<WeakReference<BaseView>> iterator = views.iterator();
            while (iterator.hasNext()) {
                WeakReference<BaseView> weakReference = iterator.next();
                if (weakReference.get() != null) {
                    if (weakReference.get() == view) {
                        isContains = true;
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }

            if (!isContains) {
                views.add(new WeakReference<>(view));
            }
        }
    }

    /**
     * 需要移除的view，在view销毁时需要调用，每次遍历到被回收的view，则移除
     * @param view 要移除的view
     */
    public void removeView(BaseView view) {
        synchronized (this) {
            Iterator<WeakReference<BaseView>> iterator = views.iterator();
            while (iterator.hasNext()) {
                WeakReference<BaseView> weakReference = iterator.next();
                if (weakReference.get() != null) {
                    if (weakReference.get() == view) {
                        iterator.remove();
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 通知当前还存活的view接收事件
     * @param baseEvent 事件
     */
    public void notifyViewChange(BaseEvent baseEvent) {
        synchronized (this) {
            Iterator<WeakReference<BaseView>> iterator = views.iterator();
            while (iterator.hasNext()) {
                WeakReference<BaseView> weakReference = iterator.next();
                if (weakReference.get() != null && weakReference.get().isViewAlive() && weakReference.get().isWindowAttach()) {
                    weakReference.get().onEvent(baseEvent);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}


