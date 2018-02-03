package com.youli.zbetuch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youli.zbetuch.R;
import com.youli.zbetuch.bean.MainPagerGvInfo;
import com.youli.zbetuch.view.MyListView;

import java.util.List;

/**
 * Created by liutao on 2017/8/4.
 */

public class MainPagerGvAdapter extends BaseAdapter{

    private List<MainPagerGvInfo> data;
    private Context context;
    private MainPagerLvAdapter lvAdapter;

    public MainPagerGvAdapter(Context context, List<MainPagerGvInfo> data) {
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

            view= LayoutInflater.from(context).inflate(R.layout.item_main_pager_gv,null,false);

            vh.titleTv= (TextView) view.findViewById(R.id.item_gv_title_tv);
            vh.contentLv= (MyListView) view.findViewById(R.id.item_gv_lv);

            view.setTag(vh);
        }else{

            vh= (ViewHolder) view.getTag();

        }

        final MainPagerGvInfo info=data.get(i);

        vh.titleTv.setText(info.getTitle());
        lvAdapter=new MainPagerLvAdapter(context,info.getLvInfoList());

        vh.contentLv.setAdapter(lvAdapter);
//        vh.contentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context,info.getLvInfoList().get(i).getLvContent(),Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }

    class ViewHolder{

        TextView titleTv;
        MyListView contentLv;
    }

}
