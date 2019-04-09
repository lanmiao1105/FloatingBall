package com.lm.floatingball;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.lm.floatingball.floating.FloatingBallService;

/**
 * @Author Lm
 * @Create 2019/4/1
 * @Description MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACTION_ACCESSIBILITY_SETTINGS = 1000;
    private static final int REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION = 1001;


    TextView tv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            tv.setText("开启悬浮窗权限以便显示悬浮球");
            btn.setText("前往开启悬浮窗权限");
            btn.setOnClickListener(v -> {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION);
            });
        } else {
            checkAndStartFloatingBallService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ACTION_ACCESSIBILITY_SETTINGS) {
            checkAndStartFloatingBallService();
        } else if (requestCode == REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            checkAndStartFloatingBallService();
        }
    }

    private void checkAndStartFloatingBallService() {
        if (isAccessibilityEnable()) {
            startService(new Intent(this, FloatingBallService.class));
            finish();
        } else {
            tv.setText("开启辅助设置以便启动悬浮球的功能");
            btn.setText("前往开启辅助设置");
            btn.setOnClickListener(v -> {
                startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), REQUEST_CODE_ACTION_ACCESSIBILITY_SETTINGS);
            });
        }
    }

    private boolean isAccessibilityEnable() {
        String serviceName = getPackageName() + "/" + FloatingBallService.class.getName();
        LogUtils.d(serviceName);

        try {
            int enable = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            LogUtils.d(enable);
            if (enable == 1) {
                String setting = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                LogUtils.d(setting);
                if (!TextUtils.isEmpty(setting)) {
                    TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
                    splitter.setString(setting);
                    while (splitter.hasNext()) {
                        String accessibilityService = splitter.next();
                        if (accessibilityService.equalsIgnoreCase(serviceName)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Settings.SettingNotFoundException e) {

        }
        return false;
    }

}
