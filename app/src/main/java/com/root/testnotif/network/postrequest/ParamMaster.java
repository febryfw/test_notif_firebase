package com.root.testnotif.network.postrequest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class ParamMaster {

    public ParamMaster() {
    }

    private Map<String, String> getDefaultHeader(){
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public Map<String, RequestBody> getFormParams(){
        if (requestParams() != null && requestParams().size() > 0){
            Map<String, RequestBody> formParam  = new HashMap<>();

            for (String s : requestParams().keySet()){
                formParam.put(s, RequestBody.create(MultipartBody.FORM, requestParams().get(s)));
            }

            return formParam;

        } else {
            throw new RuntimeException("Request Params Cannot be null");
        }
    }

    public Map<String, String> getHeader(){
        if (header() != null){
            return header();

        } else {
            return getDefaultHeader();
        }
    }

    @NonNull
    protected abstract Map<String, String> requestParams();

    @Nullable
    protected abstract Map<String, String> header();
}
