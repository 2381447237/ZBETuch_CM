package com.youli.zbetuch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youli.zbetuch.R;
import com.youli.zbetuch.bean.PersonalInfoBean;

import java.util.List;


public class PersonalInfoListAdapter extends BaseAdapter {
    private List<PersonalInfoBean> mBeanList;

    public PersonalInfoListAdapter(List<PersonalInfoBean> list) {
        mBeanList = list;
    }

    @Override
    public int getCount() {
        return mBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        PersonalInfoBean bean = mBeanList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_info_list_item, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_person_info_name);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_person_info_address);
            viewHolder.tv_sex = (TextView) convertView.findViewById(R.id.tv_person_info_sex);
            viewHolder.tv_town_name = (TextView) convertView.findViewById(R.id.tv_person_info_town);
            viewHolder.tv_village_name = (TextView) convertView.findViewById(R.id.tv_person_info_village);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(bean.getApplicantName());
        viewHolder.tv_sex.setText(bean.getSex());
        viewHolder.tv_village_name.setText(bean.getVillageName());
        viewHolder.tv_address.setText(bean.getRegisteredAddress());
        viewHolder.tv_town_name.setText(bean.getTownName());

        if (position % 2 == 0){
            convertView.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
        }else {
            convertView.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_sex;
        TextView tv_address;
        TextView tv_town_name;
        TextView tv_village_name;
    }
}
