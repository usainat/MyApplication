package com.etrack.myapplication.model.outputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGroupAccount {

    @SerializedName("Exception")
    @Expose
    private Object exception;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DataObject")
    @Expose
    private List<DataObject> dataObject = null;

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

    public List<DataObject> getDataObject() {
        return dataObject;
    }

    public void setDataObject(List<DataObject> dataObject) {
        this.dataObject = dataObject;
    }

    public class DataObject {

        @SerializedName("group_code")
        @Expose
        private String groupCode;
        @SerializedName("group_name")
        @Expose
        private String groupName;

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }


    }
}
