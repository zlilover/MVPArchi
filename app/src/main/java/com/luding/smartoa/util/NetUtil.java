package com.luding.smartoa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * Created by lizhen on 2021/3/17.
 */
public class NetUtil {
    /**
     * 无网络
     */
    public static final int TYPE_NO_NETWORK = -1;
    /**
     * WIFI网络
     */
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    /**
     * 移动网络
     */
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;

    public static ConnectivityManager getConnectivityManager(Context c) {
        return (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static WifiLock wifilock = null;

    /**
     * 使用Wifi连接, wifi锁
     */
    public static void startUseWifi(Context c) {
        WifiManager wifimanager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        if (wifimanager != null) {
            wifilock = wifimanager.createWifiLock(WifiManager.WIFI_MODE_FULL, "meq");
            if (wifilock != null) {
                wifilock.acquire();
            }
        }
    }

    /**
     * 释放wifi锁
     */
    public static void stopUseWifi() {
        if (wifilock != null && wifilock.isHeld()) {
            wifilock.release();
        }
        wifilock = null;
    }

    /**
     * 是否存在已经连接上的网络, 不论是wifi/cmwap/cmnet还是其他
     *
     * @return
     */
    public static boolean isConnected(Context c) {
        if (c == null) {
            return false;
        }
        NetworkInfo ni = getConnectivityManager(c).getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * 检查WIFI是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isWifiConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 检查移动网络是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isMobileConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 检查当前网络类型
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return TYPE_NO_NETWORK;
    }

}
