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
import com.etrack.myapplication.model.inputService.AddBranch
import com.etrack.myapplication.model.outputService.SuccessFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class CreateBranchFragment : Fragment() {
    private lateinit var mContext: Context;
    private lateinit var et_branchname: EditText
    private lateinit var et_branchnameAr: EditText
    private lateinit var et_branchAddress: EditText
private lateinit var mHom:MainActivity
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_create_branch, container, false)
        et_branchname = view.findViewById<EditText>(R.id.et_branchname)
        et_branchnameAr = view.findViewById<EditText>(R.id.et_branchnameAr)
        var btn_createbranch = view.findViewById<Button>(R.id.btn_createbranch)
        et_branchAddress = view.findViewById<EditText>(R.id.et_branchAddress)

        btn_createbranch.setOnClickListener(View.OnClickListener {
            if (et_branchname.text.trim().toString().length > 0 && et_branchAddress.text.trim()
                    .toString()
                    .length > 0)
                addBranchService(et_branchname.text.trim().toString(), et_branchAddress.text.trim
                ().toString(), et_branchAddress.text.trim().toString())
        })
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mHom = activity as MainActivity
    }

    private fun addBranchService(branchNameen: String?, branchNameAr: String?, branchAddress:
    String?) {
        try {
            var company = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_COMPANY, "ABC Company")
            var branch = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_BRANCH, "Muscat")
            ProgressUtils.startProgress(mContext, getString(R.string.adding_branch), getString(R.string.please_wait), false)
            var addBranch = AddBranch()
            var branchObject = addBranch.AddBranchObject()
            branchObject.branchId = ""
            branchObject.branchNameAr = branchNameAr
            branchObject.branchNameEn = branchNameen
            branchObject.branchAddress = branchAddress
            branchObject.companyId = company
            addBranch.objbranch = branchObject
            val registerBranch = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val registerCall = registerBranch!!.createBranch(addBranch)
            registerCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.toString().equals("true")) {
                            Toast.makeText(mContext, "Add Branch succefully", Toast.LENGTH_SHORT).show()
                            et_branchname.setText("")
                            et_branchnameAr.setText("")
                            et_branchAddress.setText("")
                        } else
                            Toast.makeText(mContext, "Not add Branch", Toast.LENGTH_SHORT).show()
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Add Branch Failure")
                    }
                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Add Branch Failure" + t.toString())
                }
            })

        } catch (ex: Exception) {
            Log.e(ContentValues.TAG, "Add Branch Failure" + ex.toString())
        }
    }
    override fun onResume() {
        mHom!!.setTitle(getString(R.string.add_product))
        super.onResume()
    }
}// Required empty public constructor
