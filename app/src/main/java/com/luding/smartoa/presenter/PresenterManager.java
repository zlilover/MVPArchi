package com.luding.smartoa.presenter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lizhen on 2021/3/17.
 */
class PresenterManager {
    static PresenterManager getInstance() {
        return Holder.INSTANCE;
    }

    private static final  class Holder {
        private static PresenterManager INSTANCE = new PresenterManager();
    }

    private final ArrayList<BasePresenter> presenters = new ArrayList<>();

    /**
     * 当前view存活并且不包含的情况下加入管理
     * @param presenter presenter实例
     */
    void addToPresenters(BasePresenter presenter) {
        synchronized (presenters) {
            boolean isContains = false;
            for (BasePresenter basePresenter : presenters) {
                if (basePresenter == presenter) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                presenters.add(presenter);
            }
        }
    }

    /**
     * 移除不再使用的presenter
     * @param basePresenter 需要移除的presenter
     */
    void removePresenter(BasePresenter basePresenter) {
        synchronized (presenters) {
            Iterator<BasePresenter> iterator = presenters.iterator();
            while (iterator.hasNext()) {
                BasePresenter presenter = iterator.next();
                if (presenter == basePresenter) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 通知当前presenter接收事件
     * @param baseEvent 事件
     */
    void notifyPresenterChange(BaseEvent baseEvent,BasePresenter reporter) {
        synchronized (presenters) {
            for (BasePresenter presenter : presenters) {
                if (reporter != presenter) {
                    presenter.onEvent(baseEvent);
                }
            }
        }
    }
}
