package com.etrack.myapplication.commonUtils

import android.app.ProgressDialog
import android.content.Context

/**
 * Created by Hussain on 19-02-2018.
 */
   object ProgressUtils {
    var progressDialog: ProgressDialog? = null
    @JvmStatic
    fun startProgress(context: Context?, Title: String, message: String, cancallable: Boolean?) {
        progressDialog = ProgressDialog.show(context, Title, message, false,true)
    }

    fun stopProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}