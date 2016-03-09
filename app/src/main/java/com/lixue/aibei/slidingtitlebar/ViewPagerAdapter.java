package com.lixue.aibei.slidingtitlebar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> nameStr;

    public ViewPagerAdapter(Context context,List<String> nameStr){
        this.mContext = context;
        this.nameStr = nameStr;
    }

    @Override
    public int getCount() {
        return nameStr.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.viewpager_main,null);
        TextView nameTV = (TextView) v.findViewById(R.id.pageview_tv);
        nameTV.setText(nameStr.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
