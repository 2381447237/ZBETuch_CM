package com.youli.zbetuch.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch.BaseActivity;
import com.youli.zbetuch.R;
import com.youli.zbetuch.adapter.PersonalInfoListAdapter;
import com.youli.zbetuch.bean.PersonalInfoBean;
import com.youli.zbetuch.bean.QuestionNaireBean;
import com.youli.zbetuch.utils.OkHttpUtils;
import com.youli.zbetuch.utils.ProgressUtils;
import com.youli.zbetuch.utils.SharedPreferencesUtils;
import com.youli.zbetuch.utils.ToastUtils;
import com.youli.zbetuch.view.XListView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonalInfoListActivity extends BaseActivity implements AdapterView.OnItemClickListener,XListView.IXListViewListener,View.OnClickListener{

    private Context mContext = this;
    private ImageView ivChoice;
    private List<PersonalInfoBean> personalInfoBeanList;
    private XListView listView_personal_info;
    private TextView tv_total_num;
    //private int isApplyLand = 1;
    private int pageSize = 30;
    private int isComplete = 0;
    private TextView tv_nodatas;
    private static final int GET_JSON = 10000;
    private static final int RESPONSE_NULL = 10001;
    private static final int FAILURE = 10002;


   // private RadioGroup rg_complete;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case GET_JSON:

                    //ProgressUtils.dismissMyProgressDialog(PersonalInfoListActivity.this);
                    String jsonBody = (String) msg.obj;
                    parseJsonToGson(jsonBody);
                    if (personalInfoBeanList != null) {
                        personalInfoListAdapter = new PersonalInfoListAdapter(personalInfoBeanList);
                        listView_personal_info.setAdapter(personalInfoListAdapter);
                        personalInfoListAdapter.notifyDataSetChanged();
                        listView_personal_info.setSelection(pageSize - 32);
                        if (personalInfoBeanList.size() != 0) {
                            tv_total_num.setText(personalInfoBeanList.get(0).getRecordCount());
                            listView_personal_info.setVisibility(View.VISIBLE);
                            tv_nodatas.setVisibility(View.GONE);
                        } else {
                            tv_total_num.setText("0");
                            listView_personal_info.setVisibility(View.GONE);
                            tv_nodatas.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case RESPONSE_NULL:

                   // ProgressUtils.dismissMyProgressDialog(PersonalInfoListActivity.this);
                    break;
                case FAILURE:

                   // ProgressUtils.dismissMyProgressDialog(PersonalInfoListActivity.this);
                    break;
            }
        }
    };

    private PersonalInfoListAdapter personalInfoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_info_list_layout);
        initView();
    }

    private void initView() {
        listView_personal_info = (XListView) findViewById(R.id.lv_personal_info);
        listView_personal_info.setOnItemClickListener(this);
        listView_personal_info.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        listView_personal_info.setXListViewListener(this);
        tv_nodatas = (TextView) findViewById(R.id.tv_no_datas);
        tv_total_num = (TextView) findViewById(R.id.tv_all_num);
        ivChoice= (ImageView) findViewById(R.id.iv_choice);
        ivChoice.setOnClickListener(this);
        getJsonBody(isComplete, pageSize);
//        rg_complete= (RadioGroup) findViewById(R.id.rg_check);
//        rg_complete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.rb_unchecked) {
//                    isComplete = 0;
//                    rg_complete.check(R.id.rb_unchecked);
//                } else if (i == R.id.rb_checked) {
//                    isComplete = 1;
//                    rg_complete.check(R.id.rb_checked);
//                }
//
//                getJsonBody(isComplete, pageSize);
//            }
//        });
//        if(isComplete==1){
//            rg_complete.check(R.id.rb_checked);
//        }else{
//            rg_complete.check(R.id.rb_unchecked);
//        }

    }

    //获取 json 数据
    public void getJsonBody(final int isComplete, final int pageSize) {
       // ProgressUtils.showMyProgressDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * PageIndex=0
                 * PageSize=30
                 * Is_ApplyLand=0
                 * Is_Complete=0
                 */
                OkHttpUtils.okHttpAsynGet(OkHttpUtils.BaseUrl + "/JSON/Json_GetLand_Respnedents.aspx?" + "PageIndex=0&PageSize=" + pageSize +"&Is_Complete=" + isComplete, SharedPreferencesUtils.getString("cookies", mContext).toString().trim(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("getPersonalInfoList", "onFailure: " + e.getMessage().toString());
                        Message msg=Message.obtain();
                        msg.what=FAILURE;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null) {

                            String jsonBody = response.body().string();
                            Message msg = Message.obtain();
                            msg.what = GET_JSON;
                            msg.obj = jsonBody;
                            mHandler.sendMessage(msg);
                        } else if (response == null) {
                            Message msg=Message.obtain();
                            msg.what=RESPONSE_NULL;
                            mHandler.sendMessage(msg);
                            //ToastUtils.showToast(mContext, "人物信息列表获取失败");
                        }
                    }
                });

            }
        }).start();
    }

    //解析 json 数据
    public void parseJsonToGson(String jsonBody) {
        Gson gson = new Gson();

        Log.e("2017/9/28","jsonBody=="+jsonBody);

        if(!TextUtils.equals("false",jsonBody)) {

            personalInfoBeanList = gson.fromJson(jsonBody, new TypeToken<List<PersonalInfoBean>>() {
            }.getType());

        }else{

            tv_total_num.setText("0");

        }
    }

    public void loadMore() {

        pageSize += 30;

        if(personalInfoBeanList.size()==0){
            ToastUtils.showToast(mContext, "已加载完成");
            return;
        }

        if (pageSize >= Integer.parseInt(personalInfoBeanList.get(0).getRecordCount())) {
            ToastUtils.showToast(mContext, "已加载完成");
        } else {

            getJsonBody(isComplete, pageSize);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(isComplete==0){
            showPersionDetailInfo(personalInfoBeanList.get(i-1));
        }else if(isComplete==1){
            Intent intent=new Intent(mContext,QuestionNaireDetailsActivity.class);
            intent.putExtra("wenjuan",(QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan")));
            intent.putExtra("personalInfo",personalInfoBeanList.get(i-1));
            intent.putExtra("isComplete",isComplete);
            startActivity(intent);
        }

    }

    private void showPersionDetailInfo(final PersonalInfoBean info) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_persion_detailinfo,null);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();

        TextView name= (TextView) view.findViewById(R.id.dialog_persion_tv_name);
        TextView sex= (TextView) view.findViewById(R.id.dialog_persion_tv_sex);
        TextView identity= (TextView) view.findViewById(R.id.dialog_persion_tv_identity);
        TextView town= (TextView) view.findViewById(R.id.dialog_persion_tv_town);//街道
        TextView village= (TextView) view.findViewById(R.id.dialog_persion_tv_village);//村委
        TextView address= (TextView) view.findViewById(R.id.dialog_persion_tv_address);//联系地址
        TextView phone= (TextView) view.findViewById(R.id.dialog_persion_tv_phone);

        name.setText(info.getApplicantName());
        sex.setText(info.getSex());
        identity.setText(info.getApplicantIDNo());
        town.setText(info.getTownName());
        village.setText(info.getVillageName());
        address.setText(info.getRegisteredAddress());
        phone.setText(info.getCellPhoneNumber());

        Button research= (Button) view.findViewById(R.id.btn_start_research);
        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext,QuestionNaireDetailsActivity.class);
                intent.putExtra("wenjuan",(QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan")));
                intent.putExtra("personalInfo",info);
                intent.putExtra("isComplete",isComplete);
                startActivityForResult(intent,100);
                dialog.dismiss();
            }
        });
        Button history= (Button) view.findViewById(R.id.btn_research_history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"历史问卷",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100&&resultCode==200){

            getJsonBody(isComplete, pageSize);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {//刷新
        getJsonBody(isComplete,  pageSize);
        if(personalInfoListAdapter!=null) {
            personalInfoListAdapter.notifyDataSetChanged();
        }
        //停止刷新
        listView_personal_info.stopRefresh();
        listView_personal_info.setRefreshTime("刚刚");
    }

    @Override
    public void onLoadMore() {//加载更多

        loadMore();
        if(personalInfoListAdapter!=null) {
            personalInfoListAdapter.notifyDataSetChanged();
        }
        listView_personal_info.stopLoadMore();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_choice:
                choiceDialog();
                break;

        }

    }

    private void choiceDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_choice,null);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button cancelBtn= (Button) view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button sureBtn= (Button) view.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJsonBody(isComplete, pageSize);
                dialog.dismiss();
            }
        });

        final RadioGroup rg_complete= (RadioGroup) view.findViewById(R.id.rg_complete);
        rg_complete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_no_complete) {
                    isComplete = 0;
                    rg_complete.check(R.id.rb_no_complete);
                } else if (i == R.id.rb_is_complete) {
                    isComplete = 1;
                    rg_complete.check(R.id.rb_is_complete);
                }
            }
        });

        final RadioGroup rg_applyLand= (RadioGroup) view.findViewById(R.id.rg_applyLand);
        rg_applyLand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_no_applyLand) {
                   // isApplyLand = 0;
                    rg_applyLand.check(R.id.rb_no_applyLand);
                } else if (i == R.id.rb_is_applyLand) {
                  //  isApplyLand = 1;
                    rg_applyLand.check(R.id.rb_is_applyLand);
                }
            }
        });

        if(isComplete==1){
            rg_complete.check(R.id.rb_is_complete);
        }else{
            rg_complete.check(R.id.rb_no_complete);
        }

//        if(isApplyLand==1){
//            rg_applyLand.check(R.id.rb_is_applyLand);
//        }else{
//            rg_applyLand.check(R.id.rb_no_applyLand);
//        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("2017/9/27","haha=====");
        Intent intent=new Intent();
        setResult(200,intent);
        finish();
    }
}
