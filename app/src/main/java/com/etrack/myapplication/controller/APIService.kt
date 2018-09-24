package com.etrack.myapplication.controller


/**
 * Created by Hussain on 20-03-2018.
 */


//  private HomeActivity hom;
/*    public TService(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }*/
/*

class TService : IntentService("TService") {
    private var VariableStore: SharedPreferences? = null
    private var sharedPref: SharedPreferences? = null
    private var mContext: Context

    override fun onHandleIntent(intent: Intent?) {
        mContext = applicationContext
        //     hom = (HomeActivity) getApplicationContext();
        val action = intent!!.getStringExtra("action")



        Log.e("TService Action", action.toString())
        if (action == "login") {          // Bismillah. This should be called from TApp and whenever lang changes.
            val customerObjInToClass = intent.extras!!.getParcelable<Login>("l")
            val login = intent.get("logindata")
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

    }

    companion object {
        private val TAG = "Tservice"
    }

}*/
