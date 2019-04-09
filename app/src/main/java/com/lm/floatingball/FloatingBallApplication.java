package com.lm.floatingball;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

/**
 * @Author Lm
 * @Create 2019/4/1
 * @Description Application
 */
public class FloatingBallApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());
        LogUtils.getConfig().setGlobalTag("FB");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.d("onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtils.d("onTerminate");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.d("onTrimMemory", "level=" + level);
    }

}
