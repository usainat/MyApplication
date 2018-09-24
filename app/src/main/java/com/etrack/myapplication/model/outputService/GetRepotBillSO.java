package com.etrack.myapplication.model.outputService;import com.google.gson.annotations.Expose;import com.google.gson.annotations.SerializedName;import java.util.List;public class GetRepotBillSO {    @SerializedName("Exception")    @Expose    private Object exception;    @SerializedName("Message")    @Expose    private String message;    @SerializedName("DataObject")    @Expose    private List<DataObject> dataObject = null;    @SerializedName("Total_Amount")    @Expose    private String totalAmount;    public Object getException() {        return exception;    }    public void setException(Object exception) {        this.exception = exception;    }    public String getMessage() {        return message;    }    public void setMessage(String message) {        this.message = message;    }    public List<DataObject> getDataObject() {        return dataObject;    }    public void setDataObject(List<DataObject> dataObject) {        this.dataObject = dataObject;    }    public String getTotalAmount() {        return totalAmount;    }    public void setTotalAmount(String totalAmount) {        this.totalAmount = totalAmount;    }    public class DataObject {        @SerializedName("account_name_en")        @Expose        private String accountNameEn;        @SerializedName("account_name_ar")        @Expose        private String accountNameAr;        @SerializedName("id")        @Expose        private Integer id;        @SerializedName("sales_code")        @Expose        private String salesCode;        @SerializedName("sales_date")        @Expose        private String salesDate;        @SerializedName("remarks")        @Expose        private String remarks;        @SerializedName("company_id")        @Expose        private String companyId;        @SerializedName("total_amount")        @Expose        private String totalAmount;        @SerializedName("branch_id")        @Expose        private String branchId;        @SerializedName("sale_by")        @Expose        private String saleBy;        @SerializedName("sales_to")        @Expose        private String salesTo;        @SerializedName("branch_name_en")        @Expose        private String branchNameEn;        public String getAccountNameEn() {            return accountNameEn;        }        public void setAccountNameEn(String accountNameEn) {            this.accountNameEn = accountNameEn;        }        public String getAccountNameAr() {            return accountNameAr;        }        public void setAccountNameAr(String accountNameAr) {            this.accountNameAr = accountNameAr;        }        public Integer getId() {            return id;        }        public void setId(Integer id) {            this.id = id;        }        public String getSalesCode() {            return salesCode;        }        public void setSalesCode(String salesCode) {            this.salesCode = salesCode;        }        public String getSalesDate() {            return salesDate;        }        public void setSalesDate(String salesDate) {            this.salesDate = salesDate;        }        public String getRemarks() {            return remarks;        }        public void setRemarks(String remarks) {            this.remarks = remarks;        }        public String getCompanyId() {            return companyId;        }        public void setCompanyId(String companyId) {            this.companyId = companyId;        }        public String getTotalAmount() {            return totalAmount;        }        public void setTotalAmount(String totalAmount) {            this.totalAmount = totalAmount;        }        public String getBranchId() {            return branchId;        }        public void setBranchId(String branchId) {            this.branchId = branchId;        }        public String getSaleBy() {            return saleBy;        }        public void setSaleBy(String saleBy) {            this.saleBy = saleBy;        }        public String getSalesTo() {            return salesTo;        }        public void setSalesTo(String salesTo) {            this.salesTo = salesTo;        }        public String getBranchNameEn() {            return branchNameEn;        }        public void setBranchNameEn(String branchNameEn) {            this.branchNameEn = branchNameEn;        }    }}