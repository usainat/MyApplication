package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 23-02-2018.
 */

public class GetCatogorey {

    @SerializedName("objcat")
    @Expose
    private CatogoreyObjcat objcat;

    public CatogoreyObjcat getObjcat() {
        return objcat;
    }

    public void setObjcat(CatogoreyObjcat objcat) {
        this.objcat = objcat;
    }

    public class CatogoreyObjcat {

        @SerializedName("category_code")
        @Expose
        private String categoryCode;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

    }

}
