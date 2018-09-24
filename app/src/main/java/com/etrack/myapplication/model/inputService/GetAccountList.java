package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAccountList {
    @SerializedName("objacnt")
    @Expose
    private Objacnt objacnt;

    public Objacnt getObjacnt() {
        return objacnt;
    }

    public void setObjacnt(Objacnt objacnt) {
        this.objacnt = objacnt;
    }


    public class Objacnt {

        @SerializedName("account_group")
        @Expose
        private String accountGroup;
        @SerializedName("account_code")
        @Expose
        private String accountCode;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        public String getAccountGroup() {
            return accountGroup;
        }

        public void setAccountGroup(String accountGroup) {
            this.accountGroup = accountGroup;
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

    }

}
