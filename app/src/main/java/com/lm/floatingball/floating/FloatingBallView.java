package com.lm.floatingball.floating;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.lm.floatingball.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lm
 * @Create 2019/4/1
 * @Description 悬浮球View
 */
public class FloatingBallView extends LinearLayout implements View.OnClickListener {

    private FloatingBallHelper floatingBallHelper;

    private List<FloatingItem> floatingItemList = new ArrayList<>();

    private ImageButton mainButton;

    public FloatingBallView(Context context) {
        this(context, null);
    }

    public FloatingBallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        mainButton = new ImageButton(getContext());
        mainButton.setBackgroundResource(R.drawable.icon_floating_ball);
        LayoutParams mainButtonParams = new LayoutParams(ConvertUtils.dp2px(32), ConvertUtils.dp2px(32));
        mainButtonParams.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
        mainButton.setLayoutParams(mainButtonParams);
        mainButton.setOnClickListener(this);
        addView(mainButton);
    }

    private int lastX, lastY;
    private boolean isMove;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                if (Math.abs(offsetX) >= 1 || Math.abs(offsetY) >= 1) {
                    floatingBallHelper.updateViewLayout(offsetX, offsetY);
                    lastX = x;
                    lastY = y;
                    isMove = true;
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    isMove = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (v == mainButton) {
            if (getChildAt(getChildCount() - 1) == mainButton) {
                addFloatingItemView();
            } else {
                removeFloatingItemView();
            }
        } else if (v.getTag() != null && v.getTag() instanceof FloatingItem) {
            ((FloatingItem) v.getTag()).onClick();
            removeFloatingItemView();
        } else {
            if (getChildAt(getChildCount() - 1) != mainButton) {
                removeFloatingItemView();
            }
        }
    }

    private void removeFloatingItemView() {
        for (int i = 0; i < floatingItemList.size(); i++) {
            removeView(findViewWithTag(floatingItemList.get(i)));
        }
    }

    private void addFloatingItemView() {
        for (int i = 0; i < floatingItemList.size(); i++) {
            FloatingItem item = floatingItemList.get(i);

            ImageButton itemView = new ImageButton(getContext());
            itemView.setBackgroundResource(item.getDrawableRes());
            LayoutParams params = new LayoutParams(ConvertUtils.dp2px(32), ConvertUtils.dp2px(32));
            params.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
            itemView.setLayoutParams(params);
            itemView.setOnClickListener(this);

            addView(itemView);

            itemView.setTag(item);
        }
    }

    public void addFloatingItem(FloatingItem item) {
        floatingItemList.add(item);
        postInvalidate();
    }

    public void setFloatingBallHelper(FloatingBallHelper floatingBallHelper) {
        this.floatingBallHelper = floatingBallHelper;
    }
}
