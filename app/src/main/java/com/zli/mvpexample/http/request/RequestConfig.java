package com.zli.mvpexample.http.request;

/**
 * Created by lizhen on 2021/3/17.
 */
public class RequestConfig {
    public static final String REQUEST_SUCCESS = "200";
    public static final String HTTP_AUTH_HEAD = "AUTH";
    public static final String HTTP_AUTH_KEY = "Authorization";
    public static final String HTTP_AUTH_VALUE = "YES";
    public static final String HTTP_AUTH_REQUEST = HTTP_AUTH_HEAD + ":" + HTTP_AUTH_VALUE; // 代表接口需要鉴权
    public static final String HTTP_TAG_HEAD= "TAG";
    public static final String HTTP_URL_HEAD= "HTTP_URL_HEAD";

    public static final String HTTP_URL_HEAD_VALUE_1 = "SERVICE_1";
    public static final String HTTP_URL_HEAD_VALUE_2 = "SERVICE_2";
    public static final String HTTP_URL_HEAD_VALUE_3 = "SERVICE_3";
    public static final String HTTP_URL_HEAD_VALUE_4 = "SERVICE_4";
    public static final String HTTP_URL_HEAD_SERVICE_1 = HTTP_URL_HEAD + ":" + HTTP_URL_HEAD_VALUE_1;
    public static final String HTTP_URL_HEAD_SERVICE_2 = HTTP_URL_HEAD + ":" + HTTP_URL_HEAD_VALUE_2;
    public static final String HTTP_URL_HEAD_SERVICE_3 = HTTP_URL_HEAD + ":" + HTTP_URL_HEAD_VALUE_3;
    public static final String HTTP_URL_HEAD_SERVICE_4 = HTTP_URL_HEAD + ":" + HTTP_URL_HEAD_VALUE_4;

    // 当项目有多个baseUrl时设置用，支持一共5个baseURL
    public static String BASE_URL_1 = "";
    public static String BASE_URL_2 = "";
    public static String BASE_URL_3 = "";
    public static String BASE_URL_4 = "";

    public final static String SOCKET_TIME_OUT_EXCEPTION = "SOCKET_TIME_OUT_EXCEPTION";
    public final static String CONNECT_TIME_OUT_EXCEPTION = "CONNECT_TIME_OUT_EXCEPTION";
    public final static String UNKNOWN_HOST_EXCEPTION = "UNKNOWN_HOST_EXCEPTION";
    public final static String NOROUTE_TO_HOST_EXCEPTION = "NOROUTE_TO_HOST_EXCEPTION";
    public final static String[] EXCEPTION = new String[]{SOCKET_TIME_OUT_EXCEPTION, CONNECT_TIME_OUT_EXCEPTION, UNKNOWN_HOST_EXCEPTION, NOROUTE_TO_HOST_EXCEPTION};

    public final static class RequestCode{
        public final static int CHECK_UPDATE = 1001;
        public final static int GET_PLATFORM = 1002;
    }
}
