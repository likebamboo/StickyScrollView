/**
 * StickyScrollViewGlobalLayoutListener.java
 * StickyScrollView
 * 
 * Created by likebamboo on 2014-4-21
 * Copyright (c) 1998-2014 https://github.com/likebamboo All rights reserved.
 */

package com.likebamboo.stickyscrollview;

import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * 该接口主要用来接口当界面首次绘制时，让悬停控件处在正确的位置上。
 * 
 * @author likebamboo
 */
public class StickyScrollViewGlobalLayoutListener implements OnGlobalLayoutListener {

    private StickyScrollViewCallbacks mCallbacks;

    public StickyScrollViewGlobalLayoutListener(StickyScrollViewCallbacks mCallbacks) {
        this.mCallbacks = mCallbacks;
    }

    @Override
    public void onGlobalLayout() {
        mCallbacks.onScrollChanged();
    }

}
