StickyScrollView
========================
相信大家都知道有很多让ListView分组Header浮动悬停的开源控件，比如：**[StickyListHeaders](https://github.com/emilsjolander/StickyListHeaders)**、**[pinned-section-listview](https://github.com/beworker/pinned-section-listview)**,而本项目就是要在ScrollView中实现类似的功能。  
StickyScrollView 是一个让ScrollView同样支持浮动悬停的控件(该控件支持android2.2(api level 8)及以上的版本)。
效果图如下(gif屏幕录制，有点卡顿，实际使用不会的)：  
![sticyScrollView](https://raw.github.com/likebamboo/StickyScrollView/master/ScreenCapture/screen.gif)

构建
----
该项目基于Eclipse构建。如果你使用的IDE是Eclipse，请：
* 在Eclipse中菜单栏中选择 **File, Import, Existing projects into workspace...**
* 选中你clone下来的repository的根目录，然后点击"import"导入项目（包括StickyScorllView 库项目和 StickyScrollViewSample示例项目）。
* 导入之后如果有错误，请使用菜单栏中的 **Project - Clean** 清理下项目。
* 在apk目录下有示例程序的[安装包](https://github.com/likebamboo/StickyScrollView/tree/master/apk)，你也可以先下载安装到手机看下效果。

使用方法
---------

#### 布局文件： 

``` xml
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent" >

        <com.likebamboo.stickyscrollview.StickyScrollView
            android:id="@+id/scroll_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" >

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical" >
                <!--第一个可以浮动悬停View 的锚点(不处于悬停时所在的位置)-->
                <View
                    android:id="@+id/placeholder"
                    android:layout_width="match_parent"
                    android:layout_height="48dp" />

                <View
                    android:id="@+id/view1"
                    style="@style/Item.Top" />

                <View
                    android:id="@+id/view2"
                    style="@style/Item.Bottom" />

                <View style="@style/Item.Bottom.Alt" />
                
                <!--第二个可以浮动悬停View 的锚点-->
                <View
                    android:id="@+id/placeholder2"
                    android:layout_width="match_parent"
                    android:layout_height="100dp" />

                <View style="@style/Item.Bottom" />

                <View style="@style/Item.Bottom.Alt" />

                <View style="@style/Item.Bottom" />

                <View style="@style/Item.Bottom.Alt" />
            </LinearLayout>
        </com.likebamboo.stickyscrollview.StickyScrollView>
        <!--第一个可以浮动悬停的View-->
        <TextView
            android:id="@+id/sticky"
            android:layout_width="match_parent"
            android:layout_height="48dp"
            android:background="#ffffff"
            android:gravity="center"
            android:text="@string/stop_sticky"
            android:textColor="@android:color/black" />

        <!--第二个可以浮动悬停的View-->
        <TextView
            android:id="@+id/sticky2"
            android:layout_width="match_parent"
            android:layout_height="100dp"
            android:background="#ffffff"
            android:gravity="center"
            android:text="@string/sticky_item"
            android:textColor="@android:color/black" />
    </FrameLayout>
```


#### Activity
  
``` java
        // 初始化控件
        mStickyScrollView = (StickyScrollView)findViewById(R.id.scroll_view);
        mStickyView = (TextView)findViewById(R.id.sticky);
        mPlaceholderView = findViewById(R.id.placeholder);

        
        mCallbacks = new StickyScrollViewCallbacks(mStickyView, mPlaceholderView,
                mPlaceholderView2, mStickyScrollView);

        mStickyScrollView.addCallbacks(mCallbacks);

        mStickyScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new StickyScrollViewGlobalLayoutListener(mCallbacks));

```  

其他
------
在 [这里](https://github.com/emilsjolander/StickyScrollViewItems) 你可以找到与本项目具有相似功能的开源项目[StickyScrollViewItems](https://github.com/emilsjolander/StickyScrollViewItems)。  
两个项目实现的功能是一样的，相比而言:  
* 本项目在使用的时候相对比较复杂，要写的代码会比较多，[StickyScrollViewItems](https://github.com/emilsjolander/StickyScrollViewItems) 项目使用起来比较简单。
* 本项目支持子控件可悬停状态的动态切换，你可以在代码中通过设置``mCallbacks.setEnableSticky(!enableSticky);``来动态控制某个View是否悬停并且不影响其他子控件的悬停状态。  
* 本项目中可以方便的控制悬停控件的悬停范围,可控制悬停控件从哪个View到哪个View之前悬停,具体可参照 StickyScrollViewCallbacks 的第二个构造函数:
``` java  
    // 不设置endView将一直悬停，直到ScrollView滚动到最底部
    public StickyScrollViewCallbacks(View stickyView, View placeholderView, View endView,StickyScrollView observableScrollView) 
```
* 其他 

总之，两个项目各有优缺点，大家可以根据自己的喜好选择啦。
