package com.root.testnotif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.root.testnotif.network.RetrofitEndpoint;
import com.root.testnotif.network.UnsafeOkHttpClient;
import com.root.testnotif.network.postrequest.SendTokenParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvFirebaseToken, tvReqResponse;
    private EditText etUserToken, etUrl;
    private Button btnSave, btnSend;
    private RadioGroup radioGroup;
    private RadioButton rbHttp, rbHttps;

    private BroadcastReceiver broadcastReceiver;

    private String endpointUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUi();
        initCntrl();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Register Broadcast Listener
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (tvFirebaseToken != null){
                        tvFirebaseToken.setText(SharedPref
                                .getInstance(MainActivity.this)
                                .getString(SharedPref.Key.STRING_FIREBASE_TOKEN)
                        );
                    }
                }
            };
        }

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("firebase-token"));

        if (!SharedPref.getInstance(getApplicationContext()).getString(SharedPref.Key.STRING_FIREBASE_TOKEN, "").isEmpty()){
            Log.d("Firebase", "Token: " + SharedPref.getInstance(this).getString(SharedPref.Key.STRING_FIREBASE_TOKEN));
            tvFirebaseToken.setText(SharedPref.getInstance(getApplicationContext()).getString(SharedPref.Key.STRING_FIREBASE_TOKEN));
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void initUi(){
        setContentView(R.layout.activity_main);

        tvReqResponse = (TextView) findViewById(R.id.tvReqResponse);
        tvFirebaseToken = (TextView) findViewById(R.id.tvToken);
        etUserToken = (EditText) findViewById(R.id.etUserToken);
        etUrl = (EditText) findViewById(R.id.etUrl);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSend = (Button) findViewById(R.id.btnSend);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbHttps = (RadioButton) findViewById(R.id.rbHttps);
        rbHttp = (RadioButton) findViewById(R.id.rbHttp);

        etUrl.setText(SharedPref.getInstance(this).getString(SharedPref.Key.STRING_URL, ""));
        etUserToken.setText(SharedPref.getInstance(this).getString(SharedPref.Key.STRING_USER_TOKEN, ""));
    }

    private void initCntrl(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etUrl.getText().toString().isEmpty()
                        || !etUserToken.getText().toString().isEmpty()
                        || !tvFirebaseToken.getText().toString().isEmpty()){

                    RetrofitEndpoint request = initializeRetrofit();

                    if (initializeRetrofit() != null){
                        tvReqResponse.setVisibility(View.VISIBLE);
                        tvReqResponse.setText("Sending your request...");
                        //Firebase Token: fca0913d9e131d1e7d20ba3ab36c68e4b374aa2264d1ea2376cee36925043b63
                        SendTokenParam param = new SendTokenParam(tvFirebaseToken.getText().toString().trim(), getDeviceId());

                        Map<String, String> headerMap = new HashMap<>();
                        headerMap.put("Authorization", "Token " + etUserToken.getText().toString());
                        headerMap.put("USER-AGENT", getUserAgent());
                        headerMap.put("Content-Type", "application/json");

                        Call<ResponseBody> sendTokenReq = request.sendToken(endpointUrl, headerMap, param);
                        sendTokenReq.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    tvReqResponse.setText(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                tvReqResponse.setText(t.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(MainActivity.this, "Please provide correct URL", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    tvReqResponse.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Fill all data before send", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSaveUserToken = false;
                boolean isSaveUrl = false;

                if (!etUserToken.getText().toString().isEmpty()){
                    SharedPref.getInstance(MainActivity.this).put(SharedPref.Key.STRING_USER_TOKEN, etUserToken.getText().toString().trim());
                    isSaveUserToken = true;
                }

                if (!etUrl.getText().toString().isEmpty()){
                    SharedPref.getInstance(MainActivity.this).put(SharedPref.Key.STRING_URL, etUrl.getText().toString().trim());
                    isSaveUrl = true;
                }

                if (isSaveUserToken && isSaveUrl){
                    Toast.makeText(MainActivity.this, "URL and User Saved", Toast.LENGTH_SHORT).show();

                } else if (isSaveUrl && !isSaveUserToken){
                    Toast.makeText(MainActivity.this, "URL Saved", Toast.LENGTH_SHORT).show();

                } else if (!isSaveUrl && isSaveUserToken){
                    Toast.makeText(MainActivity.this, "User Saved", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Nothing Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private RetrofitEndpoint initializeRetrofit(){
        String baseUrl, completeUrl;
        int charIndex = -1;

        completeUrl = etUrl.getText().toString();
        int radioGroupSelection = radioGroup.getCheckedRadioButtonId();

        if (radioGroupSelection == rbHttps.getId()){
            baseUrl = "https://" + completeUrl.substring(0, completeUrl.indexOf('/') + 1);

        } else if (radioGroupSelection == rbHttp.getId() ){
            baseUrl = "http://" + completeUrl.substring(0, completeUrl.indexOf('/') + 1);

        } else {
            return null;
        }

        endpointUrl = completeUrl.substring(completeUrl.indexOf('/') + 1, completeUrl.length());
        charIndex = completeUrl.indexOf('/');

        if (charIndex != -1 && !completeUrl.contains("https://") && !completeUrl.contains("http://")){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(RetrofitEndpoint.class);

        } else {
            return null;
        }

    }

    private String getUserAgent(){
        AndroidDeviceNames androidDeviceNames = new AndroidDeviceNames(this);
        return "Skor/3 Android|"
                + androidDeviceNames.getDeviceName()
                + "|"
                + androidDeviceNames.getAPIVerison()
                + "|"
                + GetAppVersion.getInstance(this).getAppVersionCode();
    }

    private String getDeviceId(){
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
