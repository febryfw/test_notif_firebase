package com.root.testnotif.network.postrequest;

import android.app.Application;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.root.testnotif.AndroidDeviceNames;
import com.root.testnotif.GetAppVersion;

import java.util.HashMap;
import java.util.Map;

public class SendTokenParam extends ParamMaster {

    private String registration_id, device_id;

    public SendTokenParam(String registration_id, String device_id) {
        this.registration_id = registration_id;
        this.device_id = device_id;
    }

    @NonNull
    @Override
    protected Map<String, String> requestParams() {
        return new HashMap<String, String>();
    }

    @Nullable
    @Override
    protected Map<String, String> header() {
         return null;
    }
}
