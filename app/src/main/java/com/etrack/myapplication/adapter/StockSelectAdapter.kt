package com.etrack.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.fragment.StockFragment
import com.etrack.myapplication.fragment.StockFragment.Companion.selectedProductList
import com.etrack.myapplication.model.outputService.GetItemListSuccessFailure
import kotlinx.android.synthetic.main.select_product_item.view.*

/**
 * Created by Hussain on 14-03-2018.
 */


class StockSelectAdapter  (private val mContext: Context) : RecyclerView.Adapter<StockSelectAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private var product: GetItemListSuccessFailure.GetItemListDataObject? = null

    override fun getItemCount(): Int {
        return StockFragment.productList.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductNameEn: TextView
        var tvProductNameAr: TextView
        var tvGroupType: TextView
        var tvProductPrice: TextView
        var cbSelectProduct: CheckBox

        init {

            tvProductNameEn = view.tvProductNameEn
            tvProductNameAr = view.tvProductNameAr
            tvProductPrice = view.tvPrice
            tvGroupType = view.tvGroupType
            cbSelectProduct = view.cbSelectProduct
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.select_product_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        product = StockFragment.productList[position]
        holder.tvGroupType.setText(product!!.categoryNameEn)
        holder.tvProductNameEn.setText(product!!.itemNameEn)
        holder.tvProductNameAr.setText(product!!.itemNameAr)
        holder.tvProductPrice.setText(product!!.costPrice)
        if (StockFragment.selectedProductList.size != 0){
            StockFragment.selectedProductList!!.forEach {
                //  if (it.id.equals(productId)) {
                if (it.id == product!!.id){
                    holder.cbSelectProduct.isChecked = true
                }
            }
        }
        /*     if (keyValueProductId.size != 0) {
                 val currentProductId = productItemList[position].id
                 if (keyValueProductId.containsKey(currentProductId)) {
                     holder.cbSelectProduct.isChecked = true
                 } else {
                     holder.cbSelectProduct.isChecked = false
                 }
             }
             if (selectedProduct.size != 0) {
                 val currentProductId = productItemList[position].id
                 if (selectedProduct.contains(currentProductId)) {
                     holder.cbSelectProduct.isChecked = true
                 } else {
                     holder.cbSelectProduct.isChecked = false
                 }
             }*/
            holder.cbSelectProduct.setOnClickListener { v ->
                if ((v as CheckBox).isChecked) {

                    StockFragment.selectedProductList.add(StockFragment
                            .productList[position])
                    selectedProductList.get( selectedProductList.size-1).customerQty = 1
                } else {
                    if (StockFragment.selectedProductList.size != 0) {
                        val productId = StockFragment.productList[position].id
                        var i=0
                        for ( GetItemListDataObject in  StockFragment.selectedProductList) {
                            if (GetItemListDataObject.id.equals(productId)) {
                                StockFragment.selectedProductList!!.removeAt(i)
                                break
                            }
                            i++
                        }/*
                        PurchaseFragment.selectedProductList.forEach {
                            if (it.id.equals(productId)) {
                                PurchaseFragment.selectedProductList!!.remove(it)
                            }
                        }*/
                    }
                }
            }
    }
}