package com.youli.zbetuch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youli.zbetuch.BaseActivity;
import com.youli.zbetuch.R;
import com.youli.zbetuch.adapter.FunctionListGridViewAdapter;
import com.youli.zbetuch.adapter.FunctionListViewPagerAdapter;
import com.youli.zbetuch.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FunctionListActivity extends BaseActivity {

    final List<View> gridViewList = new ArrayList<View>();
    private ViewPager viewPager;
    List<Map<String, Integer>> listView = new ArrayList<Map<String, Integer>>();
    private List<ImageView> imageViewList;
    private FunctionListViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.function_list_layout);


        viewPager = (ViewPager) findViewById(R.id.viewpager_function_list);
        initData();
        init();

    }

    private void initData() {
        int[] imageId = new int[]{R.drawable.research_title};//, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title, R.drawable.research_title};
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Integer> mapView = new HashMap<String, Integer>();
            mapView.put("image", imageId[i]);
            listView.add(mapView);
        }
    }

    private void init() {
        initGridView();
        viewPagerAdapter = new FunctionListViewPagerAdapter(gridViewList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        //设置指示小点
        initPoint();
        //设置点击监听
        onClick();

    }

    private void onClick() {
        for (int i = 0; i < gridViewList.size(); i++) {
            final GridView gridView = (GridView) gridViewList.get(i);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   // ToastUtils.showToast(getApplicationContext(), "当前点击：第" + viewPager.getCurrentItem() + "页,第" + l + "个");
                    switch (viewPager.getCurrentItem()){
                        case 0:
                            switch (i){
                                case 0:
                                    Intent intent = new Intent(getApplicationContext(),QuestionNaireActivity.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }

                }
            });
        }

    }


    //根据ViewPager的页数来动态添加标识点
    private void initPoint() {
        LinearLayout ll_point = (LinearLayout) findViewById(R.id.ll_point);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 0);
        int pageNum = pageNum = gridViewList.size();
        imageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < pageNum; i++) {
            ImageView view = new ImageView(this);
            view.setLayoutParams(lp);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.shape_point_selected);
            } else {
                view.setBackgroundResource(R.drawable.shape_point_unselected);
            }
            ll_point.addView(view);
            imageViewList.add(view);
        }
    }

    private void initGridView() {
        boolean b = true;
        int temp = 0;
        while (b) {
            int result = temp + 9;
            System.out.println("result" + result);
            if (listView.size() != 0 && result < listView.size()) {
                GridView gridView = new GridView(this);
                gridView.setNumColumns(3);
                gridView.setClickable(true);
                List<Map<String, Integer>> gridviewList = new ArrayList<Map<String, Integer>>();
                for (int i = temp; i < result; i++) {
                    gridviewList.add(listView.get(i));
                }
                FunctionListGridViewAdapter gridViewAdapter = new FunctionListGridViewAdapter(this, gridviewList);
                gridView.setAdapter(gridViewAdapter);
                temp = result;
                gridViewList.add(gridView);

            } else if (result - listView.size() <= 9) {
                List<Map<String, Integer>> gridlist = new ArrayList<Map<String, Integer>>();
                for (int i = temp; i < listView.size(); i++) {
                    gridlist.add(listView.get(i));
                }
                GridView gridView = new GridView(this);
                gridView.setNumColumns(3);
                FunctionListGridViewAdapter gridViewAdapter =
                        new FunctionListGridViewAdapter(this, gridlist);
                gridView.setAdapter(gridViewAdapter);
                temp = listView.size() - 1;
                gridViewList.add(gridView);
                b = false;
            } else {
                b = false;
            }
        }
    }

    //页面滑动监听
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < imageViewList.size(); i++) {
                if (i == position) {
                    ImageView imageView = imageViewList.get(i);
                    imageView.setImageResource(R.drawable.shape_point_selected);
                } else {
                    ImageView imageView = imageViewList.get(i);
                    imageView.setImageResource(R.drawable.shape_point_unselected);
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
