package com.zli.mvpexample.http.response;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zli.mvpexample.common.App;
import com.zli.mvpexample.R;
import com.zli.mvpexample.http.request.RequestConfig;
import com.zli.mvpexample.util.JsonUtils;
import com.zli.mvpexample.util.Logger;
import com.zli.mvpexample.util.NetUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lizhen on 2021/3/17.
 */

public class ResponseIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        request = changeBaseUrlWithHeader(request);

        String authValue = request.header(RequestConfig.HTTP_AUTH_HEAD);
        if (!TextUtils.isEmpty(authValue)) {
            String token = ""; //TODO
            if (!TextUtils.isEmpty(token)) {
                Logger.e("RequestToken","token is:" + token);
                request = request.newBuilder().addHeader(RequestConfig.HTTP_AUTH_KEY, token).build();
            }
        }

        String tag = request.header(RequestConfig.HTTP_TAG_HEAD);
        int httpTag = -1;
        if (tag != null) {
            try {
                httpTag = Integer.parseInt(tag);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        response = chain.proceed(request);
        if (response.code() != 200) {
            return buildErrorResponse(response,request);
        }
        return builderNewResponse(response,httpTag);
    }

    /**
     * 构造响应失败的response
     * @param response 原response
     * @param request 请求request
     * @return 重构后的response
     * @throws IOException IOException
     */
    @SuppressWarnings("unchecked")
    private Response buildErrorResponse(Response response,Request request) throws IOException {
        BaseResponse errorResponse;

        String errorTip = response.body() == null ? App.getInstance().getString(R.string.service_error): response.body().string();
        if (!NetUtil.isConnected(App.getInstance())) {
            errorTip = App.getInstance().getString(R.string.service_error);
        }
        errorResponse = new BaseResponse(
                null,
                errorTip,
                response.code());
        return new Response.Builder().code(response.code())
                .body(ResponseBody.create(response.body() == null ? MediaType.parse("text/json; charset=utf-8") : response.body().contentType(), JsonUtils.toString(errorResponse)))
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message(errorTip)
                .build();
    }

    /**
     * 构造统一格式返回的response
     * @param response 原response
     * @param httpTag 请求tag
     * @return 构造后的response
     * @throws IOException IOException
     */
    private Response builderNewResponse(Response response,int httpTag) throws IOException {
        JSONObject jsonObject = new JSONObject();
        MediaType mediaType = null;
        boolean isJson = false;
        if (response.body() != null) {
            try {
                mediaType = response.body().contentType();
                if (mediaType != null && "application/json".equals(mediaType.type())) {
                    String responseText = response.body().string();
                    if (!TextUtils.isEmpty(responseText)) {
                        if (responseText.startsWith("{") && responseText.endsWith("}")) {
                            isJson = true;
                            jsonObject = JSONObject.parseObject(responseText);
                            jsonObject.put("httpTag",httpTag);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String textFilterAfter = jsonObject.toString();
        ResponseBody responseBody = isJson ? ResponseBody.create(mediaType,textFilterAfter) : response.body();
        return response.newBuilder().body(responseBody).build();
    }


    /**
     * 根据请求参数配置请求头，动态替换baseUrl
     * @param request 原请求
     * @return 重构后的新请求
     */
    private Request changeBaseUrlWithHeader(Request request) {
        Request.Builder builder = request.newBuilder();

        // 配置多BASE URL
        String urlHeader = request.header(RequestConfig.HTTP_URL_HEAD);
        if (!TextUtils.isEmpty(urlHeader)) {
            HttpUrl url;
            builder.removeHeader(RequestConfig.HTTP_URL_HEAD);
            switch (urlHeader) {
                case RequestConfig.HTTP_URL_HEAD_VALUE_1:
                    url = HttpUrl.parse(RequestConfig.BASE_URL_1);
                    break;
                case RequestConfig.HTTP_URL_HEAD_VALUE_2:
                    url = HttpUrl.parse(RequestConfig.BASE_URL_2);
                    break;
                case RequestConfig.HTTP_URL_HEAD_VALUE_3:
                    url = HttpUrl.parse(RequestConfig.BASE_URL_3);
                    break;
                case RequestConfig.HTTP_URL_HEAD_VALUE_4:
                    url = HttpUrl.parse(RequestConfig.BASE_URL_4);
                    break;
                default:
                    throw new RuntimeException("UnSupport http header");
            }
            HttpUrl originUrl = request.url();
            assert url != null;
            HttpUrl newUrl = originUrl.newBuilder()
                    .scheme(url.scheme())
                    .host(url.host())
                    .port(url.port())
                    .build();
            request = builder.url(newUrl).build();
        } else {
            request = builder.build();
        }
        return request;
    }
}
