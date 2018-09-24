package com.etrack.myapplication.fragment


import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.commonUtils.SharedPreferenceHelper
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.Login
import com.etrack.myapplication.model.outputService.LoginSuccessFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private var root: View? = null
    private var mHom: MainActivity? = null;
    private var mContext: Context? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHom = activity as MainActivity?
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater!!.inflate(R.layout.fragment_login, container, false)
        var btn_login = root?.findViewById<Button>(R.id.btn_login) as Button
        var username = root?.findViewById<EditText>(R.id.et_username)
        var password = root?.findViewById<EditText>(R.id.et_password)
        btn_login.setOnClickListener(View.OnClickListener {
            if (username.text.length > 0 && password.text.length > 0) {

                var login = Login()
                var loginObject = login.Objuser()
                loginObject.userName = username.text.toString()
                loginObject.password = password.text.toString()
                login.objuser = loginObject

                callLoginService(login)

            } else
                Toast.makeText(activity, getString(R.string.missmatchuserpass), Toast.LENGTH_SHORT).show()

        })
        return root;
    }


    private fun callLoginService(login: Login) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.logingin), getString(R.string.please_wait), false)
            val loginApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val loginCall = loginApi!!.login(login)
            loginCall.enqueue(object : Callback<LoginSuccessFailure> {
                override fun onResponse(call: Call<LoginSuccessFailure>, response: Response<LoginSuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            SharedPreferenceHelper.setSharedPreferenceString(mContext,
                                    SharedPreferenceHelper.PREF_APP_USERROLE, response!!.body()!!.dataObject.get(0).userRole)
                            SharedPreferenceHelper.setSharedPreferenceString(mContext,
                                    SharedPreferenceHelper.PREF_APP_COMPANY, response!!.body()!!.dataObject.get(0).companyId)
                            SharedPreferenceHelper.setSharedPreferenceInt(mContext,
                                    SharedPreferenceHelper.PREF_APP_USERID, response!!.body()!!.dataObject.get(0).userId)
                            SharedPreferenceHelper.setSharedPreferenceString(mContext,
                                    SharedPreferenceHelper.PREF_APP_BRANCH, response!!.body()!!.dataObject.get(0).branchId)
                            SharedPreferenceHelper.setSharedPreferenceString(mContext,
                                    SharedPreferenceHelper.PREF_APP_USERNAME, response!!.body()!!.dataObject.get(0).userName)
                            mHom!!.setHomePage()
                        } else {
                            Toast.makeText(mContext, getString(R.string.not_valid), Toast.LENGTH_SHORT).show()
                        }
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Login Failure")
                    }
                }

                override fun onFailure(call: Call<LoginSuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Login Failure" + t.toString())
                }
            })

        } catch (ex: Exception) {
            ProgressUtils.stopProgress()
            Log.e(ContentValues.TAG, "Add Branch Failure" + ex.toString())
        }

    }
    override fun onResume() {
        super.onResume()
        mHom!!.setTitle(getString(R.string.login))
    }
}
