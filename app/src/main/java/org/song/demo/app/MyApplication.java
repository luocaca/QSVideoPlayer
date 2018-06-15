package org.song.demo.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import org.song.demo.BuildConfig;

/**
 *
 */

public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

        Beta.canNotifyUserRestart = true;

//      Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();


        init();


//        Toast.makeText(this, (1 / 0) + "", Toast.LENGTH_SHORT).show();


    }

    private void init() {
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        //设置为开发设备
        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        Bugly.init(this, "e3c38e56e5", BuildConfig.DEBUG);


    }
}
