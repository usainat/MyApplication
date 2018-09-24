package com.etrack.myapplication.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.epson.EpsonCom.*
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ApiInterface
import com.etrack.myapplication.`interface`.UpdateList
import com.etrack.myapplication.adapter.SalesSelectedListAdapter
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
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class SalesFragment : Fragment(), UpdateList, CallbackInterface {
    //    private lateinit var root: View;
    private lateinit var bt_addItem: Button
    private lateinit var bt_scan: Button
    private lateinit var et_billdate: EditText
    private lateinit var et_totalamount: EditText
    private lateinit var et_billno: EditText
    private lateinit var spGroup: Spinner
    private lateinit var bt_sav_bill: Button
    private lateinit var et_remark: EditText
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private var itemCodeLis: String = ""
    private var itemQtyLis: String = ""
    private var itemPriceLis: String = ""
    private lateinit var til_balanceamount: TextInputLayout
    private lateinit var til_amountrecived: TextInputLayout
    private lateinit var et_balanceamount: EditText
    private lateinit var et_amountrecived: EditText
    private lateinit var spBranchList: Spinner
    private lateinit var branches: ArrayList<GetBranchsSuccessFailure.GetBranchDataObject>
    private var supplierList: List<GetBillNoGroup.DataObject>? = null
    private var m_Device: EpsonComDevice? = null
    private var m_DeviceParameters: EpsonComDeviceParameters? = null
    private var salesOrder: SalesOrder? = null
    //private lateinit var selectedAdapter: PrurchaseSelectedListAdapter
    //private var productList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    // private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject>? = null
    private lateinit var rv_ItemList: RecyclerView
    private var branchId: String = ""
    lateinit var dialog: Dialog;
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
        var root = inflater!!.inflate(R.layout.fragment_purchase, container, false)

        bt_addItem = root.bt_addItem
        rv_ItemList = root.rv_ItemList
        et_billdate = root.et_billdate
        spGroup = root.spGroup
        et_billno = root.et_billno
        bt_scan = root.bt_scan
        bt_sav_bill = root.bt_sav_bill
        et_totalamount = root.et_totalamount
        et_remark = root.et_remark
        til_balanceamount = root.til_balanceamount
        til_amountrecived = root.til_amountrecived
        et_amountrecived = root.et_amountrecived
        et_balanceamount = root.et_balanceamount
        getAllItems();
        getBillno()
        setCurrentDate()
        til_balanceamount.visibility = View.VISIBLE
        til_amountrecived.visibility = View.VISIBLE
        et_amountrecived.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (et_totalamount
                                .text.length > 0 && et_amountrecived.text.length > 0)
                    et_balanceamount.setText("%.3f".format(et_amountrecived.text.toString()
                            .toDouble().minus(et_totalamount
                                    .text.toString().toDouble())).toString())
                else
                    CommonUtils.makeText(mContext, getString(R.string.plz_total_amount), Toast
                            .LENGTH_SHORT)

            }

        });
        bt_scan.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = ScanedSalesFragment(mContext, this)
            dialogFragment.show(fragmentManager, "scan Fragment")
        })
        bt_addItem.setOnClickListener(View.OnClickListener {
            val fragmentManager = fragmentManager
            //  productList, keyValueProductId
            val dialogFragment = SalesListFragment(mContext, this)
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
                           unitPrice.setText(it.unitPrice)
                           salePrice.setText(it.salesPrice)
                           netPrice.setText(it.unitPrice)
                           // v.setText(it.itemNameAr)
                       }

                   }*/
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        bt_sav_bill.setOnClickListener {
            if (et_billdate.text.toString().length > 0 && et_billno.text.toString().length > 0 && et_remark.text.toString().length > 0
                    && et_totalamount.text.toString().length > 0 && SalesFragment.selectedProductList
                            .size > 0 && (spGroup
                            .selectedItem as KeyValue).key != null) {
                itemCodeLis = ""
                itemQtyLis = ""
                itemPriceLis =""
                SalesFragment.selectedProductList.forEach {
                    itemCodeLis = it.itemCode + "," + itemCodeLis
                    itemQtyLis = it.customerQty.toString() + "," + itemQtyLis
                    itemPriceLis = it.salesPrice + "," + itemPriceLis

                }
                itemCodeLis = removeChar(itemCodeLis)
                itemQtyLis = removeChar(itemQtyLis)
                itemPriceLis = removeChar(itemPriceLis)
                saveBillGendrate()
            } else {
                Toast.makeText(mContext, getString(R.string.please_enter_all_field), Toast.LENGTH_SHORT).show()
            }
        }

        return root
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

    private fun getBillno() {
         var getBillNo = GetBillNo()
        var objectItem = getBillNo.Objitem()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        getBillNo.objitem = objectItem
        GetBillService(getBillNo)
    }

    private fun removeChar(str: String): String {
        var str = str
        if (str != null && str.length > 0) {
            str = str.substring(0, str.length - 1);
        }
        return str;
    }

    private fun saveBillGendrate() {
        salesOrder = SalesOrder()
        var objectItem = salesOrder!!.Objsales()
        objectItem.salesCode = et_billno.text.toString()
        objectItem.salesDate = et_billdate.text.toString()
        objectItem.companyId = CommonUtils.getCompanyId(mContext)
        objectItem.totalAmount = et_totalamount.text.toString()
        objectItem.remarks = et_remark.text.toString()
        objectItem.branchId = CommonUtils.getBranchId(mContext)
        objectItem.salesBy = CommonUtils.getUserId(mContext)
        //CommonUtils.getBranchId(mContext) ""
        objectItem.salesTo = (spGroup.selectedItem as KeyValue).key
        objectItem.itemCode = itemCodeLis
        objectItem.itemQuantity = itemQtyLis
        objectItem.itemPrice = itemPriceLis
        salesOrder!!.objsales = objectItem
        if (!CommonUtils.getBranchId(mContext).equals("0"))
            SaveSalesOrder(salesOrder!!)
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

    private fun SaveSalesOrder(item: SalesOrder) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.saving_sales_bill), getString(R
                    .string
                    .please_wait), false)
            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val productsCall = productsApi!!.saveSalesOrder(item)
            productsCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            val bundle = Bundle()
                            bundle.putString("billamount", et_totalamount.text.toString())
                            bundle.putString("paidamount", et_amountrecived.text.toString())
                            bundle.putString("balamount", et_balanceamount.text.toString())
                            //         bundle.putParcelable("recipetModel", item)
                            connectAndPrint()
                            // mHom.showSalesFragment()
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

    private fun connectAndPrint() {
        m_Device = EpsonComDevice()
        m_DeviceParameters = EpsonComDeviceParameters()
        //register myself as callback
        m_Device!!.registerCallback(this)
        connectToPrinter()
    }

    private fun GetBillService(item: GetBillNo) {
        try {
//           ProgressUtils.startProgress(mContext, getString(R.string.getting_bill_no), getString(R.string.please_wait), false)
            val billnoApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val billNoCall = billnoApi!!.getNextSalesBillNo(item)
            billNoCall.enqueue(object : Callback<GetBillNoGroup> {
                override fun onResponse(call: Call<GetBillNoGroup>, response: Response<GetBillNoGroup>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            et_billno.setText(response.body()!!.code.toString())
                            supplierList = response.body()!!.dataObject as ArrayList<GetBillNoGroup.DataObject>

                            if (supplierList!!.size != 0) {
                                var KeyValue = ArrayList<KeyValue>()
                                supplierList!!.forEach {
                                    AddItemFragment.data = KeyValue(it.accountCode, it.accountNameEn)
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
        val selectedAdapter = SalesSelectedListAdapter(mContext, this)
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
        TotalAmount = "%.3f".format(TotalAmount).toDouble()
        //    TotalAmount =        BigDecimal(activity).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

        et_totalamount.setText(TotalAmount.toString())
    }

    override fun onResume() {
        mHom.setDrawerState(true)
        mHom!!.setTitle(getString(R.string.sales_order))
        try {
            if (m_Device!!.isDeviceOpen())
                m_Device!!.openDevice()
        } catch (ex: Exception) {

        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            if (m_Device!!.isDeviceOpen()!!)
                m_Device!!.closeDevice()
        } catch (ex: Exception) {

        }
    }

    override fun CallbackMethod(epsonComCallbackInfo: EpsonComCallbackInfo): EpsonCom.ERROR_CODE? {
        /*
        EpsonCom.ERROR_CODE retval = EpsonCom.ERROR_CODE.SUCCESS;

        try
        {
            switch(cbInfo.ReceivedDataType)
            {
                case GENERAL:
                    //do nothing, ignore any general incoming data
                    break;
                case ASB:	//new ASB data came in
                    receiveAndShowASBData();
                    break;
                case MICR:	//new MICR data came in
                    receiveAndShowMICRData();
                    break;
                case IMAGE:	//new image data came in
                    receiveAndShowImageData();

                    //kill progress dialog
                    if(m_progressDialog!=null)
                    {
                        m_progressDialog.dismiss();
                    }
            }
        }
        catch(Exception e)
        {
            messageBox(this, "callback method threw exception: " + e.toString() + " - " + e.getMessage(), "Callback Error");
        }

        return retval;*/
        return null
    }


/*
    private fun connectAndPrint() {
        if (m_Device!!.isDeviceOpen() == false) {
            m_Device!!.openDevice()
        }

        var err: EpsonCom.ERROR_CODE = EpsonCom.ERROR_CODE.SUCCESS

        var font: EpsonCom.FONT = EpsonCom.FONT.FONT_A
        var bold: Boolean? = false
        var underlined: Boolean? = false
        var doubleHeight: Boolean? = false
        var doubleWidth: Boolean? = false

        try {

            font = EpsonCom.FONT.FONT_A
            //                    font = EpsonCom.FONT.FONT_B;
            bold = true
            //                bold = false;
            //                underlined = true;
            underlined = false
            //set double print
            when (1) {
                1 -> {
                    doubleHeight = false
                    doubleWidth = false
                }
                2 -> {
                    doubleHeight = false
                    doubleWidth = true
                }
                3 -> {
                    doubleHeight = true
                    doubleWidth = false
                }
                4 -> {
                    doubleHeight = true
                    doubleWidth = true
                }
            }
            if (m_Device!!.isDeviceOpen() == true) {
                //comment later
                //   if (true) {
                //set print alignment
                when (2) {
                    1 -> {
                        err = m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            val errorString = EpsonCom.getErrorText(err)
                            messageBox(errorString, "printString Error", null)
                        }
                    }
                    2 -> {
                        err = m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            val errorString = EpsonCom.getErrorText(err)
                            messageBox(errorString, "printString Error", null)
                        }
                    }
                    3 -> {
                        err = m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.RIGHT)
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            val errorString = EpsonCom.getErrorText(err)
                            messageBox(errorString, "printString Error", null)
                        }
                    }
                }
                //comment later
                //    err = EpsonCom.ERROR_CODE.SUCCESS;
                if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                    //print string
                    //   m_Device.printString(sendString, font, bold, underlined, doubleHeight, doubleWidth);
                    //    printTest();
                    printFormat()
                    //m_Device.sendData()
                    if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                        val errorString = EpsonCom.getErrorText(err)
                        messageBox(errorString, "printString Error", null)
                    }
                }
                //    Helper.hideProgress(mContext);
            } else {
                //       Helper.hideProgress(mContext);
                messageBox("Device is not open", "printString Error", null)
            }
        } catch (e: Exception) {
            //   Helper.hideProgress(mContext);
            messageBox("Exception: " + e.toString() + " - " + e.message, "printString Error", null)
        }

    }
*/


    private fun  printFormat() {
        val format = DecimalFormat("###.000")

        var serviceName = ""
        var Qty = ""
        var price = ""
        var amount = ""// m_Device.printPage()
        var star = ""
        var s = ""
        try {
            /*    s = String("\u0628\u06A9".toByteArray(), "UTF-8")
                star = String("تعطي يونيكود رقما فريدا لكل حرف".toByteArray(), "UTF-8")
                star = String("تعطي يونيكود رقما فريدا لكل حرف".toByteArray(charset("ISO-8859-1")))*/
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        // star = String.format("%040x", new BigInteger(1, star.getBytes(Charset.forName("UTF-8"))));
        m_Device!!.printString(star, EpsonCom.FONT.FONT_A, true, true, true, true)
        m_Device!!.printString(star, EpsonCom.FONT.FONT_A, true, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString(" Al-FAKHAMA TRAD & CONT. L.L.C", EpsonCom.FONT.FONT_A, true,
                false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
      //  m_Device!!.printString(" Tel 24505941 Faz 24505295", EpsonCom.FONT.FONT_A, false,             false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString(" Branch: " , EpsonCom.FONT.FONT_A, false, false,
                false, false)


        //  m_Device.printString("Store Location  : Termad Branch", EpsonCom.FONT.FONT_A, false, false, false, false);
        //  m_Device.printString("Store Contact no:000000" + billingModel.getContactNumber(), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device!!.printString("Bill no   :" + et_billno.text.toString(), EpsonCom.FONT.FONT_A,
                false, false, false, false)
        m_Device!!.printString("Bill Date :" + et_billdate.text, EpsonCom.FONT
                .FONT_A, false, false, false, false)
        //   m_Device.printString(" كمية  |  السعر |كمية  |   وصف                ", EpsonCom.FONT.FONT_A, true, false, true, false);

        m_Device!!.printString("Description           | qty  | price  | Amount", EpsonCom.FONT
                .FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", EpsonCom.FONT
                .FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
        for (i in SalesFragment.selectedProductList) {

            serviceName = i.getItemNameEn()
            Qty = (i.customerQty.toString())
            price =(i.salesPrice).toString()
            amount = (i.customerQty * i.salesPrice.toDouble()).toString()
            //30 max
            val length = serviceName.length
            if (serviceName.length > 22) {
                serviceName = serviceName.substring(0, 22) + ".."
            } else {
                serviceName = rightPadding(serviceName, 22)
            }
            if (Qty.length > 6) {
                Qty = serviceName.substring(0, 6)
            } else {
                Qty = rightPadding(Qty, 6)
            }
            if (price.length > 8) {
                price = serviceName.substring(0, 8)
            } else {
                price = rightPadding(price, 8)
            }
            m_Device!!.printString("$serviceName $Qty $price $amount", EpsonCom.FONT.FONT_A, false, false, false, false)
            //   m_Device!!.printString("اسم الخدمة هناn", EpsonCom.FONT.FONT_A, false, false, false, false);
        }


        //   m_Device!!.
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
        m_Device!!.printString("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", EpsonCom.FONT.FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString("Payable Amount OMR:" + et_balanceamount.text.toString(), EpsonCom
                .FONT.FONT_B, true, true, true, true)
        m_Device!!.printString("_______________________________________________", EpsonCom.FONT.FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
        m_Device!!.printString("Bill Amount      OMR :" + et_totalamount.text, EpsonCom.FONT
                .FONT_A, false,
                false, false, false)
        //     m_Device!!.printString("Discount Amount  OMR :" + format.format(billingModel.getLessDiscount()), EpsonCom.FONT.FONT_A, false, true, false, false);
        //     m_Device!!.printString("Payable Amount   OMR :" + format.format(billingModel.getNetamount()), EpsonCom.FONT.FONT_A, false, true, false, false);
        m_Device!!.printString("Cash received    OMR :" + et_amountrecived.text, EpsonCom.FONT
                .FONT_A, false,
                false, false, false)
        m_Device!!.printString("Change Amount    OMR :" + et_balanceamount.text, EpsonCom.FONT
                .FONT_A, false,
                false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.RIGHT)
        //  m_Device!!.printString("(Discount   point  :" + billingModel.getLesspoint() + ")", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString("_______________________________________________", EpsonCom.FONT.FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.LEFT)
        m_Device!!.printString("This station is not responsible for damages that occur because of negligence or fault of the consumer ", EpsonCom.FONT.FONT_A, false, false, false, false)
        m_Device!!.selectAlignment(EpsonCom.ALIGNMENT.CENTER)
        m_Device!!.printString("Thank you & Visit Again", EpsonCom.FONT.FONT_A, false, false, false, false)
        m_Device!!.cutPaper()

        mHom.onBackPressed()
    }

    fun messageBox(message: String, title: String, type: String?) {
        /*  mContext.runOnUiThread(
                new Runnable()
                {
                    public void run()
                    {*/
        val alertDialog = AlertDialog.Builder(mContext).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton("OK") { dialog, which -> alertDialog.cancel() }
        if (type != null && type == "retry") {
            alertDialog.setButton("retry") { dialog, which ->
                //        val a = Html.fromHtml(recipet).toString()
                //String sendString =a;
                connectAndPrint()
            }
        }
        alertDialog.show()
        /*      }
                }
        );*/

    }

    private fun connectToPrinter() {
        var err: EpsonCom.ERROR_CODE = EpsonCom.ERROR_CODE.SUCCESS

        //fill in some parameters
        m_DeviceParameters!!.PortType = EpsonCom.PORT_TYPE.ETHERNET
        m_DeviceParameters!!.IPAddress = "192.168.0.88"
        m_DeviceParameters!!.PortNumber = 9100

        if (err == EpsonCom.ERROR_CODE.SUCCESS) {
            //set the parameters to the device
            err = m_Device!!.setDeviceParameters(m_DeviceParameters)
            if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                val errorString = EpsonCom.getErrorText(err)
                messageBox(errorString, "SampleCode: setDeviceParameters Error", null)
            }
            if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                //open the device
                err = m_Device!!.openDevice()
                //comment later
                //   err = EpsonCom.ERROR_CODE.SUCCESS;
                if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                    val errorString = EpsonCom.getErrorText(err)
                    Log.d("SampleCode", "Error from openDevice: $errorString")
                    //    messageBox(errorString, "SampleCode 0: openDevice Error", null);
                    // mHom.onBackPressed();
                    showPrintFailureDialog()
                } else {
                    //     val content = Html.fromHtml(recipet)
                    //    ProgressUtils.startProgress(mContext,"print config", " Connecting to printer...",false);
                    //connectAndPrint()
                    printFormat()
                }
            }

            if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                //activate ASB sending
                err = m_Device!!.activateASB(true, true, true, true, true, true)
                if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                    val errorString = EpsonCom.getErrorText(err)
                    Log.d("SampleCode", "Error from activateASB: $errorString")
                    messageBox(errorString, "SampleCode 1: openDevice Error", null)
                }
            }
        }
    }

    private fun showPrintFailureDialog() {
        val onClickListener = DialogInterface.OnClickListener { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                connectToPrinter()
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
           mHom.showSalesFragment();
            }
        }

        val alertBuilder = AlertDialog.Builder(mContext)
        alertBuilder.setTitle("Printer not connected")
        alertBuilder.setMessage("The KOT is not printed. Do you want to retry?")
        alertBuilder.setPositiveButton("Try Again in 5 sec", onClickListener)
        alertBuilder.setNegativeButton("Go back", onClickListener)
        alertBuilder.setCancelable(false)
        val alert = alertBuilder.create()
        alert.show()
    }

    fun rightPadding(str: String, num: Int): String {
        return String.format("%1$-" + num + "s", str)
    }

}