package com.etrack.myapplication.model.inputService;import com.google.gson.annotations.Expose;import com.google.gson.annotations.SerializedName;public class ReportByItem {    @SerializedName("objreport")    @Expose    private Objreport objreport;    public Objreport getObjreport() {        return objreport;    }    public void setObjreport(Objreport objreport) {        this.objreport = objreport;    }    public class Objreport {        @SerializedName("company_id")        @Expose        private String companyId;        @SerializedName("actor_code")        @Expose        private String actorCode;        @SerializedName("item_code")        @Expose        private String itemCode;        @SerializedName("bill_type")        @Expose        private String billType;        @SerializedName("branch_id")        @Expose        private String branchId;        @SerializedName("from_date")        @Expose        private String fromDate;        @SerializedName("to_date")        @Expose        private String toDate;        public String getCompanyId() {            return companyId;        }        public void setCompanyId(String companyId) {            this.companyId = companyId;        }        public String getActorCode() {            return actorCode;        }        public void setActorCode(String actorCode) {            this.actorCode = actorCode;        }        public String getItemCode() {            return itemCode;        }        public void setItemCode(String itemCode) {            this.itemCode = itemCode;        }        public String getBillType() {            return billType;        }        public void setBillType(String billType) {            this.billType = billType;        }        public String getBranchId() {            return branchId;        }        public void setBranchId(String branchId) {            this.branchId = branchId;        }        public String getFromDate() {            return fromDate;        }        public void setFromDate(String fromDate) {            this.fromDate = fromDate;        }        public String getToDate() {            return toDate;        }        public void setToDate(String toDate) {            this.toDate = toDate;        }    }}