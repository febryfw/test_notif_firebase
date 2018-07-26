package com.root.testnotif.network;

import com.root.testnotif.network.postrequest.SendTokenParam;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Febry Fajarmico Wicaksono on 7/11/18.
 *
 *
 * Suggestion 1:
 * # Good Practice
 * base url: https://futurestud.io/api/
 * endpoint: my/endpoint
 * Result:   https://futurestud.io/api/my/endpoint
 *
 * # Bad Practice
 * base url: https://futurestud.io/api
 * endpoint: /my/endpoint
 * Result:   https://futurestud.io/my/endpoint
 */

public interface RetrofitEndpoint {
    //push_notifications/api/apn/
    @POST
    Call<ResponseBody> sendToken(
            @Url String url,
            @HeaderMap Map<String, String> header,
            @Body SendTokenParam body);

    @Multipart
    @POST("profiles/api/token-auth/")
    Call<ResponseBody> login(
            @HeaderMap Map<String, String> header,
            @PartMap Map<String, RequestBody> param);
}
