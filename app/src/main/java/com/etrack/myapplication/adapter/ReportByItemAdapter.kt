package com.etrack.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.model.outputService.GetRepotItemPO
import com.etrack.myapplication.model.outputService.GetRepotItemSO
import com.etrack.myapplication.model.outputService.GetRepotItemSta
import kotlinx.android.synthetic.main.report_item.view.*

/**
 * Created by Hussain on 14-03-2018.
 */


class ReportByItemAdapter(private val mContext: Context, private var reportListPo:
ArrayList<GetRepotItemPO.DataObject>, private var reportListSo:
                          ArrayList<GetRepotItemSO.DataObject>, private var reportListSta:
                          ArrayList<GetRepotItemSta.DataObject>, private var type:
                          String) : RecyclerView
.Adapter<ReportByItemAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private lateinit var reportItemPo: GetRepotItemPO.DataObject;
    private lateinit var reportItemSo: GetRepotItemSO.DataObject;
    private lateinit var reportItemSta: GetRepotItemSta.DataObject;
    override fun getItemCount(): Int {
        if (type == "PO")
            return reportListPo!!.size
        else if (type == "SO")
            return reportListSo!!.size
        else
            return reportListSta!!.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_number: TextView
        var tv_supplyname: TextView
        var tv_ItemName: TextView
        var tv_ItemQty: TextView
        var tv_ItemRate: TextView
        var tv_billamount: TextView
        var tv_date: TextView

        init {


            tv_number = view.tv_number
            tv_date = view.tv_date
            tv_supplyname = view.tv_supplyname
            tv_ItemName = view.tv_ItemName
            tv_ItemQty = view.tv_ItemQty
            tv_ItemRate = view.tv_ItemRate
            tv_billamount = view.tv_billamount
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.report_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (type == "PO") {
            reportItemPo = reportListPo[position]
            holder.tv_number.setText((position + 1).toString())
            holder.tv_date.setText(reportItemPo!!.purchaseDate)
            holder.tv_supplyname.setText(reportItemPo!!.accountNameEn)
            holder.tv_ItemName.setText(reportItemPo!!.itemNameEn)
            holder.tv_ItemQty.setText(reportItemPo!!.itemQuantity)
            holder.tv_ItemRate.setText(reportItemPo!!.itemPrice)
            holder.tv_billamount.setText(reportItemPo!!.amount)
        } else if (type == "SO") {
            reportItemSo = reportListSo[position]
            holder.tv_number.setText((position + 1).toString())
            holder.tv_date.setText(reportItemSo!!.salesDate)
            holder.tv_supplyname.setText(reportItemSo!!.accountNameEn)
            holder.tv_ItemName.setText(reportItemSo!!.itemNameEn)
            holder.tv_ItemQty.setText(reportItemSo!!.itemQuantity)
            holder.tv_ItemRate.setText(reportItemSo!!.itemPrice)
            holder.tv_billamount.setText(reportItemSo!!.amount)
        } else if (type == "STA") {
            reportItemSta = reportListSta[position]
            holder.tv_number.setText((position + 1).toString())
            holder.tv_date.setText(reportItemSta!!.stadjustDate)
            holder.tv_ItemName.setText(reportItemSta!!.itemNameEn)
            if (reportItemSta!!.type.equals("1"))
                holder.tv_ItemQty.setText(reportItemSta!!.itemQuantity)
            else
                holder.tv_ItemQty.setText("-" + reportItemSta!!.itemQuantity)
            holder.tv_ItemRate.setText(reportItemSta!!.itemQuantity)
            holder.tv_ItemRate.visibility = View.GONE
            holder.tv_billamount.visibility = View.GONE
            holder.tv_supplyname.setText("Stock Adjust")
        }
    }
}