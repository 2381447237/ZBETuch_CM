package com.youli.zbetuch.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;


public class FunctionListViewPagerAdapter extends PagerAdapter {


    private List<View> mLists;
    private String TAG = "FunctionListViewPagerAdapter";

    public FunctionListViewPagerAdapter(List<View> gridViewList) {
        this.mLists = gridViewList;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mLists.get(position));
        return mLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mLists.get(position));
    }
}