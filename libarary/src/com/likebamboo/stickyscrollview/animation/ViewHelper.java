/**
 * ViewHelper.java
 * StickyScrollView
 * 
 * Created by likebamboo on 2014-4-21
 * Copyright (c) 1998-2014 https://github.com/likebamboo All rights reserved.
 */

package com.likebamboo.stickyscrollview.animation;

import android.annotation.SuppressLint;
import android.view.View;

import static com.likebamboo.stickyscrollview.animation.AnimatorProxy.NEEDS_PROXY;
import static com.likebamboo.stickyscrollview.animation.AnimatorProxy.wrap;

/**
 * 动画代理，来源于NineOldAndroids
 * 
 * @author likebamboo
 */
public final class ViewHelper {
    private ViewHelper() {
    }

    public static float getTranslationY(View view) {
        return NEEDS_PROXY ? wrap(view).getTranslationY() : Honeycomb.getTranslationY(view);
    }

    public static void setTranslationY(View view, float translationY) {
        if (NEEDS_PROXY) {
            wrap(view).setTranslationY(translationY);
        } else {
            Honeycomb.setTranslationY(view, translationY);
        }
    }

    @SuppressLint("NewApi")
    private static final class Honeycomb {
        static float getTranslationY(View view) {
            return view.getTranslationY();
        }

        static void setTranslationY(View view, float translationY) {
            view.setTranslationY(translationY);
        }
    }
}
