package com.luding.smartoa.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luding.smartoa.R;
import com.luding.smartoa.util.DensityUtil;

/**
 * Created by lizhen on 2019/10/10.支持带文本的加载框，动画自己在布局中解决
 */
public class LoadingDialog extends PopupWindow {
    private Context context;
    private TextView mTvMeesage;

    public LoadingDialog createLoadingDialog(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog,null);
        setContentView(view);
        setWidth(DensityUtil.dip2px(context,60));
        setHeight(DensityUtil.dip2px(context,60));
        setOutsideTouchable(false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(false);

        if (view instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) view;
            int childCount = root.getChildCount();
            TextView[] textViewCount = new TextView[1];
            for (int index = 0;index < childCount;index++) {
                if (root.getChildAt(index) instanceof TextView) {
                    if (textViewCount[0] != null) {
                        throw new RuntimeException("Loading layout must contains one and only one TextView to display message");
                    }
                    textViewCount[0] = (TextView) root.getChildAt(index);
                }
            }
            mTvMeesage = textViewCount[0];
        }
        return this;
    }

    public void setText(String text) {
        if (mTvMeesage != null) {
            mTvMeesage.setText(text);
        }
    }

    public void show() {
        if (context == null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()) {
            return;
        }
        showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER,0,0);
    }

}
