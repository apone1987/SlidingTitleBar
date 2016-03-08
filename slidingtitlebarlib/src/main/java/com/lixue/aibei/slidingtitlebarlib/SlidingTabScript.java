package com.lixue.aibei.slidingtitlebarlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 滑动选项卡
 * Created by xueli on 2016/3/8.
 */
public class SlidingTabScript extends HorizontalScrollView {
    /**是否充满屏幕**/
    private boolean allowWidthFull;
    /**是否禁用viewPager**/
    private boolean disableViewPager;
    /**是否禁用拉伸滑动块**/
    private boolean disableTensileSlidingBlock;
    /**滑动块**/
    private Drawable slidingBlock;

    public SlidingTabScript(Context context) {
        super(context);
    }

    public SlidingTabScript(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabScript);
        if (typedArray != null){
            int a = typedArray.getIndexCount();
            for (int i = 0;i<a;i++){
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.SlidingTabScript_allowWidthFull) {
                    allowWidthFull = typedArray.getBoolean(attr, false);
                }else if(attr == R.styleable.SlidingTabScript_disableTensileSlidingBlock){
                    disableTensileSlidingBlock = typedArray.getBoolean(attr,false);
                }else if(attr == R.styleable.SlidingTabScript_disableViewPager){
                    disableViewPager = typedArray.getBoolean(attr,false);
                }else if(attr == R.styleable.SlidingTabScript_slidingBlock){
                    slidingBlock = typedArray.getDrawable(attr);
                }
            }
            typedArray.recycle();
        }
    }

    public SlidingTabScript(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingTabScript, defStyleAttr, 0);
        if (typedArray != null){
            int a = typedArray.getIndexCount();
            for (int i = 0;i<a;i++){
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.SlidingTabScript_allowWidthFull) {
                    allowWidthFull = typedArray.getBoolean(attr, false);
                }else if(attr == R.styleable.SlidingTabScript_disableTensileSlidingBlock){
                    disableTensileSlidingBlock = typedArray.getBoolean(attr,false);
                }else if(attr == R.styleable.SlidingTabScript_disableViewPager){
                    disableViewPager = typedArray.getBoolean(attr,false);
                }else if(attr == R.styleable.SlidingTabScript_slidingBlock){
                    slidingBlock = typedArray.getDrawable(attr);
                }
            }
            typedArray.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingTabScript(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
