package com.etrack.myapplication.fragment


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.KeyValue
import com.etrack.myapplication.model.inputService.SaveAccount
import com.etrack.myapplication.model.outputService.GetGroupAccount
import com.etrack.myapplication.model.outputService.SuccessFailure
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AddAccountFragment : Fragment() {
    private var et_acc_code: EditText? = null
    private var et_acc_name: EditText? = null
    private var btn_add_item: Button? = null
    private var spGroup: Spinner? = null
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private var catCode: String? = null
    private var catList: List<GetGroupAccount.DataObject>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_create_account, container, false)
        et_acc_code = root!!.et_acc_code
        et_acc_name = root!!.et_acc_name
        spGroup = root!!.spGroup
        btn_add_item = root!!.btn_add_account
        mContext = getActivity()

        getGroupList();
        spGroup!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                catCode = (parent.selectedItem as KeyValue).key
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        btn_add_item!!.setOnClickListener { v ->
            if ( et_acc_name!!.text.toString().trim().length > 0 ) {
                var saveAccount = SaveAccount()
                var savobjectAccount = saveAccount.Objacnt()
                savobjectAccount.accountCode = ""
                savobjectAccount.accountGroup = (spGroup!!.selectedItem as KeyValue).key
                savobjectAccount.accountNameAr = ""
                savobjectAccount.accountNameEn = et_acc_name!!.text.toString().trim()
                savobjectAccount.companyId = CommonUtils.getCompanyId(mContext)
                saveAccount.objacnt = savobjectAccount
                callAddAccountApi(saveAccount)
            } else {
                Toast.makeText(mContext, getString(R.string.please_enter_all_field), Toast.LENGTH_LONG).show()
            }
        }

        return root
    }


    private fun getGroupList() {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_list_type), getString(R.string.please_wait), false)
            //            val gson = Gson()
            //       val personString = gson.toJson(register).toString()
            val getAllCatogoreyList = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val getCatogoreyListCall = getAllCatogoreyList!!.getAllAcctTypeList()
            getCatogoreyListCall.enqueue(object : Callback<GetGroupAccount> {
                override fun onResponse(call: Call<GetGroupAccount>, response: Response<GetGroupAccount>) {
                    if (response.body() != null) {
                        catList = response.body()!!.dataObject

                        if (catList!!.size != 0) {
                            var KeyValue = ArrayList<KeyValue>()
                            catList!!.forEach {
                                data = com.etrack.myapplication.model.inputService.KeyValue(it.groupCode, it.groupName)
                                KeyValue.add(data!!)
                            }


                            val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, KeyValue)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spGroup!!.adapter = adapter
                            ProgressUtils.stopProgress()

                        } else {
                            mHom.showAddCatogory()
                        }
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(TAG, "Get account group type Failure")
                    }

                }

                override fun onFailure(call: Call<GetGroupAccount>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(TAG, "Get account group type Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(TAG, "Get account group type Failure" + e.toString())
        }

    }

    private fun callAddAccountApi(saveAccount: SaveAccount) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.adding_account), getString(R.string.please_wait), false)

            val saveItemApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val saveItemCall = saveItemApi!!.saveAccountApi(saveAccount)
            saveItemCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            clearfield()
                            Log.i(TAG, "Account added successfully" + response.body()!!.toString())
                        } else if (response.body()!!.message.equals("Already Exist")) {
                            CommonUtils.makeText(mContext, getString(R.string.name_already_exist), Toast
                                    .LENGTH_SHORT)
                            ProgressUtils.stopProgress()
                        } else {
                            CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                            ProgressUtils.stopProgress()
                        }
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(TAG, "Account add Failured")
                    }

                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(TAG, " Account add  Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(TAG, " Account add service Failure" + e.toString())
        }

    }

    private fun clearfield() {
        et_acc_code!!.setText("")
        et_acc_name!!.setText("")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHom = activity as MainActivity
    }

    override fun onResume() {
        mHom!!.setTitle(getString(R.string.add_account))
        super.onResume()
    }

    companion object {
        var data: KeyValue? = null
    }
}


