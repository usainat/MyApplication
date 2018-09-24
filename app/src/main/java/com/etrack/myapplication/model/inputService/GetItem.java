package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 15-03-2018.
 */

public class GetItem {
    @SerializedName("objitem")
    @Expose
    private Objitem objitem;

    public Objitem getObjitem() {
        return objitem;
    }

    public void setObjitem(Objitem objitem) {
        this.objitem = objitem;
    }

    public class Objitem {

        @SerializedName("item_barcode")
        @Expose
        private String itemCode;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

    }
}
