package com.youli.zbetuch.bean;


import java.io.Serializable;

/**
 *"ApplicantIDNo": "310230196409200470",
 "ApplicantName": "顾伟达",
 "CellPhoneNumber": "13815072557",
 "RecordCount": 15,
 "RegisteredAddress": "三星镇海洪港村仓房310号",
 "RowNumber": 8,
 "Sex": "男",
 "Status": "未缴费",
 "TownName": "三星镇",
 "VillageName": "海洪港村
 */
public class PersonalInfoBean implements Serializable{
    private String ApplicantIDNo;
    private String ApplicantName;
    private String CellPhoneNumber;
    private String Sex;
    private String Status;
    private String RegisteredAddress;
    private String TownName;
    private String VillageName;
    private String RecordCount;
    private String Id;
    private String Is_Complete;

    public String getApplicantIDNo() {
        return ApplicantIDNo;
    }

    public void setApplicantIDNo(String applicantIDNo) {
        ApplicantIDNo = applicantIDNo;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }

    public String getCellPhoneNumber() {
        return CellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        CellPhoneNumber = cellPhoneNumber;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRegisteredAddress() {
        return RegisteredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        RegisteredAddress = registeredAddress;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(String recordCount) {
        RecordCount = recordCount;
    }

    public String getIs_Complete() {
        return Is_Complete;
    }

    public void setIs_Complete(String is_Complete) {
        Is_Complete = is_Complete;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
