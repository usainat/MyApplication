package com.etrack.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.model.outputService.ReportItemHist
import kotlinx.android.synthetic.main.report_by_item_history.view.*


/**
 * Created by Hussain on 14-03-2018.
 */


class ReportByItemHistAdapter(private val mContext: Context, private var reportList:
ArrayList<ReportItemHist.DataObject>) : RecyclerView
.Adapter<ReportByItemHistAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private lateinit var reportItemHist: ReportItemHist.DataObject;

    override fun getItemCount(): Int {
        return reportList!!.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_ItemNo: TextView
        var tv_ItemDate: TextView
        var tv_billCode: TextView
        var tv_ItemName: TextView
        var tv_ItemType: TextView
        var tv_Qty: TextView

        init {
            tv_ItemNo = view.tv_ItemNo
            tv_ItemDate = view.tv_ItemDate
            tv_billCode = view.tv_billCode
            tv_ItemName = view.tv_ItemName
            tv_ItemType = view.tv_ItemType
            tv_Qty = view.tv_Qty
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.report_by_item_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        reportItemHist = reportList[position]
        holder.tv_ItemNo.setText((position+1).toString())
        holder.tv_ItemDate.setText(reportItemHist!!.billDate)
        holder.tv_billCode.setText(reportItemHist!!.billCode)
        holder.tv_ItemName.setText(reportItemHist!!.itemNameEn)
        holder.tv_ItemType.setText(reportItemHist!!.billType)
        holder.tv_Qty.setText(reportItemHist!!.itemQuantity)
    }
}