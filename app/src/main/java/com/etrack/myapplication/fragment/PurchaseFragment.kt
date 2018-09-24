package com.etrack.myapplication.fragment


import android.app.Activity
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
import com.etrack.myapplication.`interface`.UpdateList
import com.etrack.myapplication.adapter.PrurchaseSelectedListAdapter
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ProgressUtils
import com.etrack.myapplication.commonUtils.SharedPreferenceHelper
import com.etrack.myapplication.controller.ApiClient
import com.etrack.myapplication.controller.MainActivity
import com.etrack.myapplication.model.inputService.*
import com.etrack.myapplication.model.outputService.GetBillNoGroup
import com.etrack.myapplication.model.outputService.GetBranchsSuccessFailure
import com.etrack.myapplication.model.outputService.GetItemListSuccessFailure
import com.etrack.myapplication.model.outputService.SuccessFailure
import kotlinx.android.synthetic.main.custom_branch_dialoge.*
import kotlinx.android.synthetic.main.fragment_purchase.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class PurchaseFragment : Fragment(), UpdateList {
    //    private lateinit var root: View;
    private lateinit var bt_addItem: Button
    private lateinit var bt_scan: Button
    private lateinit var et_billdate: EditText
    private lateinit var et_totalamount: EditText
    private lateinit var et_billno: EditText
    private lateinit var spGroup: Spinner
    private lateinit var et_remark: EditText
    private lateinit var bt_sav_bill: Button
    private lateinit var mContext: Context
    lateinit var dialog: Dialog;
    private lateinit var mHom: MainActivity
    private var supplierList: List<GetBillNoGroup.DataObject>? = null
    private lateinit var selectedAdapter: PrurchaseSelectedListAdapter
    //private var productList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    // private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    private lateinit var rv_ItemList: RecyclerView
    private var itemCodeLis: String = ""
    private var itemQtyLis: String = ""
    private var itemPriceLis: String = ""
    private var branchId:String=""
    private lateinit var spBranchList:Spinner
    private lateinit var branches: ArrayList<GetBranchsSuccessFailure.GetBranchDataObject>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mHom = (activity as MainActivity?)!!

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater!!.inflate(R.layout.fragment_purchase, container, false)
        productList.clear()
        selectedProductList.clear()
        bt_addItem = root.bt_addItem
        rv_ItemList = root.rv_ItemList
        et_billdate = root.et_billdate
        spGroup = root.spGroup
        et_billno = root.et_billno
        et_remark = root.et_remark
        bt_scan = root.bt_scan
        bt_sav_bill = root.bt_sav_bill
        et_totalamount = root.et_totalamount

        getAllItems();
        getBillno()
        setCurrentDate()
        bt_sav_bill.setOnClickListener {
            if(et_billdate.text.toString().length>0 && et_billno.text.toString().length>0  && et_remark.text.toString().length>0
            &&et_totalamount.text.toString().length>0 && selectedProductList.size>0 &&  (spGroup
                            .selectedItem as KeyValue).key!= null  ) {

                selectedProductList.forEach {
                    itemCodeLis = it.itemCode + "," + itemCodeLis
                    itemQtyLis = it.customerQty.toString() + "," + itemQtyLis
                    itemPriceLis = it.costPrice + "," + itemPriceLis
                }
                itemCodeLis = removeChar(itemCodeLis)
                itemQtyLis = removeChar(itemQtyLis)
                itemPriceLis = removeChar(itemPriceLis)
                saveBillGendrate()
            }else{
                Toast.makeText(mContext,getString(R.string.please_enter_all_field),Toast.LENGTH_SHORT).show()
            }
        }
        bt_scan.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = ScanedProductFragment(mContext, this)
            dialogFragment.show(fragmentManager, "scan Fragment")
        })
        bt_addItem.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = ProductListFragment(mContext, this)
            dialogFragment.show(fragmentManager, "selectItem Fragment")
        })
        spGroup!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //  itemCode = (parent.selectedItem as KeyValue).key
                /*   itemList!!.forEach {
                       if(it.itemCode.toString().equals(itemCode)){
                           itemName.setText(it.itemNameEn)
                           itemArabName.setText(it.itemNameAr)
                           catogorey.setText(it.categoryNameEn)
                           openningQty.setText(it.openingQuantity)
                           costPrice.setText(it.costPrice)
                           salePrice.setText(it.salesPrice)
                           netPrice.setText(it.unitPrice)
                           // v.setText(it.itemNameAr)
                       }

                   }*/
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        return root
    }

    private fun removeChar(str:String): String {
        var str = str
        if (str != null && str.length > 0 ) {
            str = str.substring(0, str.length - 1);
        }
        return str;
    }
    private fun saveBillGendrate() {
        var purchaseOrder = PurchaseOrder()
        var objectItem = purchaseOrder.Objpurchase()
        objectItem.purchaseCode = et_billno.text.toString()
        objectItem.purchaseDate = et_billdate.text.toString()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        objectItem.totalAmount = et_totalamount.text.toString()
        objectItem.remarks = et_remark.text.toString()
        objectItem.branchId = CommonUtils.getBranchId(mContext)
        objectItem.purchaseBy = CommonUtils.getUserId(mContext)
        //CommonUtils.getBranchId(mContext) ""
        objectItem.purchaseFrom = (spGroup.selectedItem as KeyValue).key
        objectItem.itemCode = itemCodeLis
        objectItem.itemQuantity = itemQtyLis
        objectItem.itemPrice = itemPriceLis


        purchaseOrder.objpurchase = objectItem
if( !CommonUtils.getBranchId(mContext).equals("0"))
        SavePurchaseOrder(purchaseOrder)
        else
    selectBranch()
    }

    private fun selectBranch() {
        var getBranchs = GetBranchs()
        var getBranch = getBranchs.Objbranch()
        getBranch.branchId = ""
        getBranch.companyId = CommonUtils.getCompanyId(mContext)
        getBranchs.objbranch = getBranch
        getAllBranch(getBranchs)

    }
    private fun getAllBranch(getBranchs: GetBranchs) {
        try {

            ProgressUtils.startProgress(mContext, getString(R.string.getting_branches), getString(R.string.please_wait), false)
            val getAllBranchs = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val branchesCall = getAllBranchs!!.getAllBranch(getBranchs)
            branchesCall.enqueue(object : Callback<GetBranchsSuccessFailure> {
                override fun onResponse(call: Call<GetBranchsSuccessFailure>, response: Response<GetBranchsSuccessFailure>) {
                    if (response.body() != null) {
                        val branchList = ArrayList<KeyValue>()
                        if (response.body()!!.message.equals("true")) {
                            branches = response.body()!!.dataObject as ArrayList<GetBranchsSuccessFailure.GetBranchDataObject>
                            if (branches.size != 0) {
                                showDialog(activity)
                                branches.forEach {
                                    branchList.add(KeyValue(it.branchId.toString(), it.branchNameEn))
                                }
                                val adapter = ArrayAdapter<KeyValue>(context, android.R.layout.simple_spinner_dropdown_item, branchList)
                                spBranchList.setAdapter(adapter)
                                //             Toast.makeText(mContext, "Company created succefully", Toast.LENGTH_SHORT).show()
                            } else {
                                mHom!!.showCreateBranch()
                            }

                        } else
                            Toast.makeText(mContext, "failed to retrive ", Toast.LENGTH_LONG).show()
                        ProgressUtils.stopProgress()
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_LONG)
                        // retryAgain();
                        Log.e(ContentValues.TAG, "failed to retrive branch ")
                    }

                }

                override fun onFailure(call: Call<GetBranchsSuccessFailure>, t: Throwable) {
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


    fun showDialog(activity: Activity) {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_branch_dialoge)
         spBranchList = dialog.spBranchList as Spinner
        val bt_select = dialog.bt_select as Button
        spBranchList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                branchId = (parent.selectedItem as KeyValue).key
                //    Toast.makeText(context, "Country ID: " + country.getId() + ",  Country Name : " + country.getName(), Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        bt_select.setOnClickListener {
            SharedPreferenceHelper.setSharedPreferenceString(mContext,
                    SharedPreferenceHelper.PREF_APP_BRANCH, branchId)
            dialog.dismiss()
        }
        dialog.show()

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
            val billNoCall = billnoApi!!.getNextBillNo(item)
            billNoCall.enqueue(object : Callback<GetBillNoGroup> {
                override fun onResponse(call: Call<GetBillNoGroup>, response: Response<GetBillNoGroup>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            et_billno.setText(response.body()!!.code.toString())
                            supplierList = response.body()!!.dataObject as ArrayList<GetBillNoGroup.DataObject>

                            if (supplierList!!.size != 0) {
                                var KeyValue = ArrayList<KeyValue>()
                                supplierList!!.forEach {
                                    AddItemFragment.data = com.etrack.myapplication.model.inputService.KeyValue(it.accountCode, it.accountNameEn)
                                    KeyValue.add(AddItemFragment.data!!)
                                }
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
        val selectedAdapter = PrurchaseSelectedListAdapter(mContext, this)
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
        PurchaseFragment.selectedProductList!!.forEach {
            //  if (it.id.equals(productId)) {
            if (it.customerQty == null)
                it.customerQty = 0
            TotalAmount += (it.customerQty * it.costPrice.toDouble())
        }
        TotalAmount =  "%.3f".format(TotalAmount).toDouble()
        et_totalamount.setText(TotalAmount.toString())
    }

    private fun SavePurchaseOrder(item: PurchaseOrder) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_product_list), getString(R.string.please_wait), false)
            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val productsCall = productsApi!!.savePurchaseOrder(item)
            productsCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            mHom.showPurchaseItem()
                        }
                        else
                            CommonUtils.makeText(mContext, getString(R.string
                                    .please_regendrate_bill), Toast
                                    .LENGTH_SHORT)
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

    override fun onResume() {
        mHom.setDrawerState(true)
            mHom!!.setTitle(getString(R.string.purchase_order))
        super.onResume()
    }
}
