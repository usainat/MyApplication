package com.etrack.myapplication.model.inputService;import com.google.gson.annotations.Expose;import com.google.gson.annotations.SerializedName;/** * Created by Hussain on 29-03-2018. */public class CreateNewUser {    @SerializedName("objuser")    @Expose    private Objuser objuser;    public Objuser getObjuser() {        return objuser;    }    public void setObjuser(Objuser objuser) {        this.objuser = objuser;    }    public class Objuser {        @SerializedName("user_id")        @Expose        private String userId;        @SerializedName("user_name")        @Expose        private String userName;        @SerializedName("password")        @Expose        private String password;        @SerializedName("first_name")        @Expose        private String firstName;        @SerializedName("last_name")        @Expose        private String lastName;        @SerializedName("email")        @Expose        private String email;        @SerializedName("mobile")        @Expose        private String mobile;        @SerializedName("user_role")        @Expose        private String userRole;        @SerializedName("branch_id")        @Expose        private String branchId;        @SerializedName("company_id")        @Expose        private String companyId;        public String getUserId() {            return userId;        }        public void setUserId(String userId) {            this.userId = userId;        }        public String getUserName() {            return userName;        }        public void setUserName(String userName) {            this.userName = userName;        }        public String getPassword() {            return password;        }        public void setPassword(String password) {            this.password = password;        }        public String getFirstName() {            return firstName;        }        public void setFirstName(String firstName) {            this.firstName = firstName;        }        public String getLastName() {            return lastName;        }        public void setLastName(String lastName) {            this.lastName = lastName;        }        public String getEmail() {            return email;        }        public void setEmail(String email) {            this.email = email;        }        public String getMobile() {            return mobile;        }        public void setMobile(String mobile) {            this.mobile = mobile;        }        public String getUserRole() {            return userRole;        }        public void setUserRole(String userRole) {            this.userRole = userRole;        }        public String getBranchId() {            return branchId;        }        public void setBranchId(String branchId) {            this.branchId = branchId;        }        public String getCompanyId() {            return companyId;        }        public void setCompanyId(String companyId) {            this.companyId = companyId;        }    }}