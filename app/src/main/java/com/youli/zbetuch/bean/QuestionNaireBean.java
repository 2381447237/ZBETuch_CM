package com.youli.zbetuch.bean;


import java.io.Serializable;
import java.util.List;

//http://server:91/JSON/Json_Get_Question_Master.aspx?MasterId=4

//[{"ID":4,"TITLE":"征地专项摸底调查问卷","QUESTION_TEST":"编号","QUESTION_NUM":"001","USER_ID":null,
// "CREATE_TIME":"2017-07-19T11:02:18.423","RecordCount":0,"QuestionDetails":
public class QuestionNaireBean implements Serializable{
    private int ID;
    private String TITLE;//问卷标题
    private String QUESTION_TEST;
    private String QUESTION_NUM;//问卷编号
    private String USER_ID;
    private String CREATE_TIME;
    private int RecordCount;
    private int SurveyNum;

    private List<QuestionDetailsList> QuestionDetails;//问卷具体的问题和选项
//"QuestionDetails":[{"ID":4,"TITLE_LEFT":"Q1可否调查？(单选)","TITLE_RIGHT":"",
// "CODE":"Q1","QUESTION_NUM":1.00,"IS_INPUT":false,"INPUT_TYPE":"","JUMP_CODE":"",
// "PARENTID":0,"MASTERID":4,"REMOVE_CODE":"","RecordCount":0}


    public QuestionNaireBean(String CREATE_TIME, int ID, String QUESTION_NUM, String TITLE) {
        this.CREATE_TIME = CREATE_TIME;
        this.ID = ID;
        this.QUESTION_NUM = QUESTION_NUM;
        this.TITLE = TITLE;
    }

    public static class  QuestionDetailsList implements  Serializable{

      private int ID;//问题或选项的ID
      private String TITLE_LEFT;
      private String TITLE_RIGHT;
      private String CODE;//问题的题码
      private float QUESTION_NUM;
      private boolean IS_INPUT;//是否是要输入文本
      private String INPUT_TYPE;//输入文本的类型
      private String JUMP_CODE;//直接跳转到哪一题
     private int PARENTID;//为0就是问题，不为0就是选项
     private int MASTERID;//属于哪一套问卷
     private String REMOVE_CODE;
     private int RecordCount;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getTITLE_RIGHT() {
        return TITLE_RIGHT;
    }

    public void setTITLE_RIGHT(String TITLE_RIGHT) {
        this.TITLE_RIGHT = TITLE_RIGHT;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getINPUT_TYPE() {
        return INPUT_TYPE;
    }

    public void setINPUT_TYPE(String INPUT_TYPE) {
        this.INPUT_TYPE = INPUT_TYPE;
    }

    public boolean IS_INPUT() {
        return IS_INPUT;
    }

    public void setIS_INPUT(boolean IS_INPUT) {
        this.IS_INPUT = IS_INPUT;
    }

    public String getJUMP_CODE() {
        return JUMP_CODE;
    }

    public void setJUMP_CODE(String JUMP_CODE) {
        this.JUMP_CODE = JUMP_CODE;
    }

    public int getMASTERID() {
        return MASTERID;
    }

    public void setMASTERID(int MASTERID) {
        this.MASTERID = MASTERID;
    }

    public int getPARENTID() {
        return PARENTID;
    }

    public void setPARENTID(int PARENTID) {
        this.PARENTID = PARENTID;
    }

    public float getQUESTION_NUM() {
        return QUESTION_NUM;
    }

    public void setQUESTION_NUM(float QUESTION_NUM) {
        this.QUESTION_NUM = QUESTION_NUM;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public String getREMOVE_CODE() {
        return REMOVE_CODE;
    }

    public void setREMOVE_CODE(String REMOVE_CODE) {
        this.REMOVE_CODE = REMOVE_CODE;
    }

    public String getTITLE_LEFT() {
        return TITLE_LEFT;
    }

    public void setTITLE_LEFT(String TITLE_LEFT) {
        this.TITLE_LEFT = TITLE_LEFT;
    }
}

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQUESTION_NUM() {
        return QUESTION_NUM;
    }

    public void setQUESTION_NUM(String QUESTION_NUM) {
        this.QUESTION_NUM = QUESTION_NUM;
    }

    public String getQUESTION_TEST() {
        return QUESTION_TEST;
    }

    public void setQUESTION_TEST(String QUESTION_TEST) {
        this.QUESTION_TEST = QUESTION_TEST;
    }

    public List<QuestionDetailsList> getQuestionDetails() {
        return QuestionDetails;
    }

    public void setQuestionDetails(List<QuestionDetailsList> questionDetails) {
        QuestionDetails = questionDetails;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public int getSurveyNum() {
        return SurveyNum;
    }

    public void setSurveyNum(int surveyNum) {
        SurveyNum = surveyNum;
    }
}
