package com.etrack.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.model.outputService.GetReportItemCat
import kotlinx.android.synthetic.main.report_by_item_list.view.*

/**
 * Created by Hussain on 14-03-2018.
 */


class ReportByItemCatAdapter(private val mContext: Context, private var reportList:
ArrayList<GetReportItemCat.DataObject>) : RecyclerView
.Adapter<ReportByItemCatAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private lateinit var reportItemCat: GetReportItemCat.DataObject;

    override fun getItemCount(): Int {
        return reportList!!.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_ItemCode: TextView
        var tv_ItemName: TextView
        var tv_CostRate: TextView
        var tv_CostAmount: TextView
        var tv_SaleRate: TextView
        var tv_Disc: TextView
        var tv_Net_Rate: TextView
        var tv_Qty: TextView

        init {
            tv_ItemCode = view.tv_ItemCode
            tv_ItemName = view.tv_ItemName
            tv_CostRate = view.tv_CostRate
            tv_CostAmount = view.tv_CostAmount
            tv_SaleRate = view.tv_SaleRate
            tv_Disc = view.tv_Disc
            tv_Net_Rate = view.tv_Net_Rate
            tv_Qty = view.tv_Qty
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.report_by_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        reportItemCat = reportList[position]
        holder.tv_ItemCode.setText(reportItemCat!!.itemCode)
        holder.tv_Qty.setText(reportItemCat!!.availableQuantity)
        holder.tv_ItemName.setText(reportItemCat!!.itemNameEn)
        holder.tv_CostRate.setText(reportItemCat!!.costPrice)
        holder.tv_CostAmount.setText(reportItemCat!!.costAmount)
        holder.tv_SaleRate.setText(reportItemCat!!.salesPrice)
        holder.tv_Disc.setText(reportItemCat!!.discount+"%")
        holder.tv_Net_Rate.setText(reportItemCat!!.unitPrice)
    }
}