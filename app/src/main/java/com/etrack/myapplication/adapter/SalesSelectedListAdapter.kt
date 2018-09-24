package com.etrack.myapplication.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.etrack.myapplication.R
import com.etrack.myapplication.`interface`.UpdateList
import com.etrack.myapplication.fragment.SalesFragment
import com.etrack.myapplication.model.outputService.GetItemListSuccessFailure
import kotlinx.android.synthetic.main.selected_product_item.view.*

/**
 * Created by Hussain on 23-03-2018.
 */

class SalesSelectedListAdapter(private val mContext: Context, private val refreshInterface: UpdateList) : RecyclerView.Adapter<SalesSelectedListAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private var product: GetItemListSuccessFailure.GetItemListDataObject? = null
    private var totalAmount: Double = 0.000
    var totalCost: Double = 0.00
    private var count: Int = 1
    private lateinit var refreshInterfaces: UpdateList
    override fun getItemCount(): Int {
        return SalesFragment.selectedProductList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductNameEn: TextView
        var tvProductNameAr: TextView
        var tvGroupType: TextView
        var tvProductPrice: TextView
        var cbSelectProduct: CheckBox
        var btDecreaseQuantity: Button
        var btIncreaseQuantity: Button
        var tvProductCount: TextView
        var tvProductTotalPrice: TextView
        var et_Price: EditText
        var et_qty: EditText


        init {
            refreshInterfaces = refreshInterface
            tvProductNameEn = view.tvProductNameEn
            tvProductNameAr = view.tvProductNameAr
            et_Price = view.et_Price
            tvGroupType = view.tvGroupType
            cbSelectProduct = view.cbSelectProduct
            btDecreaseQuantity = view.btDecreaseQuantity
            btIncreaseQuantity = view.btIncreaseQuantity
            tvProductPrice = view.tvProductPrice
            tvProductCount = view.tvProductCount
            tvProductTotalPrice = view.tvProductTotalPrice
            et_Price = view.et_Price
            et_qty = view.et_Qty
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.selected_product_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.et_Price.visibility =View.GONE
        product = SalesFragment.selectedProductList[position]
        if (product!!.customerQty == null)
            product!!.customerQty = 1
        count = product!!.customerQty
        holder.tvGroupType.setText(product!!.categoryNameEn)
        holder.tvProductNameEn.setText(product!!.itemNameEn)
        holder.tvProductNameAr.setText(product!!.itemNameAr)
        holder.tvProductPrice.setText(product!!.unitPrice)
        holder.et_Price.setText(product!!.unitPrice)
        holder.et_qty.setText(Integer.toString(count))
        holder.cbSelectProduct.isChecked = true
        totalAmount = add(totalAmount, product!!.unitPrice)!!
        holder.tvProductCount.setText(Integer.toString(count) + " X ")
        totalCost = (count * (product!!.unitPrice).toDouble())
        holder.tvProductTotalPrice.setText(" = " + totalCost)
        holder.btDecreaseQuantity.setOnClickListener(View.OnClickListener {
            product = SalesFragment.selectedProductList[position]
            count = product!!.customerQty
            if (count > 1) {
                count = count - 1
                holder.tvProductCount.setText(Integer.toString(count))
                totalCost = (count * (product!!.unitPrice).toDouble())
                holder.tvProductCount.setText(Integer.toString(count) + " X " )
                holder.tvProductTotalPrice.setText(" = " + totalCost + "")
                holder.et_Price.setText(product!!.unitPrice)
                holder.et_qty.setText(Integer.toString(count))
                product!!.customerQty = count
                refreshInterface.updateTotalCost()
            }
        })
        holder.btIncreaseQuantity.setOnClickListener(View.OnClickListener {
            product = SalesFragment.selectedProductList[position]
         if((product!!.availableQuantity) .toInt() >  product!!.customerQty ){
         count = product!!.customerQty
            count = count + 1
            val totalCost = count * (product!!.unitPrice).toDouble()
            holder.tvProductCount.setText(Integer.toString(count) + " X ")
            holder.tvProductTotalPrice.setText(" = " + totalCost + "")
            holder.et_Price.setText(product!!.unitPrice)
            holder.et_qty.setText(Integer.toString(count))
            product!!.customerQty = count
            var totalAmount = 0.000
            /*  PurchaseFragment.selectedProductList!!.forEach {
                  //   if (it.id.equals(productId)) {
                  *//*           if (it.customerQty == null)
                               it.customerQty = 0*//*
                  totalAmount += (it.customerQty * it.unitPrice.toInt())
                //     }
            }*/
            // PurchaseFragment.selectedProductList.add(product!!)
            refreshInterface.updateTotalCost() }
            else
             Toast.makeText(mContext,mContext.getString(R.string.qty_not_available),Toast
                     .LENGTH_SHORT).show()
        })
        holder.et_Price.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                product = SalesFragment.selectedProductList[position]
                var cost = 0.00
                val handler = Handler()
                handler.postDelayed({
                    if (holder.et_Price!!.text.toString().equals("")) {
                        cost = 0.00
                    } else {
                        cost = (holder.et_Price!!.text.toString()).toDouble()
                    }


                    product!!.unitPrice = cost.toString()
                    //       holder.tvPrice.setText(product!!.unitPrice)
                    // totalCost
                    totalCost = (product!!.customerQty * (product!!.unitPrice).toDouble())
                    holder.tvProductPrice.setText(cost.toString())
                    holder.tvProductCount.setText(Integer.toString(count) + " X ")
                    holder.tvProductTotalPrice.setText(" = " + totalCost + "")
                    refreshInterface.updateTotalCost()
                }, 0)
            }
        });
        holder.et_qty.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                product = SalesFragment.selectedProductList[position]

                var qty = 0
                val handler = Handler()
                handler.postDelayed({
                    if (holder.et_qty!!.text.toString().equals("")) {
                        qty = 1
                    } else {
                        qty = Integer.parseInt(holder.et_qty!!.text.toString())
                    }
                    count = qty
                    holder.et_Price.setText(product!!.unitPrice)
                    product!!.customerQty = count
                    totalCost = (count * (product!!.unitPrice).toDouble())
                    holder.tvProductCount.setText(Integer.toString(count) + " X ")
                    holder.tvProductTotalPrice.setText(" = " + totalCost + "")
                    refreshInterface.updateTotalCost()
                }, 0)

            if((product!!.availableQuantity) .toInt() >  product!!.customerQty ){

            }
                else {
                    Toast.makeText(mContext, mContext.getString(R.string.qty_not_available), Toast
                            .LENGTH_SHORT).show()
                }
            }
        });
        refreshInterface.updateTotalCost()
        /*    holder.cbSelectProduct.setOnClickListener { v ->

                if (PurchaseFragment.selectedProductList.size != 0) {
                    val productId = PurchaseFragment.selectedProductList[position].id

                    PurchaseFragment.selectedProductList.forEach {
                        if (it.id.equals(productId)) {
                            PurchaseFragment.selectedProductList!!.remove(it)
                        }

                    }
                }
            }*/
    }

    fun add(a: Double, b: String): Double? {
        //val a = String.format("%.2f", a)

        val result = (a + b.toDouble())
        return result
    }
}

