package com.etrack.myapplication.service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.commonUtils.SharedPreferenceHelper
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.model.inputService.Login
import com.etrack.myapplication.model.outputService.LoginSuccessFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Hussain on 20-03-2018.
 */


private fun callLoginService(mContext: Context, login: Login) {
    try {
        ProgressUtils.startProgress(mContext, mContext.getString(R.string.adding_branch), mContext.getString(R.string.please_wait), false)
        val loginApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
        val loginCall = loginApi!!.login(login)
        loginCall.enqueue(object : Callback<LoginSuccessFailure> {
            override fun onResponse(call: Call<LoginSuccessFailure>, response: Response<LoginSuccessFailure>) {
                if (response.body() != null) {
                    if (response.raw().message().toString().equals("true")) {
                        var company = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_COMPANY, "empty")
                        var branch = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_BRANCH, "empty")
                    }
                    ProgressUtils.stopProgress()
                } else {
                    ProgressUtils.stopProgress()
                    CommonUtils.makeText(mContext, mContext.getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    // retryAgain();
                    Log.e(ContentValues.TAG, "Login Failure")
                }
            }

            override fun onFailure(call: Call<LoginSuccessFailure>, t: Throwable) {
                ProgressUtils.stopProgress()
                //  retryAgain();
                CommonUtils.makeText(mContext, mContext.getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                Log.e(ContentValues.TAG, "Add Branch Failure" + t.toString())
            }
        })

    } catch (ex: Exception) {
        Log.e(ContentValues.TAG, "Add Branch Failure" + ex.toString())
    }

}