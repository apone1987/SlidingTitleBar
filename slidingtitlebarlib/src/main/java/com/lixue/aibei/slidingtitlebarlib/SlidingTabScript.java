package com.lixue.aibei.slidingtitlebarlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 滑动选项卡
 * Created by xueli on 2016/3/8.
 */
public class SlidingTabScript extends HorizontalScrollView {
    private static final String TAG = "SlidingTabScript";

    /**是否充满屏幕**/
    private boolean allowWidthFull;
    /**是否禁用viewPager**/
    private boolean disableViewPager;
    /**是否禁用拉伸滑动块**/
    private boolean disableTensileSlidingBlock;
    /**滑动块**/
    private Drawable slidingBlock;

    private float currentPositionOffset;	//当前位置偏移量
    private int lastScrollX;//上一次的位置
    private int lastOffset;
    private boolean start;

    private ViewPager viewPager;//viewPager
    private int currentIndex;//当前位置
    private ViewGroup tabsLayout;//标题项布局
    private List<View> tabsView;//标题项集合
    private ViewPager.OnPageChangeListener onPageChangeListener;//页面改变监听器
    private TabViewFactory tabViewFactory;//tabview生成器

    public SlidingTabScript(Context context) {
        super(context);
    }

    public SlidingTabScript(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        removeAllViews();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabScript);
        if (typedArray != null){
            int a = typedArray.getIndexCount();
            for (int i = 0;i<a;i++){
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.SlidingTabScript_allowWidthFull) {
                    allowWidthFull = typedArray.getBoolean(attr, false);
                    Log.i(TAG, "allowWidthFull: " + (!allowWidthFull ? "false" : "true"));
                }else if(attr == R.styleable.SlidingTabScript_disableTensileSlidingBlock){
                    disableTensileSlidingBlock = typedArray.getBoolean(attr,false);
                    Log.i(TAG, "disableTensileSlidingBlock: " + (!disableTensileSlidingBlock ? "false" : "true"));
                }else if(attr == R.styleable.SlidingTabScript_disableViewPager){
                    disableViewPager = typedArray.getBoolean(attr,false);
                    Log.i(TAG, "disableViewPager: " + (!disableViewPager ? "false" : "true"));
                }else if(attr == R.styleable.SlidingTabScript_slidingBlock){
                    slidingBlock = typedArray.getDrawable(attr);
                }
            }
            typedArray.recycle();
        }
    }

    public SlidingTabScript(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        removeAllViews();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingTabScript, defStyleAttr, 0);
        if (typedArray != null){
            int a = typedArray.getIndexCount();
            for (int i = 0;i<a;i++){
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.SlidingTabScript_allowWidthFull) {
                    allowWidthFull = typedArray.getBoolean(attr, false);
                    Log.i(TAG, "allowWidthFull: " + (!allowWidthFull ? "false" : "true"));
                }else if(attr == R.styleable.SlidingTabScript_disableTensileSlidingBlock){
                    disableTensileSlidingBlock = typedArray.getBoolean(attr,false);
                    Log.i(TAG, "disableTensileSlidingBlock: " + (!disableTensileSlidingBlock ? "false" : "true"));
                }else if(attr == R.styleable.SlidingTabScript_disableViewPager){
                    disableViewPager = typedArray.getBoolean(attr,false);
                    Log.i(TAG, "disableViewPager: " + (!disableViewPager ? "false" : "true"));
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**如果tabs充满屏幕的宽，每个tab的宽度要平均分配**/
//        if (allowWidthFull && tabsLayout != null){
//            for (int i = 0 ;i < getChildCount();i ++){
//                ViewGroup.LayoutParams lp = getChildAt(i).getLayoutParams();
//                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                getChildAt(i).setLayoutParams(lp);
//            }
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!allowWidthFull) return;
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout == null){
            Log.i(TAG,"tabsLayout is null");
            return;
        }
        if (tabsLayout.getChildCount() <= 0){
            Log.i(TAG,"tabsLayout's childCount is <= 0");
            return;
        }
        if (tabsView == null){
            tabsView = new ArrayList<View>();
        }else{
            tabsView.clear();
        }
        for (int j = 0 ;j < tabsLayout.getChildCount();j++){
            tabsView.add(tabsLayout.getChildAt(j));
        }

        ajustChildWidthWithParent(tabsView,getMeasuredWidth()-tabsLayout.getPaddingLeft()-tabsLayout.getPaddingRight(),widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    /**重新测量标题布局（tabs）位置**/
    private void ajustChildWidthWithParent(List<View> tabsView,int mWidth,int widthMeasureSpec,int heigthMeasureSpec){
        /**去掉所有子view的外边距**/
        for(View v : tabsView){
            if (v.getLayoutParams() instanceof MarginLayoutParams){
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
                mWidth -= lp.leftMargin + lp.rightMargin;
            }
        }

        /**去掉宽度大于平均宽度的view后 再次计算平均宽度**/
        int avgWidth = mWidth / tabsView.size();
        int bigTabcount = tabsView.size();
        while (true){
            Iterator<View> iterator = tabsView.iterator();
            while (iterator.hasNext()){
                View view = iterator.next();
                if (view.getMeasuredWidth()>avgWidth){
                    mWidth -= view.getMeasuredWidth();
                    bigTabcount --;
                    iterator.remove();
                }
            }
            if (bigTabcount <= 0) break;
            avgWidth = mWidth / bigTabcount;
            boolean end = true;
            for (View v : tabsView){
                if (v.getMeasuredWidth() > avgWidth){
                    end = false;
                }
            }
            if (end) break;
        }

        /**修改宽度小于新的平均宽度的view**/
        for(View view : tabsView){
            if(view.getMeasuredWidth() < avgWidth){
                Log.i(TAG,"avgWidth : " + avgWidth +", 子view的宽度 : " + view.getMeasuredWidth());
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = avgWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                if(layoutParams instanceof MarginLayoutParams){
                    measureChildWithMargins(view, widthMeasureSpec, 0, heigthMeasureSpec, 0);
                }else{
                    measureChild(view, widthMeasureSpec, heigthMeasureSpec);
                }
            }
        }
//        int singleWidth = mWidth/tabsView.size();
//        Log.i(TAG,"sigleWidth :" + singleWidth);
//        for (View view : tabsView){
//            ViewGroup.LayoutParams lp = view.getLayoutParams();
//            lp.width = singleWidth;
//            view.setLayoutParams(lp);
//        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disableViewPager) return;
        if (tabsLayout != null && tabsLayout.getChildCount() > 0 && slidingBlock != null){
            ViewGroup tabsLayout = getTabsLayout();
            View currentView = tabsLayout.getChildAt(currentIndex);
            if (currentView != null){
                float left = currentView.getLeft();
                float right = currentView.getRight();
                if (currentPositionOffset > 0f && currentIndex < tabsLayout.getChildCount() - 1) {
                    View nextTab = tabsLayout.getChildAt(currentIndex + 1);
                    if(nextTab != null){
                        final float nextTabLeft = nextTab.getLeft();
                        final float nextTabRight = nextTab.getRight();
                        left = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * left);
                        right = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * right);
                    }
                }
                // 不拉伸
                if(disableTensileSlidingBlock){
                    int center = (int) (left + (right-left)/2);
                    left = center - slidingBlock.getIntrinsicWidth()/2;
                    right = center + slidingBlock.getIntrinsicWidth()/2;
                }
                slidingBlock.setBounds((int) left, getHeight() - slidingBlock.getIntrinsicHeight(), (int) right, getHeight());
                slidingBlock.draw(canvas);
//                invalidate();
            }
        }
    }

    /**获取tabs布局，例如《排行，精品，分类，管理》的tabs布局**/
    private ViewGroup getTabsLayout(){
        if (tabsLayout ==  null) {
            if (getChildCount() > 0) {
                this.tabsLayout = (ViewGroup) getChildAt(0);
            } else {
                removeAllViews();
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                //信new的linearlayout还没有布局参数
//            ViewGroup.LayoutParams lp = linearLayout.getLayoutParams();
//            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                this.tabsLayout = linearLayout;
                addView(tabsLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
            }
        }
        return tabsLayout;
    }

    /**得到tab的数量**/
    public int getTabCount(){
        if (tabsLayout != null){
            return tabsLayout.getChildCount();
        }
        return 0;
    }

    /**得到tab的名字**/
    public List<String> getTabName(){
        getTabsLayout();
        List<String> listStr = new ArrayList<>();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0){
            for (int w = 0;w < tabsLayout.getChildCount();w ++){
                String name = (String) ((TextView) tabsLayout.getChildAt(w)).getText();
                listStr.add(name);
                Log.i(TAG,"tag name :" + name);
            }
        }
        return listStr;
    }

    /**设置ViewPager**/
    public void setViewPager(final ViewPager viewPager){
        if (disableViewPager) return;
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int positon, float positionOffset, int positionOffsetPixels) {
                ViewGroup tabsLayout = getTabsLayout();
                if(positon < tabsLayout.getChildCount()){
                    View view = tabsLayout.getChildAt(positon);
                    if(view != null){
                        currentIndex = positon;
                        currentPositionOffset = positionOffset;
                        Log.i(TAG,"position:" + positon);
                        /**从当前位置滚动到偏移量的位置**/
                        int offset = (int)positionOffset * (view.getWidth() + getLeftMargin(view) + getRightMargin(view));
                        Log.i(TAG,"offset :" + offset);
                        //计算新的X坐标
                        int newScrollX = view.getLeft() + offset - getLeftMargin(view);
                        Log.i(TAG,"newScrollX :" + newScrollX);
                        if (positon > 0 || offset > 0) {
                            newScrollX -= getWidth()/2 - getOffset(view.getWidth())/2;
                            Log.i(TAG,"getWidth()/2:"+getWidth()/2 + ",getoffset()/2:"+ getOffset(view.getWidth())/2 + ",newScrollx:" + newScrollX);
                        }

                        //如果同上次X坐标不一样就执行滚动
                        if (newScrollX != lastScrollX) {
                            lastScrollX = newScrollX;
                            Log.i(TAG,"newScrollX : " + newScrollX +", lastScrollX :" + lastScrollX);
                            scrollTo(newScrollX, 0);
                        }
                        invalidate();
                    }
                }
                if(onPageChangeListener != null){
                    onPageChangeListener.onPageScrolled(positon, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
                if (onPageChangeListener != null){
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(onPageChangeListener != null){
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        //当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent view重新调用他的onMeasure onLayout来对重新设置自己位置。
        //特别的当view的layoutparameter发生改变，并且它的值还没能应用到view上，这时候适合调用这个方法。
        requestLayout();
    }
    /**指定的tab被选中**/
    private void selectTab(int position){
        ViewGroup tabsLayout = getTabsLayout();
        if (position > -1 && tabsLayout != null && position < tabsLayout.getChildCount()){
            for (int w =0 ;w < tabsLayout.getChildCount();w ++){
                View thisTab = tabsLayout.getChildAt(w);
                thisTab.setSelected(position==w);
            }

        }
//        currentIndex = position;
//        viewPager.setCurrentItem(position);
    }

    private int getLeftMargin(View view){
        if (view.getLayoutParams() instanceof MarginLayoutParams){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            return  lp.leftMargin;
        }
        return 0;
    }

    private int getRightMargin(View view){
        if (view.getLayoutParams() instanceof MarginLayoutParams){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            return  lp.rightMargin;
        }
        return 0;
    }


    /**
     * 获取偏移量
     */
    private int getOffset(int newOffset){
        if(lastOffset < newOffset){
            if(start){
                lastOffset += 1;
                return lastOffset;
            }else{
                start = true;
                lastOffset += 1;
                return lastOffset;
            }
        }if(lastOffset > newOffset){
            if(start){
                lastOffset -= 1;
                return lastOffset;
            }else{
                start = true;
                lastOffset -= 1;
                return lastOffset;
            }
        }else{
            start = true;
            lastOffset = newOffset;
            return lastOffset;
        }
    }

    /**
     * 设置不使用ViewPager
     * @param disableViewPager 不使用ViewPager
     */
    public void setDisableViewPager(boolean disableViewPager) {
        this.disableViewPager = disableViewPager;
        if(viewPager != null){
            viewPager.setOnPageChangeListener(onPageChangeListener);
            viewPager = null;
        }
        requestLayout();
    }

    /**
     * 设置TabView生成器
     * @param tabViewFactory
     */
    public void setTabViewFactory(TabViewFactory tabViewFactory) {
        this.tabViewFactory = tabViewFactory;
        tabViewFactory.addTabs(getTabsLayout(), viewPager!=null?viewPager.getCurrentItem():0);
    }

    /**
     * TabView生成器
     */
    public interface TabViewFactory{
        /**
         * 添加tab
         * @param parent
         * @param defaultPosition
         */
        public void addTabs(ViewGroup parent, int defaultPosition);
    }
}
