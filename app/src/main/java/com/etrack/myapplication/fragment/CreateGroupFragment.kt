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
import com.etrack.myapplication.model.inputService.AddCatogorey
import com.etrack.myapplication.model.inputService.AddCatogoreyObjcat
import com.etrack.myapplication.model.outputService.SuccessFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class CreateGroupFragment : Fragment() {
    private var groupName: EditText? = null
    private var discount: EditText? = null
    private var createCatogory: Button? = null
    private var mContext: Context? = null
    private lateinit var mHom: MainActivity
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_create_group, container, false)
        groupName = root?.findViewById<EditText>(R.id.etGroupName)
        createCatogory = root?.findViewById<Button>(R.id.btn_create_group)
        discount = root?.findViewById<EditText>(R.id.etDiscoutPercentage)
        createCatogory?.setOnClickListener(View.OnClickListener {

            if (groupName?.text.toString().length > 0 && discount?.text.toString().length > 0)
                saveCatagory(groupName?.text.toString(), discount?.text.toString());
        })
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mHom = activity as MainActivity
    }

    private fun saveCatagory(groupName: String, discount: String) {
        try {
            var company = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_COMPANY, "0")
            var branch = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_BRANCH, "empty")
            ProgressUtils.startProgress(mContext, getString(R.string.adding_catogorey), getString(R.string.please_wait), false)
            val addCatogoreyObject = AddCatogoreyObjcat()
            addCatogoreyObject.categoryName = groupName
            addCatogoreyObject.categoryNameAr = ""
            addCatogoreyObject.companyId = company
            addCatogoreyObject.categoryCode = ""
            addCatogoreyObject.discount = discount
            val addCatogorey = AddCatogorey()
            addCatogorey.objcat = addCatogoreyObject
            val registerCitizen = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val registerCall = registerCitizen!!.createCatagorey(addCatogorey)
            registerCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true"))
                            Toast.makeText(mContext, "Add succefully", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(mContext, "Not add", Toast.LENGTH_SHORT).show()
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Save Catogorey Failure")
                    }

                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Save Csatogorey Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Savec Catogr catch exception" + e.toString())
        }

    }

    override fun onResume() {
        mHom!!.setTitle(getString(R.string.create_catogory))
        super.onResume()
    }
}
