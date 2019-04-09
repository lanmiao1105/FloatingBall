package com.lm.floatingball.floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ObjectUtils;

/**
 * @Author Lm
 * @Create 2019/4/1
 * @Description 悬浮球帮助类
 */
public class FloatingBallHelper {

    private Context context;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private FloatingBallView floatingBallView;

    public FloatingBallHelper(Context context) {
        ObjectUtils.requireNonNull(context);
        this.context = context.getApplicationContext();
    }

    public void onCreate() {
        getWindowManager().addView(getFloatingBallView(), getLayoutParams());
    }

    public void onDestroy() {
        getWindowManager().removeView(floatingBallView);
        floatingBallView = null;
        params = null;
        context = null;
    }

    public void addFloatingItem(FloatingItem item) {
        ObjectUtils.requireNonNull(item);
        getFloatingBallView().addFloatingItem(item);
    }

    public void updateViewLayout(int offsetX, int offsetY) {
        WindowManager.LayoutParams params = getLayoutParams();
        params.x += offsetX;
        params.y += offsetY;
        getWindowManager().updateViewLayout(getFloatingBallView(), params);
    }

    public WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        if (params == null) {
            params = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.x = 1000;
            params.y = 1000;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        return params;
    }

    public FloatingBallView getFloatingBallView() {
        if (floatingBallView == null) {
            floatingBallView = new FloatingBallView(context);
            floatingBallView.setLayoutParams(getLayoutParams());
            floatingBallView.setFloatingBallHelper(this);
        }
        return floatingBallView;
    }
}
