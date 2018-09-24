package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAccount {

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

        @SerializedName("account_code")
        @Expose
        private String accountCode;

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

    }
}
