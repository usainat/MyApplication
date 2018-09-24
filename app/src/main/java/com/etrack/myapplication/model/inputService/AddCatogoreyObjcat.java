package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 23-02-2018.
 */

public class AddCatogoreyObjcat  {

    @SerializedName("category_code")
    @Expose
    private String categoryCode;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_name_ar")
    @Expose
    private String categoryNameAr;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("discount")
    @Expose
    private String discount;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameAr() {
        return categoryNameAr;
    }

    public void setCategoryNameAr(String categoryNameAr) {
        this.categoryNameAr = categoryNameAr;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

}