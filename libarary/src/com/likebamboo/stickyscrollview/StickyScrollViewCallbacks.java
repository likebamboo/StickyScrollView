/**
 * StickyScrollViewCallbacks.java
 * StickyScrollView
 * 
 * Created by likebamboo on 2014-4-21
 * Copyright (c) 1998-2014 https://github.com/likebamboo All rights reserved.
 */

package com.likebamboo.stickyscrollview;

import android.view.MotionEvent;
import android.view.View;

import com.likebamboo.stickyscrollview.animation.AnimatorProxy;
import com.likebamboo.stickyscrollview.animation.ViewHelper;

/**
 * <p>
 * ScrollView滚动时的回调接口，
 * <p>
 * 通过计算ScrollView滚动时悬停控件应该绘制的位置来绘制相应的View
 * 
 * @author likebamboo
 */
public class StickyScrollViewCallbacks implements StickyScrollView.Callbacks {
    private static final float CLICK_DISTANCE = 3;

    /**
     * 悬停的View
     */
    private View mStickyView = null;

    /**
     * 当mStickyView不处于悬浮状态时的停靠的View
     */
    private View mPlaceholderView = null;

    /**
     * ScrollView控件
     */
    private StickyScrollView mObservableScrollView = null;

    /**
     * 悬停控件的临界控件【非必须】，如果设置了该控件，当ScrollView滚动到该控件时，悬停控件会被这个控件顶出界面。
     */
    private View mEndSticyView;

    /**
     * 
     */
    private boolean mEnableSticky = true;

    /**
     * touchDown时ScrollView滚动的坐标
     */
    private float mTouchDownY = Float.MIN_VALUE;

    /**
     * 从touchDown到touchUp,ScrollView滚动的距离
     */
    private float mScrollDistanceY = 0F;

    public StickyScrollViewCallbacks(View stickyView, View placeholderView,
            StickyScrollView observableScrollView) {
        this(stickyView, placeholderView, null, observableScrollView);
    }

    public StickyScrollViewCallbacks(View stickyView, View placeholderView, View endView,
            StickyScrollView observableScrollView) {
        this.mStickyView = stickyView;
        this.mPlaceholderView = placeholderView;
        this.mObservableScrollView = observableScrollView;
        this.mEndSticyView = endView;
        // 监听onTouch事件有两方面的考虑，scrollView的滚动与点击事件
        mStickyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 记录下当前的滚动位置
                        mTouchDownY = mObservableScrollView.getScrollY();
                        break;
                    case MotionEvent.ACTION_MOVE:// ScrollView滚动的过程中，记录滚动的差值
                        float disY = Math.abs(mObservableScrollView.getScrollY() - mTouchDownY);
                        // 如果当前差值大于之前记录差值，将差值替换
                        if (disY > mScrollDistanceY) {
                            mScrollDistanceY = disY;
                        }
                        break;
                }

                float translateY = ViewHelper.getTranslationY(mStickyView);
                if (AnimatorProxy.NEEDS_PROXY) {
                    translateY = getTop(mStickyView);
                }

                // touch的坐标都是相对于本控件的，所以需要做一次转换
                // 构建一个新的MotionEvent事件
                MotionEvent newEvent = MotionEvent.obtain(event);
                newEvent.setLocation(newEvent.getX(), newEvent.getY() + translateY);
                mObservableScrollView.dispatchTouchEvent(newEvent);
                if (newEvent != null) {
                    newEvent.recycle();
                }

                // 如果是滚动，那么不让其触发onClick 事件
                if (mScrollDistanceY > CLICK_DISTANCE && event.getAction() == MotionEvent.ACTION_UP) {
                    mScrollDistanceY = 0F;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onScrollChanged() {
        // 首先计算移动的距离
        int translationY = calTranslationY();
        /**
         * <pre>
         * 针对2.3及以下版本，直接用layout方法改变其位置
         * 也许有人说，可以用[NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)兼容动画啊。
         * 可惜用NineOldAndroids动画执行后，控件的可点击区域还是在原来的地方啊【2.3及以下版本】。
         * github上好多人都报告了这个问题，但是没办法解决。这是android2.3及以下版本的系统问题，不是NineOldAndroids开源控件的问题
         * (之前我就是这么做的，发现不行后，才使用了layout方法，感兴趣的可以尝试下。)
         */
        if (AnimatorProxy.NEEDS_PROXY) {
            int l = mStickyView.getLeft();
            int r = mStickyView.getRight();
            mStickyView.layout(l, translationY, r, translationY + mStickyView.getHeight());
        } else {
            ViewHelper.setTranslationY(mStickyView, translationY);
        }
    }

    /**
     * 计算需要移动的位置
     * 
     * @return
     */
    private int calTranslationY() {
        // 如果不能浮动，那么固定在placeHolderView所在的位置
        if (!mEnableSticky) {
            return getTop(mPlaceholderView) - mObservableScrollView.getScrollY();
        }
        float translationY = Math.max(0,
                getTop(mPlaceholderView) - mObservableScrollView.getScrollY());
        if (mEndSticyView != null) {
            /**
             * 如果有滚动的临界区域 ，当滚动到指定的临界控件{mEndStricyView}之后，临界的控件将浮动顶上去(仿IOS效果)
             */
            if (mObservableScrollView.getScrollY() + mStickyView.getHeight() > getTop(mEndSticyView)) {
                // 如果临界控件已经将浮动控件完全顶出ScrollView，那么就让View固定在屏幕的上面
                if (mObservableScrollView.getScrollY() > getTop(mEndSticyView)) {
                    translationY = -mStickyView.getHeight();
                } else {
                    // 否则临界控件将其顶上去
                    translationY = getTop(mEndSticyView) - mObservableScrollView.getScrollY()
                            - mStickyView.getHeight();
                }
            }
        }
        return Math.round(translationY);
    }

    /**
     * 获取控件顶部的坐标
     * 
     * @return
     */
    private int getTop(View v) {
        return v.getTop();
    }

    /**
     * 设置是否允许悬停
     */
    public void setEnableSticky(boolean enable) {
        mEnableSticky = enable;
    }

    /**
     * 获取是否允许悬停
     */
    public boolean getEnableSticky() {
        return mEnableSticky;
    }
}
