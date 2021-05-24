package com.zli.mvpexample.http.request;

import com.zli.mvpexample.BuildConfig;
import com.zli.mvpexample.http.response.ResponseIntercepter;
import com.zli.mvpexample.util.Logger;
import com.zli.mvpexample.util.Util;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lizhen on 2021/3/17.
 */

public class HttpRequestBuilder {
    private Retrofit mRetrofit;
    private OkHttpClient.Builder mBuilder;
    private static final int DEFAULT_CONN_TIMEOUT = 5000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;

    private static class LazyLoader {
        private static final HttpRequestBuilder INSTANCE = new HttpRequestBuilder();
    }


    private HttpRequestBuilder() {
        mBuilder = new OkHttpClient.Builder();
        mBuilder.connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.MILLISECONDS);
        mBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        mBuilder.addInterceptor(new ResponseIntercepter());
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            mBuilder.sslSocketFactory(sslSocketFactory);
            mBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor((message) -> {
            Logger.e("RequestMessage", "---->" + Util.decode(message));
        });
        loggingInterceptor.setLevel(level);
        mBuilder.addInterceptor(loggingInterceptor);
        mRetrofit = new Retrofit.Builder()
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.APP_URL)
                .build();
    }

    public static HttpRequestBuilder getInstance() {
        return LazyLoader.INSTANCE;
    }

    public <T> T createService(Class cls) {
        return (T) mRetrofit.create(cls);
    }
}
