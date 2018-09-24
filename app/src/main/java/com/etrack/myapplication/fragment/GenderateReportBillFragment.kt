package com.etrack.myapplication.fragment


import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.adapter.PrurchaseSelectedListAdapter
import com.etrack.myapplication.adapter.ReportByBillAdapter
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.GetBillNo
import com.etrack.myapplication.model.inputService.KeyValue
import com.etrack.myapplication.model.inputService.ReportByBill
import com.etrack.myapplication.model.outputService.GetBillNoGroup
import com.etrack.myapplication.model.outputService.GetRepotBIllPO
import com.etrack.myapplication.model.outputService.GetRepotBillSO
import com.etrack.myapplication.model.outputService.GetRepotBillSta
import kotlinx.android.synthetic.main.custom_dialoge.*
import kotlinx.android.synthetic.main.fragment_reportbybill.*
import kotlinx.android.synthetic.main.fragment_reportbybill.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class GenderateReportBillFragment : Fragment() {
    //    private lateinit var root: View;
    private lateinit var bt_get_report: Button
    private lateinit var et_from_date: EditText
    private lateinit var tv_typename: TextView
         private lateinit var tv_billamount: TextView
    private lateinit var et_to_date: EditText
    private lateinit var spGroup: Spinner
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private var supplierList: List<GetBillNoGroup.DataObject>? = null
    private val myCalendar = Calendar.getInstance()
    private lateinit var selectedAdapter: PrurchaseSelectedListAdapter
    private var itemCodeLis: String = ""
    lateinit var dialog: Dialog;
    private var type: String = ""
    private lateinit var rv_items: RecyclerView
    private lateinit var et_totalamount: EditText

    private var mReportListAdapter: ReportByBillAdapter? = null
    private var reportListPO: ArrayList<GetRepotBIllPO.DataObject>? =  ArrayList<GetRepotBIllPO.DataObject>()
    private var reportListSO: ArrayList<GetRepotBillSO.DataObject>? = ArrayList<GetRepotBillSO.DataObject>()
    private var reportListSTA: ArrayList<GetRepotBillSta.DataObject>? =  ArrayList<GetRepotBillSta.DataObject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mHom = (activity as MainActivity?)!!

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater!!.inflate(R.layout.fragment_reportbybill, container, false)
        et_from_date = root.et_from_date
        spGroup = root.spGroup
        tv_typename = root.tv_typename
        tv_billamount= root.tv_billamount
        bt_get_report = root.bt_get_report
        et_to_date = root.et_to_date
        rv_items = root.rv_items
        et_totalamount = root.et_totalamount
        setCurrentDate()

        val fromDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd/MM/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            et_from_date.setText(sdf.format(myCalendar.time))
        }
        val toDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd/MM/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            et_to_date.setText(sdf.format(myCalendar.time))
        }
        bt_get_report.setOnClickListener {
            var reportBYBill = ReportByBill()
            var reportObject = reportBYBill.Objreport()
            if (type != "STA")
                try {
                    reportObject.actorCode = (spGroup.selectedItem as KeyValue).key
                } catch (e: Exception) {
                    reportObject.actorCode = ""
                    Toast.makeText(mContext, getString(R.string.no_stock_list
                    ), Toast.LENGTH_SHORT).show()
                }
            else
                reportObject.actorCode = ""
            reportObject.billType = type
            reportObject.companyId = CommonUtils.getCompanyId(mContext)
            reportObject.branchId = CommonUtils.getBranchId(mContext)
            reportObject.fromDate = et_from_date.text.toString()
            reportObject.toDate = et_to_date.text.toString()
            reportBYBill.objreport = reportObject
            if (type == "PO")
                serviceReportBYBillPO(reportBYBill)
            else if (type == "SO")
                serviceReportBYBillSO(reportBYBill)
            else if (type == "STA")
                serviceReportBYBillSTA(reportBYBill)
        }
        et_from_date.setOnClickListener(View.OnClickListener {
            val datePickerDialog = DatePickerDialog(mContext, fromDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        })
        et_to_date.setOnClickListener(View.OnClickListener {
            val datePickerDialog = DatePickerDialog(mContext, toDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        })
        spGroup!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //  itemCode = (parent.selectedItem as KeyValue).key
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        showDialog(activity);
        return root
    }

    private fun serviceReportBYBillSTA(reportBYBill: ReportByBill) {
        try {

            tv_typename.setText(getString(R.string.stock))
            ProgressUtils.startProgress(mContext, getString(R.string.get_reports), getString(R
                    .string.please_wait), false)
            val deleteBranchApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val deleteBranchesCall = deleteBranchApi!!.getReportbyBilSta(reportBYBill)
            deleteBranchesCall.enqueue(object : Callback<GetRepotBillSta> {
                override fun onResponse(call: Call<GetRepotBillSta>, response: Response<GetRepotBillSta>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            tv_billamount.visibility=View.GONE
                            reportListSTA = response.body()!!.dataObject as
                                    ArrayList<GetRepotBillSta.DataObject>
                            response.body()!!.DataObject()
                            if (reportListSTA != null && reportListSTA!!.size > 0) {
                                mReportListAdapter = ReportByBillAdapter(mContext,
                                        reportListPO!!,reportListSO!!, reportListSTA!!,"STA")
                                val mLayoutManager = LinearLayoutManager(mContext)
                                rv_items!!.layoutManager = mLayoutManager
                                rv_items!!.itemAnimator = DefaultItemAnimator()
                                rv_items!!.adapter = mReportListAdapter
                                et_totalamount.visibility =View.GONE
                                ll_setValue.visibility = View.GONE
                                rl_report_value.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(mContext, "No Report available", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            Toast.makeText(mContext, "Getting report failed " + response.body
                            ()!!.message.toString(), Toast.LENGTH_LONG).show()
                            ProgressUtils.stopProgress()
                        }
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "failed to get report ")

                    }
                }

                override fun onFailure(call: Call<GetRepotBillSta>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Get report Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Get report catch Failure" + e.toString())
        }

    }

    private fun serviceReportBYBillSO(reportBYBill: ReportByBill) {
        try {
            tv_typename.setText(getString(R.string.supliername))
            ProgressUtils.startProgress(mContext, getString(R.string.get_reports), getString(R
                    .string.please_wait), false)
            val deleteBranchApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val deleteBranchesCall = deleteBranchApi!!.getReportbyBilSo(reportBYBill)
            deleteBranchesCall.enqueue(object : Callback<GetRepotBillSO> {
                override fun onResponse(call: Call<GetRepotBillSO>, response:
                Response<GetRepotBillSO>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            reportListSO = response.body()!!.dataObject as
                                    ArrayList<GetRepotBillSO.DataObject>
                            response.body()!!.DataObject()
                            if (reportListSO != null && reportListSO!!.size > 0) {
                                mReportListAdapter = ReportByBillAdapter(mContext,
                                        reportListPO!!,reportListSO!!, reportListSTA!!,"SO")
                                val mLayoutManager = LinearLayoutManager(mContext)
                                rv_items!!.layoutManager = mLayoutManager
                                rv_items!!.itemAnimator = DefaultItemAnimator()
                                rv_items!!.adapter = mReportListAdapter
                                et_totalamount.setText(response.body()!!.totalAmount)
                                ll_setValue.visibility = View.GONE
                                rl_report_value.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(mContext, "No Report available", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            Toast.makeText(mContext, "Getting report failed " + response.body
                            ()!!.message.toString(), Toast.LENGTH_LONG).show()
                            ProgressUtils.stopProgress()
                        }
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "failed to get report ")

                    }
                }

                override fun onFailure(call: Call<GetRepotBillSO>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Get report Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Get report catch Failure" + e.toString())
        }

    }

    private fun serviceReportBYBillPO(reportBYBill: ReportByBill) {
        try {
            tv_typename.setText(getString(R.string.supliername))
            ProgressUtils.startProgress(mContext, getString(R.string.get_reports), getString(R
                    .string.please_wait), false)
            val reportByBillPOSO = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val reportByBillPOSOCall = reportByBillPOSO!!.getReportbyBilpo(reportBYBill)
            reportByBillPOSOCall.enqueue(object : Callback<GetRepotBIllPO> {
                override fun onResponse(call: Call<GetRepotBIllPO>, response: Response<GetRepotBIllPO>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            reportListPO = response.body()!!.dataObject as
                                    ArrayList<GetRepotBIllPO.DataObject>
                            response.body()!!.DataObject()
                            if (reportListPO != null && reportListPO!!.size > 0) {
                                mReportListAdapter = ReportByBillAdapter(mContext,
                                        reportListPO!!,reportListSO!!, reportListSTA!!,"PO")
                                val mLayoutManager = LinearLayoutManager(mContext)
                                rv_items!!.layoutManager = mLayoutManager
                                rv_items!!.itemAnimator = DefaultItemAnimator()
                                rv_items!!.adapter = mReportListAdapter
                                et_totalamount.setText(response.body()!!.totalAmount)
                                ll_setValue.visibility = View.GONE
                                rl_report_value.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(mContext, "No Report available", Toast.LENGTH_LONG).show()
                            }

                        } else {
                            Toast.makeText(mContext, "Getting report failed " + response.body
                            ()!!.message.toString(), Toast.LENGTH_LONG).show()
                            ProgressUtils.stopProgress()
                        }
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "failed to get report ")

                    }
                }

                override fun onFailure(call: Call<GetRepotBIllPO>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Get report Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(ContentValues.TAG, "Get report catch Failure" + e.toString())
        }

    }


    private fun setCurrentDate() {
        var currentDateTimeString = DateFormat.getDateTimeInstance().format(Date());

        var df = SimpleDateFormat("dd/MM/yyyy");
        var date = df!!.format(Calendar.getInstance().getTime());
        et_from_date.setText(date.toString())
        et_to_date.setText(date.toString())
    }

    fun showDialog(activity: Activity) {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialoge)
        val bt_purchase = dialog.bt_purchase as Button
        val bt_sales = dialog.bt_sales as Button
        val bt_adjustment = dialog.bt_adjustment as Button
        bt_purchase.setOnClickListener {
            getPurchaseList("1")
            type = "PO"
            dialog.dismiss()
        }
        bt_sales.setOnClickListener {
            getPurchaseList("0")
            type = "SO"
            dialog.dismiss()
        }
        bt_adjustment.setOnClickListener {
            spGroup.visibility = View.GONE
            type = "STA"
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun getPurchaseList(type: String) {
        var getBillNo = GetBillNo()
        var objectItem = getBillNo.Objitem()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        getBillNo.objitem = objectItem
        GetBillService(getBillNo, type)
    }

    private fun GetBillService(item: GetBillNo, type: String) {
        try {
            var billNoCall: Call<GetBillNoGroup>
            if (type.equals("0"))
                ProgressUtils.startProgress(mContext, getString(R.string.getting_supplier_list), getString(R
                        .string
                        .please_wait), false)
            else
                ProgressUtils.startProgress(mContext, getString(R.string.getting_purchase_list),
                        getString(R
                                .string
                                .please_wait), false)
            val billnoApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            if (type.equals("1"))
                billNoCall = billnoApi!!.getNextBillNo(item)
            else
                billNoCall = billnoApi!!.getNextSalesBillNo(item)
            billNoCall.enqueue(object : Callback<GetBillNoGroup> {
                override fun onResponse(call: Call<GetBillNoGroup>, response: Response<GetBillNoGroup>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            supplierList = response.body()!!.dataObject as ArrayList<GetBillNoGroup.DataObject>
                            if (supplierList!!.size != 0) {
                                var KeyValue = ArrayList<KeyValue>()
                                supplierList!!.forEach {
                                    AddItemFragment.data = com.etrack.myapplication.model.inputService.KeyValue(it.accountCode, it.accountNameEn)
                                    KeyValue.add(AddItemFragment.data!!)
                                }
                                AddItemFragment.data = com.etrack.myapplication.model
                                        .inputService.KeyValue("", "All")
                                KeyValue.add(AddItemFragment.data!!)
                                val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, KeyValue)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spGroup!!.adapter = adapter
                            }
                        }
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "get bill no Failure")
                    }
                    ProgressUtils.stopProgress()
                }

                override fun onFailure(call: Call<GetBillNoGroup>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "get bill no Failure" + t.toString())
                }
            })

        } catch (ex: Exception) {
            ProgressUtils.stopProgress()
            Log.e(ContentValues.TAG, " get bill no service Failure" + ex.toString())
        }
    }


    override fun onResume() {
        mHom.setDrawerState(true)
        mHom!!.setTitle(getString(R.string.report_gen))
        super.onResume()
    }
}
