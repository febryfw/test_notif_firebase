package com.root.testnotif.network;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Febry Fajarmico Wicaksono on 7/11/18.
 */

public class RetrofitClient {

    private final static String BASE_URL = "https://staging.skorpoints.com/";

    private static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
