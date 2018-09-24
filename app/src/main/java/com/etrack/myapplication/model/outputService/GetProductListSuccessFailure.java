package com.etrack.myapplication.model.outputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hussain on 14-03-2018.
 */

public class GetProductListSuccessFailure {

    @SerializedName("Exception")
    @Expose
    private Object exception;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DataObject")
    @Expose
    private List<ProductDataObject> dataObject = null;

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

    public List<ProductDataObject> getDataObject() {
        return dataObject;
    }

    public void setDataObject(List<ProductDataObject> dataObject) {
        this.dataObject = dataObject;
    }
    public class ProductDataObject {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("item_code")
        @Expose
        private String itemCode;
        @SerializedName("item_name_en")
        @Expose
        private String itemNameEn;
        @SerializedName("item_name_ar")
        @Expose
        private String itemNameAr;
        @SerializedName("category_code")
        @Expose
        private String categoryCode;
        @SerializedName("opening_quantity")
        @Expose
        private String openingQuantity;
        @SerializedName("cost_price")
        @Expose
        private String costPrice;
        @SerializedName("sales_price")
        @Expose
        private String salesPrice;
        @SerializedName("unit_price")
        @Expose
        private String unitPrice;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemNameEn() {
            return itemNameEn;
        }

        public void setItemNameEn(String itemNameEn) {
            this.itemNameEn = itemNameEn;
        }

        public String getItemNameAr() {
            return itemNameAr;
        }

        public void setItemNameAr(String itemNameAr) {
            this.itemNameAr = itemNameAr;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getOpeningQuantity() {
            return openingQuantity;
        }

        public void setOpeningQuantity(String openingQuantity) {
            this.openingQuantity = openingQuantity;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
        }

        public String getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(String salesPrice) {
            this.salesPrice = salesPrice;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

    }
}