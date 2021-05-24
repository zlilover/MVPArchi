package com.zli.mvpexample.widget;

import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zli.mvpexample.util.DensityUtil;
import com.zli.mvpexample.util.Logger;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lizhen on 2019/10/10.
 */
public class CommonToast {
    private Field sField_TN;
    private Field sField_TN_Handler;
    private TextView mTvMeesage;
    private Toast mToast;
    private Context mContext;

    public static CommonToast getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CommonToast INSTANCE = new CommonToast();
    }

    public CommonToast init(Context context, int layoutId) {
        if (!(context instanceof Application)) {
            throw new RuntimeException("only application context can by accepted");
        }
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        initView(view);
        mToast = new Toast(mContext);
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mToast.setView(view);
        Logger.e("CommonToast", mToast.getView() + "-----------" + mToast);
        return this;
    }

    private void reflect(int width, int height) {
        try {
            Object mTN;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    params.width = width;
                    params.height = height;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }


    public void showText(String text,int duration) {
        config(duration, text,0,0);
    }

    public void showText(String text,Context context,int duration,int minWidth,int height) {
        config(duration, text,minWidth,height);
    }

    private void config(int duration, String text,int minWidth,int height) {
        mTvMeesage.setText(text);
        int[] textWH = getTextWH(text);
        int textWidth = textWH[0];
        int popWidth = textWidth + 40 * 2;
        if (popWidth < minWidth) {
            popWidth = minWidth;
        }

        int defaultHeight = textWH[1] + 20 * 2;
        if (defaultHeight <= height) {
            defaultHeight = height;
        }
        hook(mToast);
        reflect(popWidth,defaultHeight);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mToast.cancel();
            }
        }, duration);

    }

    private void initView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) view;
            int childCount = root.getChildCount();
            TextView[] textViewCount = new TextView[1];
            for (int index = 0;index < childCount;index++) {
                if (root.getChildAt(index) instanceof TextView) {
                    if (textViewCount[0] != null) {
                        throw new RuntimeException("Toast layout must contains one and only one TextView to display message");
                    }
                    textViewCount[0] = (TextView) root.getChildAt(index);
                }
            }
            mTvMeesage = textViewCount[0];
        } else if (!(view instanceof TextView)){
            throw new RuntimeException("Toast layout must contains one and only one TextView to display message");
        } else {
            mTvMeesage = (TextView) view;
        }
    }

    private int[] getTextWH(String text) {
        TextPaint paint = new TextPaint();
        paint.measureText(text);
        Rect rect = new Rect();
        paint.setTextSize(DensityUtil.dip2px(mContext, 14));
        paint.getTextBounds(text, 0, text.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return new int[]{w, h};
    }

    private void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SafelyHandlerWarpper extends Handler {
        private Handler impl;

        private SafelyHandlerWarpper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                impl.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
