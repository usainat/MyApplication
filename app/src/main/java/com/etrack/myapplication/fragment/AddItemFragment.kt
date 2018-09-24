package com.etrack.myapplication.fragment


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
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
import com.etrack.myapplication.model.inputService.GetCatogorey
import com.etrack.myapplication.model.inputService.KeyValue
import com.etrack.myapplication.model.inputService.SaveItem
import com.etrack.myapplication.model.outputService.GetCatogoreySuccessFailure
import com.etrack.myapplication.model.outputService.SuccessFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AddItemFragment : Fragment() {
    private var barCode: EditText? = null
    private var itemName: EditText? = null
    private var itemArabName: EditText? = null
    private var openningQty: EditText? = null
    private var costPrice: EditText? = null
    private var netPrice: EditText? = null
    private var discount: EditText? = null
    private var salePrice: EditText? = null
    private var addButton: Button? = null
    private var spGroup: Spinner? = null
    private lateinit var mContext: Context
    private lateinit var mHom: MainActivity
    private var catCode: String? = null
    private var catList: List<GetCatogoreySuccessFailure.GetCatogoreyDataObject>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_create_item, container, false)
        barCode = root?.findViewById<EditText>(R.id.et_itembarcode) as EditText
        itemName = root?.findViewById<EditText>(R.id.et_itemname) as EditText
        itemArabName = root?.findViewById<EditText>(R.id.et_itemname_arabic) as EditText
        openningQty = root?.findViewById<EditText>(R.id.et_openning_qty) as EditText
        costPrice = root?.findViewById<EditText>(R.id.et_cost_price) as EditText
        salePrice = root?.findViewById<EditText>(R.id.et_sale_price) as EditText
        discount = root?.findViewById<EditText>(R.id.et_discount) as EditText
        netPrice = root?.findViewById<EditText>(R.id.et_net_price) as EditText
        spGroup = root?.findViewById<Spinner>(R.id.spGroup) as Spinner
        addButton = root?.findViewById<Button>(R.id.btn_add_item) as Button
        mContext = getActivity()

        val catogorey = GetCatogorey()
        val catogoreyObj = catogorey.CatogoreyObjcat();
        catogoreyObj.categoryCode = ""
        catogoreyObj.companyId = CommonUtils.getCompanyId(mContext)
        catogorey.objcat = catogoreyObj
        getGroupDetails(catogorey);
        spGroup!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                catCode = (parent.selectedItem as KeyValue).key
                catList!!.forEach {
                    if (it.categoryCode.equals(catCode)) {
                        discount!!.setText(it.discount.toString())
                    }
                }

                //    Toast.makeText(context, "Country ID: " + country.getId() + ",  Country Name : " + country.getName(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        addButton!!.setOnClickListener { v ->
            if (barCode!!.text.toString().trim().length > 0 &&
                    itemName!!.text.toString().trim().length > 0 &&
                    itemArabName!!.text.toString().trim().length > 0 &&
                    openningQty!!.text.toString().trim().length > 0 &&
                    costPrice!!.text.toString().trim().length > 0 &&
                    salePrice!!.text.toString().trim().length > 0 &&
                    discount!!.text.toString().trim().length > 0 &&
                    netPrice!!.text.toString().trim().length > 0) {
                var saveItem = SaveItem()
                var savobjectItem = saveItem.SaveObjitem()
                savobjectItem.itemBarcode = barCode!!.text.toString().trim()
                savobjectItem.itemNameEn = itemName!!.text.toString().trim()
                savobjectItem.itemNameAr = itemArabName!!.text.toString().trim()
                savobjectItem.categoryCode = catCode
                savobjectItem.openingQuantity = openningQty!!.text.toString().trim()
                savobjectItem.costPrice = costPrice!!.text.toString().trim()
                savobjectItem.salesPrice = salePrice!!.text.toString().trim()
                savobjectItem.discount = discount!!.text.toString().trim()
                savobjectItem.unitPrice = netPrice!!.text.toString().trim()
                savobjectItem.companyId = CommonUtils.getCompanyId(mContext)
                savobjectItem.itemCode = ""
                saveItem.objitem = savobjectItem
                callAddItemApi(saveItem)
            } else {
                Toast.makeText(mContext, getString(R.string.please_enter_all_field), Toast.LENGTH_LONG).show()
            }
        }
        salePrice!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!s.trim().toString().equals("") && !s.trim().toString().equals("0") &&
                       ! discount!!.text.toString().equals("")) {
                    var netPriceAmount = (s.toString().toDouble())
                            .minus((s.toString().toDouble().
                                    times((discount!!.text
                                            .toString().toDouble()) / 100))).toString()


                    netPrice!!.setText(netPriceAmount)
                }
            }
        });
        return root
    }


    private fun getGroupDetails(catogorey: GetCatogorey) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.getting_catogorey), getString(R.string.please_wait), false)
            //            val gson = Gson()
            //       val personString = gson.toJson(register).toString()
            val getAllCatogoreyList = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val getCatogoreyListCall = getAllCatogoreyList!!.getAllCatogoreyList(catogorey)
            getCatogoreyListCall.enqueue(object : Callback<GetCatogoreySuccessFailure> {
                override fun onResponse(call: Call<GetCatogoreySuccessFailure>, response: Response<GetCatogoreySuccessFailure>) {
                    if (response.body() != null) {
                        catList = response.body()!!.dataObject

                        if (catList!!.size != 0) {
                            var KeyValue = ArrayList<KeyValue>()
                            catList!!.forEach {
                                data = com.etrack.myapplication.model.inputService.KeyValue(it.categoryCode, it.categoryNameEn)
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
                        Log.e(TAG, "Citizen Register Failure")
                    }

                }

                override fun onFailure(call: Call<GetCatogoreySuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(TAG, "Citizen Register Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(TAG, "Citizen Register service Failure" + e.toString())
        }

    }

    private fun callAddItemApi(saveItem: SaveItem) {
        try {
            ProgressUtils.startProgress(mContext, getString(R.string.adding_item), getString(R.string.please_wait), false)
            //remove later
            //     val customer = AddCatogorey()
            //            val gson = Gson()
            //       val personString = gson.toJson(register).toString()
            val saveItemApi = ApiClient().BaseUrl()?.create(ApiInterface::class.java)
            val saveItemCall = saveItemApi!!.saveItemApi(saveItem)
            saveItemCall.enqueue(object : Callback<SuccessFailure> {
                override fun onResponse(call: Call<SuccessFailure>, response: Response<SuccessFailure>) {
                    if (response.body() != null) {
                        if (response.body()!!.message.equals("true")) {
                            ProgressUtils.stopProgress()
                            clearfield()
                            Log.i(TAG, "Item added successfully" + response.body()!!.toString())
                        } else if(response.body()!!.message.equals("Already Exist")){
                            CommonUtils.makeText(mContext, getString(R.string.name_already_exist), Toast
                                    .LENGTH_SHORT)
                            ProgressUtils.stopProgress()
                        }
                        else {
                            CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                            ProgressUtils.stopProgress()
                        }
                    } else {
                        ProgressUtils.stopProgress()
                        CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                        // retryAgain();
                        Log.e(TAG, "Item add Failured")
                    }

                }

                override fun onFailure(call: Call<SuccessFailure>, t: Throwable) {
                    ProgressUtils.stopProgress()
                    //  retryAgain();
                    CommonUtils.makeText(mContext, getString(R.string.sonthing_went_wrong), Toast.LENGTH_SHORT)
                    Log.e(TAG, "Citizen Register Failure" + t.toString())
                }
            })


        } catch (e: Exception) {
            ProgressUtils.stopProgress()
            e.printStackTrace()
            Log.e(TAG, "Citizen Register service Failure" + e.toString())
        }

    }

    private fun clearfield() {
        barCode!!.setText("")
        itemName!!.setText("")
        itemArabName!!.setText("")
        openningQty!!.setText("")
        costPrice!!.setText("")
        salePrice!!.setText("")
        discount!!.setText("")
        netPrice!!.setText("")
    }

    fun setText() {
        barCode!!.setText("asdfsa")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHom = activity as MainActivity
    }

    companion object {
        var data: KeyValue? = null
    }
    override fun onResume() {
        mHom!!.setTitle(getString(R.string.add_product))
        super.onResume()
    }
}// Required empty public constructor


