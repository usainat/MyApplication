package com.etrack.myapplication.model.outputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hussain on 19-02-2018.
 */

public class GetCatogoreySuccessFailure {


    @SerializedName("Exception")
    @Expose
    private Object exception;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DataObject")
    @Expose
    private List<GetCatogoreyDataObject> dataObject = null;

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

    public List<GetCatogoreyDataObject> getDataObject() {
        return dataObject;
    }

    public void setDataObject(List<GetCatogoreyDataObject> dataObject) {
        this.dataObject = dataObject;
    }

   public class GetCatogoreyDataObject {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("category_code")
        @Expose
        private String categoryCode;
        @SerializedName("category_name_en")
        @Expose
        private String categoryNameEn;
        @SerializedName("category_name_ar")
        @Expose
        private String categoryNameAr;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("branch_name")
        @Expose
        private String branchName;
        @SerializedName("discount")
        @Expose
        private String discount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getCategoryNameEn() {
            return categoryNameEn;
        }

        public void setCategoryNameEn(String categoryNameEn) {
            this.categoryNameEn = categoryNameEn;
        }

        public String getCategoryNameAr() {
            return categoryNameAr;
        }

        public void setCategoryNameAr(String categoryNameAr) {
            this.categoryNameAr = categoryNameAr;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

    }
}

