package com.youli.zbetuch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.youli.zbetuch.BaseActivity;
import com.youli.zbetuch.R;
import com.youli.zbetuch.bean.AnswerInfo;
import com.youli.zbetuch.bean.PersonalInfoBean;
import com.youli.zbetuch.bean.QuestionNaireBean;
import com.youli.zbetuch.utils.ProgressUtils;
import com.youli.zbetuch.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liutao on 2017/7/19.
 */

public class QuestionNaireDetailsActivity extends BaseActivity implements View.OnClickListener {

    private int isComplete;//是否调查过 1:已查 0:未查
    private String spinnerYear;
    private LinearLayout bigll;
    private List<QuestionNaireBean.QuestionDetailsList> questionDetailsList;//问卷的信息
    private List<QuestionNaireBean.QuestionDetailsList> juanInfos = new ArrayList<>();//问卷的信息
    private Button startBtn, lastBtn, nextBtn, submitBtn, allBtn, restartBtn;

    private List<QuestionNaireBean.QuestionDetailsList> questionTitleList = new ArrayList<>();//问题
    private List<QuestionNaireBean.QuestionDetailsList> answerInfo = new ArrayList<>();//选项

    private int index = 0;//问题在集合中的索引
    private int tempQuestionIndex = 0;// 用于标识上一题的题号
    // 用于保存上一步回答的题目
    private List<Integer> tempList = new ArrayList<Integer>();
    private QuestionNaireBean.QuestionDetailsList currentInfo;// 用于表示当前题


    private List<RadioButton> radioButtons = new ArrayList<>();//单选的答案
    private List<Spinner> spinners = new ArrayList<>();//选择日期的答案
    private List<AnswerInfo> answerInfo2 = new ArrayList<>();//真正要上传的答案集合

    private List<String> years = new ArrayList<>();

    //被调查人编号的ID
    private String userID;

    //http://server:91/JSON/Json_Set_Land_Respnedents.aspx
    //private String createUserUrl = com.youli.zbetuch.utils.OkHttpUtils.BaseUrl + "/JSON/Json_Set_Land_Respnedents.aspx";
    private String IdCard;//身份证
    private String UserNo;//被调查人编号
    private String cookie;

    //http://server:91/JSON/Json_Set_Question_Results.aspx
    private String uploadUrl = com.youli.zbetuch.utils.OkHttpUtils.BaseUrl + "/JSON/Json_Set_Question_Results.aspx";

    //http://192.168.11.11:91/JSON/Json_Get_Question_Results.aspx?RespondentId=22
    private String resultsUrl= com.youli.zbetuch.utils.OkHttpUtils.BaseUrl+"/JSON/Json_Get_Question_Results.aspx";
    private List<AnswerInfo> resultsList=new ArrayList<>();
    private final  int UPLOAD_SUCCESS=10000;//提交成功

    private Handler myHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

              case  UPLOAD_SUCCESS:

                Toast.makeText(QuestionNaireDetailsActivity.this, "提交完成！", Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent();
                  setResult(200,intent);
                  finish();

                  break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.questionnaire_details_activity);

        bigll = (LinearLayout) findViewById(R.id.ll);

        cookie = SharedPreferencesUtils.getString("cookies", this).toString().trim();

