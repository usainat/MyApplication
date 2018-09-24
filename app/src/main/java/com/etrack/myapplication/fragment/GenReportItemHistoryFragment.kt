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
import com.etrack.myapplication.adapter.ReportByItemHistAdapter
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.GetItem
import com.etrack.myapplication.model.inputService.GetReportItemHist
import com.etrack.myapplication.model.inputService.KeyValue
import com.etrack.myapplication.model.outputService.GetBillNoGroup
import com.etrack.myapplication.model.outputService.GetCatogoreySuccessFailure
import com.etrack.myapplication.model.outputService.GetItemListSuccessFailure
import com.etrack.myapplication.model.outputService.ReportItemHist
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_dialoge.*
import kotlinx.android.synthetic.main.fragment_reportbyitem_history.*
import kotlinx.android.synthetic.main.fragment_reportbyitem_history.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class GenReportItemHistoryFragment : Fragment() {
    //    private lateinit var root: View;
    private lateinit var bt_get_report: Button
    private lateinit var et_from_date: EditText
    private lateinit var et_to_date: EditText
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private lateinit var spItemList: Spinner
    private lateinit var et_openning_qty: EditText
    private lateinit var et_sale_qty: EditText
    private lateinit var et_puschase_qty: EditText
    private lateinit var et_stk_minus_qty: EditText
    private lateinit var et_stk_Plus_qty: EditText
    private lateinit var et_available_qty: EditText
    private var supplierList: List<GetBillNoGroup.DataObject>? = null
    private var catList: List<GetCatogoreySuccessFailure.GetCatogoreyDataObject>? = null
    private val myCalendar = Calendar.getInstance()
    private lateinit var selectedAdapter: PrurchaseSelectedListAdapter
    private var itemCodeLis: String = ""
    lateinit var dialog: Dialog;
    private var type: String = ""
    private lateinit var rv_items: RecyclerView
    private var itemList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    private var mReportListAdapter: ReportByItemHistAdapter? = null
    private var reportList: ArrayList<ReportItemHist.DataObject>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mHom = (activity as MainActivity?)!!

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater!!.inflate(R.layout.fragment_reportbyitem_history, container, false)
        bt_get_report = root.bt_get_report
        et_to_date = root.et_Itembarcode
        rv_items = root.rv_items
        spItemList = root.spItemList
        et_openning_qty = root.et_openning_qty
        et_sale_qty = root.et_sale_qty
        et_stk_minus_qty = root.et_stk_minus_qty
        et_stk_Plus_qty = root.et_stk_Plus_qty
        et_available_qty = root.et_available_qty
        et_puschase_qty = root.et_puschase_qty
        //showDialog(activity)
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
            var reportBYBill = GetReportItemHist()
            var reportObject = reportBYBill.Objitem()
            reportObject.itemCode = (spItemList.selectedItem as KeyValue).key
            reportObject.companyId = CommonUtils.getCompanyId(mContext)
            reportBYBill.objitem = reportObject
            serviceItemListReport(reportBYBill)
        }

        spItemList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //  itemCode = (parent.selectedItem as KeyValue).key
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        getAllItems()
        return root
    }

    private fun getAllItems() {
        var getItem = GetItem()
        var objectItem = getItem.Objitem()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        objectItem.itemCode = ""
        getItem.objitem = objectItem
        GetProduct(getItem)

    }


    private fun GetProduct(item: GetItem) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_product_list), getString(R.string.please_wait), false)
            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val productsCall = productsApi!!.getAllProductsList(item)
            productsCall.enqueue(object : Callback<GetItemListSuccessFailure> {
                override fun onResponse(call: Call<GetItemListSuccessFailure>, response: Response<GetItemListSuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            itemList = response.body()!!.dataObject as ArrayList<GetItemListSuccessFailure.GetItemListDataObject>

                            if (itemList!!.size != 0) {
                                var KeyValue = ArrayList<KeyValue>()
                                itemList!!.forEach {
                                    AddItemFragment.data = com.etrack.myapplication.model.inputService.KeyValue(it.itemCode, it.itemNameEn)
                                    KeyValue.add(AddItemFragment.data!!)
                                }
                                val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, KeyValue)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spItemList!!.adapter = adapter
                            }
                            ProgressUtils.stopProgress()
                        } else {
                            ProgressUtils.stopProgress()
                            CommonUtils.makeText(mContext, "failed to delete " + response.body()!!.message.toString(), Toast.LENGTH_SHORT)
                            // retryAgain();
                            Log.e(ContentValues.TAG, "Login Failure")
                        }
                    }

                }

                override fun onFailure(call: Call<GetItemListSuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(ContentValues.TAG, "Add Branch Failure" + t.toString())
                }
            })

        } catch (ex: Exception) {
            ProgressUtils.stopProgress()
            Log.e(ContentValues.TAG, "Add Branch Failure" + ex.toString())
        }
    }


    fun showDialog(activity: Activity) {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialoge)
        val bt_purchase = dialog.bt_purchase as Button
        val bt_sales = dialog.bt_sales as Button
        val bt_adjustment = dialog.bt_adjustment as Button
        bt_purchase.setText(getString(R.string.catogorey_report))
        bt_sales.setText(getString(R.string.item_report))
        bt_adjustment.visibility = View.GONE
        bt_purchase.setOnClickListener {
            //   getAllICatog()
            type = "CAT"
            dialog.dismiss()
        }
        bt_sales.setOnClickListener {
            getAllItems()
            type = "ITEM"
            dialog.dismiss()
        }
        dialog.show()

    }


    private fun serviceItemListReport(getReportItemHist: GetReportItemHist) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.get_reports), getString(R
                    .string.please_wait), false)
            val gson = Gson()
            val personString = gson.toJson(getReportItemHist).toString()
            val reportByBill = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val reportByBillCall = reportByBill!!.getReportbyItemHistory(getReportItemHist)
            reportByBillCall.enqueue(object : Callback<ReportItemHist> {
                override fun onResponse(call: Call<ReportItemHist>, response: Response<ReportItemHist>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            reportList = response.body()!!.dataObject as ArrayList<ReportItemHist.DataObject>
                            response.body()!!.DataObject()
                            et_openning_qty.setText(response.body()!!.totalOpeningQuantity)
                            et_sale_qty.setText(response.body()!!.totalSales)
                            et_puschase_qty.setText(response.body()!!.totalPurchase)
                            et_stk_minus_qty.setText(response.body()!!.totalStockadjustMinus)
                            et_stk_Plus_qty.setText(response.body()!!.totalStockadjustPlus)
                            et_available_qty.setText((response.body()!!.totalOpeningQuantity.toInt())
                                    .plus(response.body()!!.totalSales.toInt())
                                    .plus(response.body()!!.totalPurchase.toInt())
                                    .plus(response.body()!!.totalStockadjustPlus.toInt())
                                    .plus(response.body()!!.totalStockadjustMinus.toInt())
                                    .toString())
                            if (reportList != null && reportList!!.size > 0) {
                                mReportListAdapter = ReportByItemHistAdapter(mContext, reportList!!)
                                val mLayoutManager = LinearLayoutManager(mContext)
                                rv_items!!.layoutManager = mLayoutManager
                                rv_items!!.itemAnimator = DefaultItemAnimator()
                                rv_items!!.adapter = mReportListAdapter
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

                override fun onFailure(call: Call<ReportItemHist>, t: Throwable) {
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


    override fun onResume() {
        mHom.setDrawerState(true)
        mHom!!.setTitle(getString(R.string.report_gen))
        super.onResume()
    }
}
