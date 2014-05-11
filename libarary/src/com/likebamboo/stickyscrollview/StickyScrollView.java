/**
 * StickyScrollView.java
 * StickyScrollView
 * 
 * Created by likebamboo on 2014-4-21
 * Copyright (c) 1998-2014 https://github.com/likebamboo All rights reserved.
 */

package com.likebamboo.stickyscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义的ScrollView
 * <p>
 * 通过监听ScrollView的滚动事件执行特定的回调方法【动态改变可浮动View的位置】
 * 
 * @author likebamboo
 */
public class StickyScrollView extends ScrollView {
    private List<Callbacks> mCallbacks;

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    /**
     * <p>
     * 复写onInterceptTouchEvent是用来解决ScrollView与ViewPager之前滚动事件冲突的，
     * <p>
     * 实际项目可根据需要选择是否需要这段代码
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public StickyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 滚动时回调
        if (mCallbacks != null && mCallbacks.size() != 0) {
            for (Callbacks item : mCallbacks) {
                item.onScrollChanged();
            }
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void addCallbacks(Callbacks listener) {
        if (mCallbacks == null) {
            mCallbacks = new ArrayList<Callbacks>();
        }
        mCallbacks.add(listener);
    }

    public static interface Callbacks {
        public void onScrollChanged();
    }
}
