package com.youli.zbetuch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.youli.zbetuch.R;
import com.youli.zbetuch.bean.MainPagerGvInfo;

import java.util.List;

/**
 * Created by liutao on 2017/8/4.
 */

public class MainPagerLvAdapter extends BaseAdapter{

    private List<MainPagerGvInfo.MainPagerLvInfo> data;
    private Context context;

    public MainPagerLvAdapter(Context context, List<MainPagerGvInfo.MainPagerLvInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;

        if(view==null){

            vh=new ViewHolder();

            view= LayoutInflater.from(context).inflate(R.layout.item_main_pager_gv_lv,null,false);

            vh.contentTv= (TextView) view.findViewById(R.id.item_lv_content_tv);
            vh.timeTv= (TextView) view.findViewById(R.id.item_lv_time_tv);

            view.setTag(vh);
        }else{

            vh= (ViewHolder) view.getTag();

        }

        MainPagerGvInfo.MainPagerLvInfo info=data.get(i);

        vh.contentTv.setText(info.getLvContent());
        vh.timeTv.setText(info.getLvTime());

        return view;
    }

    class ViewHolder{

        TextView contentTv,timeTv;

    }

}
