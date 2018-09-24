package com.etrack.myapplication.fragmentimport android.annotation.SuppressLintimport android.content.ContentValuesimport android.content.Contextimport android.content.Intentimport android.os.Bundleimport android.os.Handlerimport android.support.v4.app.DialogFragmentimport android.support.v7.widget.DefaultItemAnimatorimport android.support.v7.widget.LinearLayoutManagerimport android.support.v7.widget.RecyclerViewimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.widget.Buttonimport android.widget.EditTextimport android.widget.Toastimport com.etrack.myapplication.Rimport com.etrack.myapplication.`interface`.ApiInterfaceimport com.etrack.myapplication.`interface`.ProductAdapterInterfaceimport com.etrack.myapplication.`interface`.UpdateListimport com.etrack.myapplication.adapter.SalesScannedAdapterimport com.etrack.myapplication.commonUtils.CommonUtilsimport com.etrack.myapplication.commonUtils.FrgmentNameimport com.etrack.myapplication.commonUtils.ProgressUtilsimport com.etrack.myapplication.controller.ApiClientimport com.etrack.myapplication.controller.MainActivityimport com.etrack.myapplication.fragment.SalesFragment.Companion.selectedProductListimport com.etrack.myapplication.model.inputService.GetItemimport com.etrack.myapplication.model.outputService.GetItemListSuccessFailureimport kotlinx.android.synthetic.main.fragment_scanned_product.view.*import retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseimport java.util.*@SuppressLint("ValidFragment")class ScanedSalesFragment(mContext: Context, refreshInterface: UpdateList) :        DialogFragment()        , UpdateQr {    override fun updateQrValue(qrCode: String) {        et_scanItem.setText(qrCode)        if (et_scanItem.text.toString().trim().length > 0) {            var getItem = GetItem()            var objectItem = getItem.Objitem()            objectItem.companyId = CommonUtils.getCompanyId(mContext)            objectItem.itemCode = et_scanItem.text.toString()            getItem.objitem = objectItem            getScannedItem(getItem)        } else {            Toast.makeText(mContext, getString(R.string.plz_enter_item_code), Toast.LENGTH_SHORT)                    .show()        }    }    private var mProductSelectListAdapter: SalesScannedAdapter? = null    private var rvProductList: RecyclerView? = null    private var mContext: Context? = null    private lateinit var btnScanProducts: Button    private lateinit var btnaddProducts: Button    private lateinit var btnClose: Button    private lateinit var et_scanItem: EditText    private var hom: MainActivity? = null    private var productAdapterInterface: ProductAdapterInterface? = null    private var keyValueProductId: HashMap<Int, Int>? = null    private var refreshInterface: UpdateList    private val handler = Handler()    private val timer = Timer()    private val DELAY: Long = 2000    init {//        this.productList = productList        this.refreshInterface = refreshInterface    }    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        //   CommonValue()qr    }    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,                              savedInstanceState: Bundle?): View? {        // Inflate the layout for this fragment        var rootView = inflater!!.inflate(R.layout.fragment_scanned_product, container, false)        rvProductList = rootView.rvProductList        hom = getActivity() as MainActivity        mContext = getActivity()        btnScanProducts = rootView.btnScanProducts        et_scanItem = rootView.et_scanItem        btnaddProducts = rootView.btnaddProducts        btnClose = rootView.btnClose        btnaddProducts.setOnClickListener {            if (et_scanItem.text.toString().trim().length > 0) {                var getItem = GetItem()                var objectItem = getItem.Objitem()                objectItem.companyId = CommonUtils.getCompanyId(mContext)                objectItem.itemCode = et_scanItem.text.toString()                getItem.objitem = objectItem                getScannedItem(getItem)            } else {                Toast.makeText(mContext, getString(R.string.plz_enter_item_code), Toast.LENGTH_SHORT).show()            }        }        btnScanProducts.setOnClickListener(View.OnClickListener {            //    refreshInterface.updateList()            val simpleScannerFragment = QRScannerFragment(this)            var fm = hom!!.getSupportFragmentManager()            simpleScannerFragment.show(fm, FrgmentName.SIMPLE_SCANNER_FRAGMENT)            // this.dismiss()        })        btnClose.setOnClickListener(View.OnClickListener {            this.dismiss()        })        return rootView    }    override fun onActivityCreated(savedInstanceState: Bundle?) {        super.onActivityCreated(savedInstanceState)        if (rvProductList != null) {            val mLayoutManager = LinearLayoutManager(mContext)            rvProductList!!.layoutManager = mLayoutManager            rvProductList!!.itemAnimator = DefaultItemAnimator()            //        rvProductList!!.adapter = mProductSelectListAdapter        }/*        btnAddProducts!!.setOnClickListener {            val args = Bundle()            args.putIntegerArrayList("productlist", mProductSelectListAdapter!!.selectedProduct())            args.putString("type", getString(R.string.seleted_private_product))            //   hom.ShowCreatePrivateOrder(getString(R.string.create_private_package),args );            productAdapterInterface.selectedItem(mProductSelectListAdapter!!.getSelectedProduct())            dismiss()        }*/    }    override fun onResume() {        super.onResume()        /* if (CommonValue.qrCode != "") {             et_scanItem.setText(CommonValue.qrCode)         }*/        //   val a = CommonValue.Companion        //val a =  CommonVa    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        super.onActivityResult(requestCode, resultCode, data)    }    private fun getScannedItem(item: GetItem) {        try {            //        ProgressUtils.startProgress(mContext, getString(R.string.getting_product_by_id), getString(R.string.please_wait), false)            val productsApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)            val productsCall = productsApi!!.getAllProductsList(item)            productsCall.enqueue(object : Callback<GetItemListSuccessFailure> {                override fun onResponse(call: Call<GetItemListSuccessFailure>, response: Response<GetItemListSuccessFailure>) {                    if (response.body() != null) {                        var isavailable: Boolean = true                        if (response.body()!!.message.equals("true") && response.body()!!                                        .dataObject.size != 0) {                            if (selectedProductList.size != 0) {                                selectedProductList.forEach {                                    if (!it.itemCode.equals((response.body()!!.dataObject as                                                    ArrayList<GetItemListSuccessFailure                                                    .GetItemListDataObject>)[0].itemCode)) {                                        isavailable = false                                    }                                }                            } else {                                isavailable = false                            }                            if (!isavailable) {                                var ItemObject = (response.body()!!.dataObject as                                        ArrayList<GetItemListSuccessFailure.GetItemListDataObject>)[0]                                ItemObject.customerQty = 1                                SalesFragment.selectedProductList.add(ItemObject)                                mProductSelectListAdapter = SalesScannedAdapter(mContext!!, (response.body()!!.dataObject as                                        ArrayList<GetItemListSuccessFailure.GetItemListDataObject>)[0])                                rvProductList!!.adapter = mProductSelectListAdapter                                mProductSelectListAdapter!!.notifyDataSetChanged()                                refreshInterface.updateList()                          et_scanItem.setText("")                            }                        } else {                            Toast.makeText(mContext, getString(R.string.no_item_available), Toast.LENGTH_SHORT).show()                        }                        ProgressUtils.stopProgress()                    } else {                        ProgressUtils.stopProgress()                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)                        // retryAgain();                        Log.e(ContentValues.TAG, "get product by itemcode  Failure")                    }                }                override fun onFailure(call: Call<GetItemListSuccessFailure>, t: Throwable) {                    ProgressUtils.stopProgress()                    //  retryAgain();                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)                    Log.e(ContentValues.TAG, "get product by itemcode Failure" + t.toString())                }            })        } catch (ex: Exception) {            ProgressUtils.stopProgress()            Log.e(ContentValues.TAG, "get product by itemcode  catch Failure" + ex.toString())        }    }    override fun onAttach(context: Context?) {        super.onAttach(context)    }    override fun onDetach() {        super.onDetach()    }}