package com.youli.zbetuch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.youli.zbetuch.BaseActivity;
import com.youli.zbetuch.R;
import com.youli.zbetuch.adapter.QuestionNaireAdapter;
import com.youli.zbetuch.bean.QuestionNaireBean;
import com.youli.zbetuch.utils.OkHttpUtils;
import com.youli.zbetuch.utils.ProgressUtils;
import com.youli.zbetuch.view.XListView;
import com.zhy.http.okhttp.callback.StringCallback;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class QuestionNaireActivity extends BaseActivity implements AdapterView.OnItemClickListener,XListView.IXListViewListener{


    private final int GET=10000;
    private final int refresh=10001;
    private final int loadmore=10002;

    private String questionUrl= OkHttpUtils.BaseUrl+"/JSON/Json_Get_Question_Master.aspx";
    private List<QuestionNaireBean> qbList=new ArrayList<>();
    private XListView lv;
    private QuestionNaireAdapter questionNaireAdapter;
    private TextView numTv;

    private Handler mhandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressUtils.dismissMyProgressDialog(QuestionNaireActivity.this);

           qbList.clear();
            qbList.addAll((List<QuestionNaireBean> )msg.obj);
            numTv.setText("共有"+qbList.size()+"篇");

            dataSetAdapter(qbList);

            switch (msg.what){

                case GET:

                    break;
                case refresh:
                            lv.stopRefresh();
                            lv.setRefreshTime("刚刚");
                    break;
                case loadmore:
                    lv.stopLoadMore();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.questionnaire_layout);

        numTv= (TextView) findViewById(R.id.questionnaire_num_tv);

        lv = (XListView) findViewById(R.id.lv_questionnaire);
        lv.setOnItemClickListener(this);
        lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        lv.setXListViewListener(this);

        getData("");
    }


    // ListView 的 item 的点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,PersonalInfoListActivity.class);
        intent.putExtra("wenjuan",qbList.get(i-1));
        startActivityForResult(intent,100);

    }

    private void getData(final String state){
        ProgressUtils.showMyProgressDialog(this);
        //PageIndex=0&PageSize=10
        com.zhy.http.okhttp.OkHttpUtils.post().url(questionUrl).addParams("PageIndex",0+"").addParams("PageSize",10+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(QuestionNaireActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                ProgressUtils.dismissMyProgressDialog(QuestionNaireActivity.this);
                if(TextUtils.equals("refresh",state)){
                    lv.stopRefresh();
                }else if(TextUtils.equals("loadmore",state)){
                    lv.stopLoadMore();
                }
            }
            @Override
            public void onResponse(final String response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Gson gson=new Gson();
//                        qbList=gson.fromJson(response,new TypeToken<List<QuestionNaireBean>>(){}.getType());
//
//                        Log.e("2017/9/27","数量=="+qbList.get(0).getSurveyNum());
//
//                        numTv.setText("共有"+qbList.size()+"篇");
//                        if(questionNaireAdapter==null) {
//                            questionNaireAdapter = new QuestionNaireAdapter(QuestionNaireActivity.this, qbList);
//                            lv.setAdapter(questionNaireAdapter);
//                            questionNaireAdapter.notifyDataSetChanged();
//                        }else{
//                            questionNaireAdapter.notifyDataSetChanged();
//                        }
//
//                        if(TextUtils.equals("refresh",state)){
//                            lv.stopRefresh();
//                            lv.setRefreshTime("刚刚");
//                        }else if(TextUtils.equals("loadmore",state)){
//                            lv.stopLoadMore();
//                        }


                        Message msg=Message.obtain();
                        Gson gson=new Gson();
                        msg.obj=gson.fromJson(response,new TypeToken<List<QuestionNaireBean>>(){}.getType());
                        if(TextUtils.equals("",state)){

                            msg.what=GET;

                        }else if(TextUtils.equals("refresh",state)){

                            msg.what=refresh;

                        }else if(TextUtils.equals("loadmore",state)){

                            msg.what=loadmore;

                        }

                        mhandler.sendMessage(msg);

                   }
                });

            }
        });


    }

    private void dataSetAdapter(List<QuestionNaireBean> data){

                                if(questionNaireAdapter==null) {
                            questionNaireAdapter = new QuestionNaireAdapter(QuestionNaireActivity.this, data);
                            lv.setAdapter(questionNaireAdapter);
                            questionNaireAdapter.notifyDataSetChanged();
                        }else{
                            questionNaireAdapter.notifyDataSetChanged();
                        }

    }

    @Override
    public void onRefresh() {//刷新
        getData("refresh");
    }

    @Override
    public void onLoadMore() {//加载更多
        getData("loadmore");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100){

            Log.e("2017/9/27","233=====");

            getData("");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
