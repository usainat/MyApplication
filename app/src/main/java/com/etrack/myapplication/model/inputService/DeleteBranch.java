package com.etrack.myapplication.model.inputService;import com.google.gson.annotations.Expose;import com.google.gson.annotations.SerializedName;/** * Created by Hussain on 13-04-2018. */public class DeleteBranch {    @SerializedName("objbranch")    @Expose    private Objitem objBranch;    public Objitem getObjitem() {        return objBranch;    }    public void setobjBranch(Objitem objitem) {        this.objBranch = objitem;    }    public class Objitem {        @SerializedName("branch_id")        @Expose        public String branchId;        public String getbranchId() {            return branchId;        }        public void setbranchId(String branchId) {            this.branchId = branchId;        }    }}