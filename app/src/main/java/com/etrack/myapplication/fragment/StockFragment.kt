package com.etrack.myapplication.fragment


import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.etrack.myapplication.`interface`.UpdateList
import com.etrack.myapplication.adapter.StockSelectedListAdapter
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.GetBillNo
import com.etrack.myapplication.model.inputService.GetItem
import com.etrack.myapplication.model.inputService.StockAjustment
import com.etrack.myapplication.model.outputService.GetBillNoGroup
import com.etrack.myapplication.model.outputService.GetItemListSuccessFailure
import com.etrack.myapplication.model.outputService.SuccessFailure
import kotlinx.android.synthetic.main.fragment_stock.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class StockFragment : android.support.v4.app.Fragment(), UpdateList {
    //    private lateinit var root: View;
    private lateinit var bt_addItem: Button
    private lateinit var bt_scan: Button
    private lateinit var et_billdate: EditText
    private lateinit var et_totalamount: EditText
    private lateinit var et_billno: EditText
    private lateinit var spGroup: Spinner
    private lateinit var bt_sav_bill: Button
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private var supplierList: List<GetBillNoGroup.DataObject>? = null
    private var itemCodeLis: String = ""
    private var itemQtyLis: String = ""
    private var itemTypeLis: String = ""
    //private lateinit var selectedAdapter: PrurchaseSelectedListAdapter
    //private var productList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    // private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    private lateinit var rv_ItemList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productList.clear()
        selectedProductList.clear()
        mContext = activity
        mHom = (activity as MainActivity?)!!

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater!!.inflate(R.layout.fragment_stock, container, false)

        bt_addItem = root.bt_addItem
        rv_ItemList = root.rv_ItemList
        et_billdate = root.et_billdate
        et_billno = root.et_billno
        bt_scan = root.bt_scan
        bt_sav_bill = root.bt_sav_bill
        et_totalamount = root.et_totalamount
//        spGroup.visibility = View.GONE
        et_totalamount.visibility = View.GONE
        getAllItems();
        getBillno()
        setCurrentDate()
        bt_scan.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = ScanedStockFragment(mContext, this)
            dialogFragment.show(fragmentManager, "scan Fragment")
        })
        bt_addItem.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = StockListFragment(mContext, this)
            dialogFragment.show(fragmentManager, "selectItem Fragment")
        })
        bt_sav_bill.setOnClickListener {
            if (et_remark.text.toString().length > 0 && et_billdate.text.toString().length > 0 &&
                    et_billno.text.toString().length > 0
                    && StockFragment.selectedProductList.size > 0) {

                StockFragment.selectedProductList.forEach {
                    itemCodeLis = it.itemCode + "," + itemCodeLis
                    if (it.customerQty.toString().contains("-")) {
                        itemQtyLis = removeFirstChar(it.customerQty.toString()) + "," + itemQtyLis
                        itemTypeLis = "2" + "," + itemTypeLis
                        //2 for minus char
                    } else {
                        itemQtyLis = it.customerQty.toString() + "," + itemQtyLis
                        itemTypeLis = "1" + "," + itemTypeLis
                    }

                    // itemTypeLis = it.costPrice + "," + itemTypeLis

                }
                itemCodeLis = removeChar(itemCodeLis)
                itemQtyLis = removeChar(itemQtyLis)
                itemTypeLis = removeChar(itemTypeLis)
                //   itemPriceLis = removeChar(itemPriceLis)
                saveBillStockGendrate()
            } else {
                Toast.makeText(mContext, getString(R.string.please_enter_all_field), Toast
                        .LENGTH_SHORT).show()
            }
        }
        return root
    }

    private fun saveBillStockGendrate() {
        var stockAjustment = StockAjustment()
        var objectItem = stockAjustment.Objsta()
        objectItem.stadjustCode = et_billno.text.toString()
        objectItem.stadjustDate = et_billdate.text.toString()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        objectItem.remarks = et_remark.text.toString()
        objectItem.branchId = CommonUtils.getBranchId(mContext)
        objectItem.stadjustBy = CommonUtils.getUserId(mContext)
        //CommonUtils.getBranchId(mContext) ""
        objectItem.itemCode = itemCodeLis
        objectItem.itemQuantity = itemQtyLis
        objectItem.stadjustType = itemTypeLis

        stockAjustment.objsta = objectItem

        SaveStockAjuOrder(stockAjustment)
    }

    private fun removeChar(str: String): String {
        var str = str
        if (str != null && str.length > 0) {
            str = str.substring(0, str.length - 1);
        }
        return str;
    }

    private fun removeFirstChar(str: String): String {
        var str = str
        if (str != null && str.length > 0) {
            str = str.substring(1, str.length);
        }
        return str;
    }

    private fun setCurrentDate() {
        var currentDateTimeString = DateFormat.getDateTimeInstance().format(Date());

        var df = SimpleDateFormat("dd-MM-yyyy");
        var date = df!!.format(Calendar.getInstance().getTime());
        et_billdate.setText(date.toString())
        /*   val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)*/
    }

    private fun getBillno() {
        var getBillNo = GetBillNo()
        var objectItem = getBillNo.Objitem()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        getBillNo.objitem = objectItem
        GetBillService(getBillNo)
    }

    private fun GetBillService(item: GetBillNo) {
        try {
//           ProgressUtils.startProgress(mContext, getString(R.string.getting_bill_no), getString(R.string.please_wait), false)
            val billnoApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val billNoCall = billnoApi!!.getStockBillNo(item)
            billNoCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            et_billno.setText(response.body()!!.dataObject.toString())
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

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
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

    fun hideKeyboard() {
        /*       if (root != null) {
                   var imm =(InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
               }*/
    }

    private fun getAllItems() {
        var getItem = GetItem()
        var objectItem = getItem.Objitem()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        objectItem.itemCode = ""
        getItem.objitem = objectItem
        GetService(getItem)
    }

    override fun updateList() {
        val selectedAdapter = StockSelectedListAdapter(mContext, this)
        rv_ItemList.adapter = selectedAdapter
        val mLayoutManager = LinearLayoutManager(mContext)
        rv_ItemList!!.layoutManager = mLayoutManager
        rv_ItemList!!.itemAnimator = DefaultItemAnimator()
        rv_ItemList!!.adapter = selectedAdapter
    }

    private fun GetService(item: GetItem) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_product_list), getString(R.string.please_wait), false)
            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val productsCall = productsApi!!.getAllProductsList(item)
            productsCall.enqueue(object : Callback<GetItemListSuccessFailure> {
                override fun onResponse(call: Call<GetItemListSuccessFailure>, response: Response<GetItemListSuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            productList = response.body()!!.dataObject as ArrayList<GetItemListSuccessFailure.GetItemListDataObject>
                        }
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Login Failure")
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

    companion object {
        var productList = ArrayList<GetItemListSuccessFailure.GetItemListDataObject>()
        var selectedProductList = ArrayList<GetItemListSuccessFailure.GetItemListDataObject>()
    }

    override fun updateTotalCost() {
        var TotalAmount = 0.000
        SalesFragment.selectedProductList!!.forEach {
            //  if (it.id.equals(productId)) {
            if (it.customerQty == null)
                it.customerQty = 0
            TotalAmount += (it.customerQty * it.unitPrice.toDouble())
        }
    }

    override fun onResume() {
        mHom.setDrawerState(true)
        mHom!!.setTitle(getString(R.string.stock_adjustment))
        super.onResume()
    }


    private fun SaveStockAjuOrder(item: StockAjustment) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_product_list), getString(R.string.please_wait), false)
            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val productsCall = productsApi!!.saveStockAdjustment(item)
            productsCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            mHom.showStockAdjustment()
                        }
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "Login Failure")
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
            ProgressUtils.stopProgress()
            Log.e(ContentValues.TAG, "Add Branch Failure" + ex.toString())
        }
    }

}
