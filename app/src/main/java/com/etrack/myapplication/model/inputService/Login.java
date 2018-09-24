package com.etrack.myapplication.model.inputService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hussain on 09-03-2018.
 */

public class Login {

    @SerializedName("objuser")
    @Expose
    private Objuser objuser;

    public Objuser getObjuser() {
        return objuser;
    }

    public void setObjuser(Objuser objuser) {
        this.objuser = objuser;
    }

    public class Objuser {

        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("password")
        @Expose
        private String password;

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
    }
}