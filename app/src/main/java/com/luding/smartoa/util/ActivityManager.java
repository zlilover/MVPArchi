package com.luding.smartoa.util;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by lizhen on 2019/7/29.
 */
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private final static String TAG = ActivityManager.class.getSimpleName();

    private ActivityManager() {

    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    private static class SingleInstanceHolder {
        private static final ActivityManager INSTANCE = new ActivityManager();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.empty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        if (activityStack == null || activityStack.empty()) {
            return;
        }
        Activity activity = activityStack.lastElement();
        Logger.e(TAG,"finish activity for " + activity.getClass().getName());
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null) {
            return;
        }
        Iterator iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 清楚所有activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                finishActivity();
            }
            activityStack.clear();
            Logger.e(TAG,"current stack size is:" + activityStack.size());
        }
    }

    /**
     * 清楚所有activity
     */
    /**
     * 清楚所有activity
     */
    public void finishAllActivityExcept(Class cls) {
        if (activityStack != null) {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity.getClass().getName().equals(cls.getName())) {
                    continue;
                }
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 退出应用程序
     *
     *            isBackground 是否开启启后台运行?
     */
    public void AppExit( Boolean isBackground) {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!isBackground) {
                System.exit(0);
            }
        }
    }

}
