package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 23-02-2018.
 */

public class AddCatogorey {

    @SerializedName("objcat")
    @Expose
    private AddCatogoreyObjcat objcat;

    public AddCatogoreyObjcat getObjcat() {
        return objcat;
    }

    public void setObjcat(AddCatogoreyObjcat objcat) {
        this.objcat = objcat;
    }

}
