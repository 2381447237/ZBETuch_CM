package com.youli.zbetuch.bean;

/**
 * Created by liutao on 2017/7/21.
 */
//[{"ID":176,"QUESTION_DETAILID":6,"INPUT_VALUE":"","MASTER_ID":4,"RESPONDENTS":15,"RecordCount":0}]
public class AnswerInfo {

    private int ID;
    private int QUESTION_DETAILID;//答案ID
    private int MASTER_ID;
    private String INPUT_VALUE;//答案文本
    private int RESPONDENTS;

    public String getINPUT_VALUE() {
        return INPUT_VALUE;
    }

    public void setINPUT_VALUE(String INPUT_VALUE) {
        this.INPUT_VALUE = INPUT_VALUE;
    }

    public int getQUESTION_DETAILID() {
        return QUESTION_DETAILID;
    }

    public void setQUESTION_DETAILID(int QUESTION_DETAILID) {
        this.QUESTION_DETAILID = QUESTION_DETAILID;
    }

    public AnswerInfo() {
    }

    public AnswerInfo(String INPUT_VALUE, int QUESTION_DETAILID) {
        this.INPUT_VALUE = INPUT_VALUE;
        this.QUESTION_DETAILID = QUESTION_DETAILID;
    }

    @Override
    public String toString() {
        return "AnswerInfo{" +
                "ID=" + ID +
                ", QUESTION_DETAILID=" + QUESTION_DETAILID +
                ", MASTER_ID=" + MASTER_ID +
                ", INPUT_VALUE='" + INPUT_VALUE + '\'' +
                ", RESPONDENTS=" + RESPONDENTS +
                '}';
    }
    //    public int getAnswerId() {
//        return answerId;
//    }
//
//    public void setAnswerId(int answerId) {
//        this.answerId = answerId;
//    }
//
//    public String getAnswerText() {
//        return answerText;
//    }
//
//    public void setAnswerText(String answerText) {
//        this.answerText = answerText;
//    }
//
//    public AnswerInfo() {
//    }
//
//    public AnswerInfo(int answerId, String answerText) {
//        this.answerId = answerId;
//        this.answerText = answerText;
//    }
//
//    @Override
//    public String toString() {
//        return "AnswerInfo{" +
//                "answerId=" + answerId +
//                ", answerText='" + answerText + '\'' +
//                '}';
//    }
}
