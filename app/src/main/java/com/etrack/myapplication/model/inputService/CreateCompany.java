package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 04-03-2018.
 */

public class CreateCompany {


    @SerializedName("objcomp")
    @Expose
    private Objcomp objcomp;

    public Objcomp getObjcomp() {
        return objcomp;
    }

    public void setObjcomp(Objcomp objcomp) {
        this.objcomp = objcomp;
    }


    public class Objcomp {

        @SerializedName("company_name_en")
        @Expose
        private String companyNameEn;
        @SerializedName("company_name_ar")
        @Expose
        private String companyNameAr;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("llast_name")
        @Expose
        private String llastName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        public String getCompanyNameEn() {
            return companyNameEn;
        }

        public void setCompanyNameEn(String companyNameEn) {
            this.companyNameEn = companyNameEn;
        }

        public String getCompanyNameAr() {
            return companyNameAr;
        }

        public void setCompanyNameAr(String companyNameAr) {
            this.companyNameAr = companyNameAr;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLlastName() {
            return llastName;
        }

        public void setLlastName(String llastName) {
            this.llastName = llastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

    }
}
