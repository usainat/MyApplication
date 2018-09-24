package com.etrack.myapplication.model.outputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllAccount {


    @SerializedName("Exception")
    @Expose
    private Object exception;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DataObject")
    @Expose
    private List<DataObject> dataObject = null;

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataObject> getDataObject() {
        return dataObject;
    }

    public void setDataObject(List<DataObject> dataObject) {
        this.dataObject = dataObject;
    }


    public class DataObject {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("account_group")
        @Expose
        private String accountGroup;
        @SerializedName("account_name_en")
        @Expose
        private String accountNameEn;
        @SerializedName("account_code")
        @Expose
        private String accountCode;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("account_name_ar")
        @Expose
        private String accountNameAr;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAccountGroup() {
            return accountGroup;
        }

        public void setAccountGroup(String accountGroup) {
            this.accountGroup = accountGroup;
        }

        public String getAccountNameEn() {
            return accountNameEn;
        }

        public void setAccountNameEn(String accountNameEn) {
            this.accountNameEn = accountNameEn;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getAccountNameAr() {
            return accountNameAr;
        }

        public void setAccountNameAr(String accountNameAr) {
            this.accountNameAr = accountNameAr;
        }


    }
}