        questionDetailsList = ((QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan"))).getQuestionDetails();

        IdCard = ((PersonalInfoBean) getIntent().getSerializableExtra("personalInfo")).getApplicantIDNo();
        UserNo = ((PersonalInfoBean) getIntent().getSerializableExtra("personalInfo")).getId();

       // Toast.makeText(this,"ID是"+UserNo,Toast.LENGTH_SHORT).show();

        isComplete=getIntent().getIntExtra("isComplete",-1);

        initView();

        addTitle();

        if(isComplete==1){
           // Toast.makeText(this,"我是已查的",Toast.LENGTH_SHORT).show();
            getResultsInfo();
            //seeCompleteAnswer();
        }else if(isComplete==0){
           // Toast.makeText(this,"我是未查的",Toast.LENGTH_SHORT).show();

        }

    }

    private void addTitle() {

        String number = ((QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan"))).getQUESTION_NUM();
        String date = ((QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan"))).getCREATE_TIME().split("T")[0];
        String title = ((QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan"))).getTITLE();
        String content = ((QuestionNaireBean) (getIntent().getSerializableExtra("wenjuan"))).getQUESTION_TEST();
        View titleView = LayoutInflater.from(this).inflate(R.layout.question_title, null);

        TextView numberTv = (TextView) titleView.findViewById(R.id.title_tv_number);//试卷编号
        TextView nameTv = (TextView) titleView.findViewById(R.id.title_tv_name);//姓名
        TextView dateTv = (TextView) titleView.findViewById(R.id.title_tv_date);//访问日期
        TextView titleTv = (TextView) titleView.findViewById(R.id.title_tv_title);//问卷标题
        TextView contentTv = (TextView) titleView.findViewById(R.id.title_tv_content);//问卷介绍

        nameTv.setText(((PersonalInfoBean) getIntent().getSerializableExtra("personalInfo")).getApplicantName());
        numberTv.setText(number);
        dateTv.setText(date);
        titleTv.setText(title);
        contentTv.setText(Html.fromHtml(content));

        bigll.addView(titleView);
    }

    private void initView() {

        startBtn = (Button) findViewById(R.id.btn_start);//开始答题
        if(isComplete==1){
            startBtn.setVisibility(View.GONE);
        }else{
            startBtn.setVisibility(View.VISIBLE);
        }
        lastBtn = (Button) findViewById(R.id.btn_last);//上一题
        nextBtn = (Button) findViewById(R.id.btn_next);//下一题
        submitBtn = (Button) findViewById(R.id.btn_submit);//提交
        allBtn = (Button) findViewById(R.id.btn_all);//查看全部
        restartBtn = (Button) findViewById(R.id.btn_restart);//重新答题

        startBtn.setOnClickListener(this);
        lastBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        restartBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_start:
                //Toast.makeText(this,"开始答题",Toast.LENGTH_SHORT).show();
                startQuestion();
                startBtn.setVisibility(View.GONE);
                lastBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_last:
                //Toast.makeText(this,"上一题",Toast.LENGTH_SHORT).show();
                lastQuestion();
                break;
            case R.id.btn_next:
                //Toast.makeText(this,"下一题",Toast.LENGTH_SHORT).show();
                nextQuestion();
                break;
            case R.id.btn_submit:
                //提交答案
                createUserInfo();
                break;
            case R.id.btn_all:
                //Toast.makeText(this, "查看全部", Toast.LENGTH_SHORT).show();
                seeAllQuestion();
                break;
            case R.id.btn_restart:
                // Toast.makeText(this, "重新答题", Toast.LENGTH_SHORT).show();
                restartQuestion();
                break;
        }

    }

    //开始答题
    private void startQuestion() {

        radioButtons.clear();
        spinners.clear();
        index = 0;

        if (questionDetailsList.size() > 0) {
            juanInfos=questionDetailsList;
        }
        questionTitleList = getQuestionByParent();
        if (questionTitleList.size() > 0) {
            fretchTree(bigll, questionTitleList.get(index), "");
        }
    }

    //重新答題
    private void restartQuestion() {

        lastBtn.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);

        submitBtn.setVisibility(View.GONE);
        restartBtn.setVisibility(View.GONE);
        radioButtons.clear();
        spinners.clear();
        index = 0;

        questionTitleList = getQuestionByParent();
        if (questionTitleList.size() > 0) {
            fretchTree(bigll, questionTitleList.get(index), "");
        }
    }

    //上一题
    private void lastQuestion() {

        if (index == 0) {
            Toast.makeText(this, "已经是第一题了", Toast.LENGTH_SHORT).show();
            return;
        }

        List<QuestionNaireBean.QuestionDetailsList> list = getAnswerByParentId(currentInfo);

        //去掉单选按钮
        for (QuestionNaireBean.QuestionDetailsList dl : list) {

            for (RadioButton rb : radioButtons) {

                if (rb.getId() == dl.getID()) {
                    rb.setChecked(false);
                }
            }
        }

        //去掉小题的文本
        for (QuestionNaireBean.QuestionDetailsList dl : list) {

            for (Spinner spinner : spinners) {

                if (spinner.getId() == dl.getID()) {

                    //spinner.setText("请选择年份");
                    spinner.setSelection(0);
                }}}

        if (tempList.size() > 0) {
            List<Integer> temp = new ArrayList<>();
            temp.addAll(tempList.subList(tempList.size() - 1, tempList.size()));
            tempQuestionIndex = temp.get(0);
            tempList.removeAll(temp);
            temp.clear();
        }
        index = tempQuestionIndex;
        currentInfo = questionTitleList.get(index);

        fretchTree(bigll, currentInfo, "");

        allBtn.setVisibility(View.GONE);

    }

    //下一题
    private void nextQuestion() {

        questionTitleList = getQuestionByParent();

        if (index >= questionTitleList.size() - 1) {
            Toast.makeText(this, "已经是最后一题了", Toast.LENGTH_SHORT).show();
            allBtn.setVisibility(View.VISIBLE);
            return;
        }

        QuestionNaireBean.QuestionDetailsList info = questionTitleList.get(index);
        //判断是否已经作答了
        int questionNo = info.getID();

        if (questionTitleList.size() > 0 &&
                !checkIsRadioed(answerInfo, questionNo)) {

            Toast.makeText(this, "请选择合适的答案", Toast.LENGTH_SHORT).show();


            return;

        }

        if (checkIsSelectYear(answerInfo)) {
            Toast.makeText(this, "请选择合适的答案", Toast.LENGTH_SHORT).show();
            return;
        }

        List<QuestionNaireBean.QuestionDetailsList> tempAnswer = new ArrayList<>();

        for (QuestionNaireBean.QuestionDetailsList answer : answerInfo) {
            if (answer.getPARENTID() == questionNo) {
                tempAnswer.add(answer);
            }
        }

        tempQuestionIndex = index;

        String jumpCode = null;

        //判断如何跳题
        for (RadioButton rb : radioButtons) {

            if (rb.isChecked()) {

                for (QuestionNaireBean.QuestionDetailsList temp : tempAnswer) {

                    if (rb.getId() == temp.getID()) {
                        jumpCode = temp.getJUMP_CODE();
                    }
                }
            }
        }

        int tempIndex = 0;

        if (jumpCode != null) {

            if (TextUtils.equals("结束", jumpCode)) {

                Toast.makeText(this, "已经是最后一题了", Toast.LENGTH_SHORT).show();
                allBtn.setVisibility(View.VISIBLE);
                tempList.add(tempQuestionIndex - 1);

                if (tempList.size() > tempQuestionIndex - 1 && (tempQuestionIndex - 1) > -1) {

                    tempList.remove(tempQuestionIndex - 1);

                } else {
                    tempList.remove(tempList.size() - 1);
                }
                return;
            } else {
                allBtn.setVisibility(View.GONE);
            }

            for (QuestionNaireBean.QuestionDetailsList dl : questionTitleList) {

                if (jumpCode.equals(dl.getCODE())) {
                    index = questionTitleList.indexOf(dl);
                    tempIndex = 0;
                    break;
                } else {
                    tempIndex++;
                }

            }

        } else {
            tempIndex++;
        }

        tempList.add(tempQuestionIndex);

        if (tempIndex > 0) {
            index++;
        }
        tempAnswer.clear();
        currentInfo = questionTitleList.get(index);

        if (questionTitleList.size() > 0) {
            fretchTree(bigll, currentInfo, "");
        }

    }

    private List<QuestionNaireBean.QuestionDetailsList> getQuestionByParent() {
        List<QuestionNaireBean.QuestionDetailsList> questionInfos = new ArrayList<>();
        for (QuestionNaireBean.QuestionDetailsList detailsList : juanInfos) {
            if (detailsList.getPARENTID() == 0) {
                questionInfos.add(detailsList);
            }
        }
        return questionInfos;
    }

    //用问题的信息得到选项的信息
    private List<QuestionNaireBean.QuestionDetailsList> getAnswerByParentId(QuestionNaireBean.QuestionDetailsList info) {

        List<QuestionNaireBean.QuestionDetailsList> aInfo = new ArrayList<>();

        for (QuestionNaireBean.QuestionDetailsList detailsList : juanInfos) {
            if (detailsList.getPARENTID() == info.getID()) {
                aInfo.add(detailsList);
            }
        }

        return aInfo;
    }

    //问题的布局
    private void fretchTree(LinearLayout layout, QuestionNaireBean.QuestionDetailsList info, String isAll) {

        if ("".equals(isAll)) {
            bigll.removeAllViews();
        }

        LinearLayout alllinearLayout = new LinearLayout(this);
        alllinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout qll = new LinearLayout(this);
        qll.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(this);
        tv.setText(info.getTITLE_LEFT());
        tv.setTextSize(30);
        qll.addView(tv);
        alllinearLayout.addView(qll);

        LinearLayout alinearLayout = new LinearLayout(this);
        alinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        answerInfo = getAnswerByParentId(info);
        //单选选项
        RadioGroup rg = new RadioGroup(this);

        LinearLayout xuanxiangll = new LinearLayout(this);
        xuanxiangll.setOrientation(LinearLayout.VERTICAL);

        for (QuestionNaireBean.QuestionDetailsList dList : answerInfo) {

            fretchTreeByQuestion(rg, xuanxiangll, dList, isAll);

        }

        alinearLayout.addView(rg);
        alinearLayout.addView(xuanxiangll);
        alllinearLayout.addView(alinearLayout);
        layout.addView(alllinearLayout);

    }

    //选项的布局（单选）
    private void fretchTreeByQuestion(RadioGroup rg, LinearLayout xuanxiangll, final QuestionNaireBean.QuestionDetailsList info, String isAll) {

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        linearLayout.setLayoutParams(llParam);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        RadioButton rb = new RadioButton(this);
        LinearLayout.LayoutParams rbParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        rb.setLayoutParams(rbParam);
        rb.setText(info.getTITLE_LEFT());
        rb.setTextSize(17);
        rb.setId(info.getID());
        rb.setButtonDrawable(getResources().getDrawable(R.drawable.details_radiobutton_selector));
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals("结束", info.getJUMP_CODE())) {
                    allBtn.setVisibility(View.GONE);
                } else if (TextUtils.equals("结束", info.getJUMP_CODE())) {
                    allBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        rg.addView(rb);
        if (radioButtons.size() > 0) {

            List<RadioButton> tempRadioButtons = new ArrayList<>();
            for (RadioButton rButton : radioButtons) {

                if (rButton.getId() == info.getID() && rButton.isChecked()) {
                    rb.setChecked(true);
                    tempRadioButtons.add(rButton);
                }

            }
            radioButtons.removeAll(tempRadioButtons);
            tempRadioButtons.clear();
        }

        if ("isAll".equals(isAll)) {
            rb.setEnabled(false);
        } else {
            rb.setEnabled(true);
        }

        radioButtons.add(rb);

        if(!resultsList.isEmpty()){

            for(AnswerInfo aInfo:resultsList){

                for(RadioButton radioButton:radioButtons){

                    if(radioButton.getId()==aInfo.getQUESTION_DETAILID()){
                        radioButton.setChecked(true);
                    }

                }

            }

        }

        if (TextUtils.equals(info.getINPUT_TYPE(), "selectYear")) {
            final Spinner spinner = new Spinner(this);
            spinner.setBackgroundColor(Color.parseColor("#00000000"));
            if ("isAll".equals(isAll)) {
                spinner.setEnabled(false);
            } else {
                spinner.setEnabled(true);
            }
            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, 70);
            spinner.setGravity(Gravity.CENTER);
            spinner.setId(info.getID());
            spinner.setPadding(10, 0, 0, 0);
            if (years.isEmpty()) {
                getYears();
            }
            ArrayAdapter<String> yAdapter = new ArrayAdapter<String>(this, R.layout.selectyear_spinner_item, R.id.spinner_tv, years);
            spinner.setAdapter(yAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    spinnerYear = spinner.getSelectedItem().toString().trim();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            if (spinners.size() > 0) {
                List<Spinner> tempSpinners = new ArrayList<>();

                for (RadioButton rButton : radioButtons) {

                    for (Spinner spinner1 : spinners) {
                        if (spinner1.getId() == info.getID() && rButton.isChecked()) {
                            //  spinner.setText(tView.getText());
                            spinner.getSelectedItem().toString();
                            for (int i = 0; i < years.size(); i++) {
                                if (TextUtils.equals(spinnerYear, years.get(i))) {
                                    spinner.setSelection(i);
                                }
                            }

                        } else {
                            // spinner.setText("请选择年份");
                            spinner.setSelection(0);
                        }
                        tempSpinners.add(spinner1);
                    }
                }

                spinners.removeAll(tempSpinners);
                tempSpinners.clear();
            }

            spinners.add(spinner);
            if(!resultsList.isEmpty()){

                for(AnswerInfo aInfo:resultsList){

                    for(Spinner mySpinner:spinners){
                        if(aInfo.getQUESTION_DETAILID()==mySpinner.getId()){

                            for (int i = 0; i < years.size(); i++) {
                                if (TextUtils.equals(aInfo.getINPUT_VALUE(), years.get(i))) {
                                    spinner.setSelection(i);
                                }
                            }

                        }
                    }
                }
            }
            linearLayout.addView(spinner, textparams);
        } else {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, 70);
            tv.setGravity(Gravity.CENTER);
            linearLayout.addView(tv, textparams);
        }

        TextView tvRight = new TextView(this);
        tvRight.setText(info.getTITLE_RIGHT());
        tvRight.setTextSize(17);
        LinearLayout.LayoutParams tvRightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        tvRight.setGravity(Gravity.CENTER);
        if (!"isAll".equals(isAll)) {
            tvRight.setTextColor(Color.parseColor("#000000"));
        }
        linearLayout.addView(tvRight, tvRightParams);

        xuanxiangll.addView(linearLayout, llParam);
    }

    //查看已查的答案
    private void getResultsInfo(){

        ProgressUtils.showMyProgressDialog(this);

        OkHttpUtils.post().url(resultsUrl).addParams("RespondentId",UserNo).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                ProgressUtils.dismissMyProgressDialog(QuestionNaireDetailsActivity.this);
                Toast.makeText(QuestionNaireDetailsActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                Gson gson=new Gson();

                resultsList=gson.fromJson(response,new TypeToken<List<AnswerInfo>>(){}.getType());

                seeCompleteAnswer();
                ProgressUtils.dismissMyProgressDialog(QuestionNaireDetailsActivity.this);
            }
        });

    }

    private void seeCompleteAnswer(){

        lastBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        allBtn.setVisibility(View.GONE);
        restartBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        startBtn.setVisibility(View.GONE);

        if (questionDetailsList.size() > 0) {
            juanInfos=questionDetailsList;
        }

        questionTitleList = getQuestionByParent();

        for (QuestionNaireBean.QuestionDetailsList dList : questionTitleList) {

            fretchTree(bigll, dList, "isAll");
        }

    }

    //查看全部
    private void seeAllQuestion() {
        bigll.removeAllViews();
        addTitle();
        lastBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        allBtn.setVisibility(View.GONE);

        restartBtn.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);

        for (QuestionNaireBean.QuestionDetailsList dList : questionTitleList) {

            fretchTree(bigll, dList, "isAll");
        }

    }


    //提交答案信息
    private void createUserInfo() {

       // Log.e("2017/9/26","UserNo=="+UserNo);
//  2017/9/26注释
     //   if (UserNo == null) {

            showMessage("温馨提示", "确定要提交答题吗?");

     //   }
    }

  //  2017/9/26注释
//    private void createUser() {
//
//        OkHttpUtils.post().url(createUserUrl).addParams("IdCard", IdCard).addHeader("Cookie", cookie).build().execute(new StringCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//                Toast.makeText(QuestionNaireDetailsActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//                userID = response;
//
//                toStreamAnswerInfo();
//            }
//        });
//
//    }

    private void toStreamAnswerInfo() {

        final String answerString;

        getAnswerInfo();

        answerString = parseAnswerInfo();

        if (answerInfo2.size() > 0) {
            answerInfo2.clear();
        }

        //Log.e("2017/7/25", "您的答案是:" + answerString);
        try {
            final byte[] answer;

            Log.e("2017/9/27","======"+answerString);

            answer = answerString.getBytes("utf-8");

            new Thread() {
                @Override
                public void run() {
                    uploadAnswerInfo(answer);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadAnswerInfo(byte[] answer) {

        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(uploadUrl + "?RespondentId=" + UserNo);

        try {
            post.setHeader("Cookie", cookie);
            String str = Base64.encodeToString(answer, Base64.DEFAULT);
            StringEntity stringEntity = new StringEntity(str);
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            stringEntity.setContentEncoding(new BasicHeader(
                    HTTP.CONTENT_ENCODING, HTTP.UTF_8));
            post.setEntity(stringEntity);
            Log.e("2017/7/28", "Cookie="+cookie);
            Log.e("2017/9/27","userId"+userID);
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {

                Log.e("2017/7/28", EntityUtils.toString(response.getEntity()));//true:成功  false:失败

                //Log.e("2017/9/27","结果=="+response.getEntity().getContent());

                Message msg=Message.obtain();
                msg.what=UPLOAD_SUCCESS;
                myHandler.sendMessage(msg);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            post.abort();
        }


    }

    private void getAnswerInfo() {

        if (radioButtons.size() > 0) {

            for (RadioButton rb : radioButtons) {

                if (rb.isChecked()) {

                    AnswerInfo answerInfo = new AnswerInfo();

                    for (QuestionNaireBean.QuestionDetailsList dList : juanInfos) {

                        if (rb.getId() == dList.getID()) {

                            answerInfo.setQUESTION_DETAILID(dList.getID());
                            answerInfo.setINPUT_VALUE("");
                        }

                    }

                    if (spinners.size() > 0) {

                        for (Spinner spinner : spinners) {

                            if (rb.getId() == spinner.getId()) {

                                // answerInfo.setAnswerText(tv.getText().toString().trim());
                                answerInfo.setINPUT_VALUE(spinner.getSelectedItem().toString().trim());
                            }

                        }

                    }

                    answerInfo2.add(answerInfo);
                }
            }
        }
    }


    private String parseAnswerInfo() {

        JSONArray array = new JSONArray();

        if (answerInfo2.size() > 0) {

            for (AnswerInfo answerInfo : answerInfo2) {

                JSONObject object = new JSONObject();

                try {
                    object.put("QUESTION_DETAILID", answerInfo.getQUESTION_DETAILID());
                    object.put("INPUT_VALUE", answerInfo.getINPUT_VALUE());
                    //object.put("HOMEID",userID);
                    object.put("MASTER_ID", questionDetailsList.get(0).getMASTERID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(object);
            }

        }

        return array.toString();
    }

    //判断单选题是否作答
    private boolean checkIsRadioed(List<QuestionNaireBean.QuestionDetailsList> infos, int questionId) {

        for (RadioButton rb : radioButtons) {

            for (QuestionNaireBean.QuestionDetailsList dl : infos) {

                if (rb.getId() == dl.getID() && dl.getPARENTID() == questionId && rb.isChecked()) {

                    return true;

                }

            }

        }

        return false;
    }


    //判断年份是否选择
    private boolean checkIsSelectYear(List<QuestionNaireBean.QuestionDetailsList> infos) {

        if (spinners.size() > 0) {

            for (RadioButton rb : radioButtons) {

                if (rb.isChecked()) {

                    for (Spinner spinner : spinners) {

                        for (QuestionNaireBean.QuestionDetailsList dList : infos) {

                            if (rb.getId() == dList.getID() && spinner.getId() == dList.getID()
                                    && TextUtils.equals("请选择年份", spinner.getSelectedItem().toString().trim())) {

                                return true;

                            }}}}}}

        return false;
    }

    @Override
    public void onBackPressed() {


        if(isComplete==0) {
            showMessage("温馨提示", "确定要放弃答题吗?");
        }else{
            finish();
        }
    }

    private void showMessage(String title, final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.equals("确定要提交答题吗?",message)){
                            //创建被调查人编号 2017/9/26注释
                            //createUser();
                            toStreamAnswerInfo();
                        }else{

                            finish();
                        }

                        dialog.dismiss();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

    }

    private void getYears() {

        years.add("请选择年份");

        for (int i = 2000; i < 2018; i++) {

            years.add(i + "");

        }
    }


}
