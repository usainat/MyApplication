package com.etrack.myapplication.fragment


import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.CheckUserAvailable
import com.etrack.myapplication.model.inputService.CreateCompany
import com.etrack.myapplication.model.outputService.SuccessFailure
import kotlinx.android.synthetic.main.fragment_registration_comp.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class RegistrationCompFragment : Fragment() {
    private var mHom: MainActivity? = null;
    private lateinit var etCompanyName: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCt: EditText
    private lateinit var etUserName: EditText
    private lateinit var et_password: EditText
    private lateinit var etCompanyArName: EditText
    private lateinit var et_confirm_password: EditText
    private lateinit var spContryList: Spinner
    private lateinit var btn_register: Button
    private lateinit var mContext: Context
    private lateinit var password: String
    lateinit var confirm_password: String

    private lateinit var  handler:Handler
    private   var  DELAY:Long =1000
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_registration_comp, container, false)
        mHom = activity as MainActivity?
        etCompanyName = view.etCompanyName
        etCompanyArName = view.etCompanyArName
        etFirstName = view.etFirstName
        etLastName = view.etLastName
        etLastName = view.etLastName
        etAddress = view.etAddressName
        spContryList = view.spContryList
        etEmail = view.etEmail
        etCt = view.etCt
        etUserName = view.etUserName
        et_password = view.et_password
        et_confirm_password = view.et_confirm_password
        btn_register = view.btn_register
        etUserName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                view!!.tv_available.setText(getString(R.string.please_wait))
                val handler = Handler()
                handler.postDelayed({
                    if (etUserName.text.length > 3) {
                        var checkUser = CheckUserAvailable()
                        var objUser = checkUser.Objuser()
                        objUser.userName = etUserName.text.toString()
                        checkUser.objuser = objUser
                        checkName(checkUser)
                    }
                }, 2000)


            }
        });
        btn_register.setOnClickListener(View.OnClickListener {
            var compEnName = etCompanyName.text.trim().toString()
            var compArName = etCompanyArName.text.trim().toString()
            var firstName = etFirstName.text.trim().toString()
            var lastName = etLastName.text.trim().toString()
            var address = etAddress.text.trim().toString()
            var contry = spContryList.selectedItem.toString()
            var email = etEmail.text.trim().toString()
            var ct = etCt.text.toString()
            var userName = etUserName.text.trim().toString()
            password = et_password.text.trim().toString()
            confirm_password = et_confirm_password.text.trim().toString()
            var createCompany = CreateCompany()
            var company = createCompany.Objcomp()
            company.companyNameEn = compEnName
            company.companyNameAr = compArName
            company.firstName = firstName
            company.llastName = lastName
            company.state = ""
            company.companyId = ""
            company.address = address
            company.country = contry
            company.email = email
            company.mobile = ct
            company.userName = userName
            company.password = password
            createCompany.objcomp = company
            if (compEnName.length > 0 && firstName.length > 0 && lastName.length > 0 && address.length > 0 &&
                    isValidEmail(email) && ct.length > 0 && userName.length > 0 &&
                    passwordIsValid() && view!!.tv_available.text.equals(getString(R.string.available)))
                registerService(createCompany);
            else if (!isValidEmail(email))
                Toast.makeText(context, getString(R.string.email_not_vlid), Toast.LENGTH_SHORT).show()
            else if (!passwordIsValid())
                Toast.makeText(context, getString(R.string.password_not_valid), Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, getString(R.string.please_enter_all_field), Toast.LENGTH_SHORT).show()
        })
        return view
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun passwordIsValid(): Boolean {
        if (password.length >= 4 && confirm_password.length >= 4) {
            if (password.equals(confirm_password))
                return true
        } else {
            Toast.makeText(context, getString(R.string.password_too_short), Toast.LENGTH_SHORT).show()
            return false
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    fun checkName(checkUserAvailable: CheckUserAvailable) {
        try {
            val isUserNameAvail = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val checkUserNameCall = isUserNameAvail!!.checkUserAvailable(checkUserAvailable)
            checkUserNameCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            view!!.tv_available.setText(getString(R.string.username_not_avail))
                            view!!.tv_available.visibility = View.VISIBLE
                        } else {
                            view!!.tv_available.setText(getString(R.string.available))
                            view!!.tv_available.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                    }

                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Check User" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Company Register service Failure" + e.toString())
        }

    }

    fun registerService(createCompany: CreateCompany) {
        try {

            ProgressUtils.startProgress(mContext, getString(R.string.adding_company), getString(R.string.please_wait), false)
            val registerCompany = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val registerCall = registerCompany!!.createCompany(createCompany)
            registerCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            Toast.makeText(mContext, "Company created succefully", Toast.LENGTH_SHORT).show()
                            clearAllField();
                            mHom!!.showLoginScreen()
                        } else
                            Toast.makeText(mContext, "Not add", Toast.LENGTH_LONG).show()
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Company Register Failure")
                    }

                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Company Register Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Company Register service Failure" + e.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        mHom!!.setTitle(getString(R.string.register_company))
    }

    private fun clearAllField() {
        etCompanyName.setText("")
        etCompanyArName.setText("")
        etFirstName.setText("")
        etLastName.setText("")
        etLastName.setText("")
        etAddress.setText("")
        etEmail.setText("")
        etCt.setText("")
        etUserName.setText("")
        et_password.setText("")
        et_confirm_password.setText("")
    }

}// Required empty public constructor
