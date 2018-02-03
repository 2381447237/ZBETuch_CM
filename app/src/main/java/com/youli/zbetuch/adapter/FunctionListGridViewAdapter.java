package com.youli.zbetuch.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.youli.zbetuch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FunctionListGridViewAdapter extends BaseAdapter {
    private List<Map<String,Integer>> imageViewList ;
    private final Context mContext;
    private static String TAG = "FunctionListGridViewAdapter";

    public FunctionListGridViewAdapter(Context context, List<Map<String,Integer>> list) {
        mContext = context;
        imageViewList = list;
        }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageViewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.gridview_item_image);
            holder.imageView.setImageResource(imageViewList.get(i).get("image"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    static class ViewHolder{
        ImageView imageView;
    }
}
