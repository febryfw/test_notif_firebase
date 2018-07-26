package com.root.testnotif;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created By Febry Fajarmico Wicaksono
 *
 * This class used to return App version name dan App version code
 */
public class GetAppVersion {

    private PackageInfo packageInfo;

    public GetAppVersion(Context context) {
        try {
            this.packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            );

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Package not found");
        }
    }

    public static synchronized GetAppVersion getInstance(Context context){
        return new GetAppVersion(context);
    }

    public String getAppVersionName(){
      if (packageInfo == null){
          throw new RuntimeException("Package info null");
      }

      return packageInfo.versionName;
    }

    public String getAppVersionCode(){
        if (packageInfo == null){
            throw new RuntimeException("Package info null");
        }

        return String.valueOf(packageInfo.versionCode);
    }
}
