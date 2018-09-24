package com.etrack.myapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.ProductAdapterInterface
import com.etrack.myapplication.`interface`.UpdateList
import com.etrack.myapplication.adapter.ProductSelectAdapter
import com.etrack.myapplication.controller.MainActivity
import kotlinx.android.synthetic.main.fragment_select_product_list.view.*
import java.util.*

@SuppressLint("ValidFragment")
class ProductListFragment(mContext: Context, refreshInterface: UpdateList) : DialogFragment() {

    private var mProductSelectListAdapter: ProductSelectAdapter? = null
    private var rvProductList: RecyclerView? = null
    private var mContext: Context? = null
    private lateinit var btnAddProducts: Button
    private var hom: MainActivity? = null
    private var productAdapterInterface: ProductAdapterInterface? = null
    private var keyValueProductId: HashMap<Int, Int>? = null
    private lateinit var refreshInterface: UpdateList

    init {
//        this.productList = productList
        this.refreshInterface = refreshInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater!!.inflate(R.layout.fragment_select_product_list, container, false)
        rvProductList = rootView.rvProductList
        hom = getActivity() as MainActivity
        mContext = getActivity()
        btnAddProducts = rootView.btnAddProducts
        btnAddProducts.setOnClickListener(View.OnClickListener {
            refreshInterface.updateList()
            this.dismiss()
        })
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (rvProductList != null) {
            mProductSelectListAdapter = ProductSelectAdapter(mContext!!)
            rvProductList!!.adapter = mProductSelectListAdapter
            val mLayoutManager = LinearLayoutManager(mContext)
            rvProductList!!.layoutManager = mLayoutManager
            rvProductList!!.itemAnimator = DefaultItemAnimator()
            rvProductList!!.adapter = mProductSelectListAdapter


        }

/*
        btnAddProducts!!.setOnClickListener {
            val args = Bundle()
            args.putIntegerArrayList("productlist", mProductSelectListAdapter!!.selectedProduct())
            args.putString("type", getString(R.string.seleted_private_product))
            //   hom.ShowCreatePrivateOrder(getString(R.string.create_private_package),args );

            productAdapterInterface.selectedItem(mProductSelectListAdapter!!.getSelectedProduct())
            dismiss()
        }*/
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }


}
