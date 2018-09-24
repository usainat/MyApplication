package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 05-03-2018.
 */

public class AddBranch {
    @SerializedName("objbranch")
    @Expose
    private AddBranchObject objbranch;

    public AddBranchObject getObjbranch() {
        return objbranch;
    }

    public void setObjbranch(AddBranchObject objbranch) {
        this.objbranch = objbranch;
    }


    public class AddBranchObject {

        @SerializedName("branch_id")
        @Expose
        private String branchId;
        @SerializedName("branch_name_en")
        @Expose
        private String branchNameEn;
        @SerializedName("branch_name_ar")
        @Expose
        private String branchNameAr;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        @SerializedName("branch_address")
        @Expose
        private String branchAddress;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getBranchNameEn() {
            return branchNameEn;
        }

        public void setBranchNameEn(String branchNameEn) {
            this.branchNameEn = branchNameEn;
        }

        public String getBranchNameAr() {
            return branchNameAr;
        }

        public void setBranchNameAr(String branchNameAr) {
            this.branchNameAr = branchNameAr;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getBranchAddress() {
            return branchAddress;
        }

        public void setBranchAddress(String branchAddress) {
            this.branchAddress = branchAddress;
        }
    }
}
