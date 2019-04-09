package com.lm.floatingball.floating;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.lm.floatingball.R;

/**
 * @Author Lm
 * @Create 2019/4/1
 * @Description 悬浮球Service
 */
public class FloatingBallService extends AccessibilityService {

    FloatingBallHelper floatingBallHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        floatingBallHelper = new FloatingBallHelper(this);
        floatingBallHelper.onCreate();

        floatingBallHelper.addFloatingItem(new FloatingItem() {
            @Override
            int getDrawableRes() {
                return R.drawable.icon_home;
            }

            @Override
            void onClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    performGlobalAction(GLOBAL_ACTION_HOME);
                }
            }
        });

        floatingBallHelper.addFloatingItem(new FloatingItem() {
            @Override
            int getDrawableRes() {
                return R.drawable.icon_back;
            }

            @Override
            void onClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    performGlobalAction(GLOBAL_ACTION_BACK);
                }
            }
        });

        floatingBallHelper.addFloatingItem(new FloatingItem() {
            @Override
            int getDrawableRes() {
                return R.drawable.icon_task;
            }

            @Override
            void onClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    performGlobalAction(GLOBAL_ACTION_RECENTS);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        floatingBallHelper.onDestroy();
        startService(new Intent(this, FloatingBallService.class));
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
}
