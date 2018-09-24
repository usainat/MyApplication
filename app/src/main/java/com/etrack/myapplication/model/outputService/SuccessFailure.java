package com.etrack.myapplication.model.outputService;

/**
 * Created by Hussain on 23-02-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessFailure {

    @SerializedName("Exception")
    @Expose
    private String exception;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DataObject")
    @Expose
    private String dataObject;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDataObject() {
        return dataObject;
    }

    public void setDataObject(String dataObject) {
        this.dataObject = dataObject;
    }

}
