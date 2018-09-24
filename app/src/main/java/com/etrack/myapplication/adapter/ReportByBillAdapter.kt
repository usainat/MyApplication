package com.etrack.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.model.outputService.GetRepotBIllPO
import com.etrack.myapplication.model.outputService.GetRepotBillSO
import com.etrack.myapplication.model.outputService.GetRepotBillSta
import kotlinx.android.synthetic.main.report_bill.view.*

/**
 * Created by Hussain on 14-03-2018.
 */


class ReportByBillAdapter(private val mContext: Context, private var reportListPo:
ArrayList<GetRepotBIllPO.DataObject>, private var reportListSo:
                          ArrayList<GetRepotBillSO.DataObject>, private var reportListSta:
                          ArrayList<GetRepotBillSta.DataObject>, private var type:
                          String) : RecyclerView
.Adapter<ReportByBillAdapter.MyViewHolder>() {
    //private var selectedProductList: List<GetItemListSuccessFailure.GetItemListDataObject> = ArrayList()
    private lateinit var reportItemPo: GetRepotBIllPO.DataObject;
    private lateinit var reportItemSo: GetRepotBillSO.DataObject;
    private lateinit var reportItemSta: GetRepotBillSta.DataObject;
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
        var tv_date: TextView
        var tv_supplyname: TextView
        var tv_remark: TextView
        var tv_billamount: TextView

        init {

            tv_number = view.tv_number
            tv_supplyname = view.tv_supplyname
            tv_date = view.tv_date
            tv_remark = view.tv_remark
            tv_billamount = view.tv_billamount
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.report_bill, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (type == "PO") {
            reportItemPo = reportListPo[position]
            holder.tv_billamount.setText(reportItemPo!!.totalAmount)
            holder.tv_date.setText(reportItemPo!!.purchaseDate)
            holder.tv_number.setText((position + 1).toString())
            holder.tv_remark.setText(reportItemPo!!.remarks)
            holder.tv_supplyname.setText(reportItemPo!!.purchaseFrom)
        } else if (type == "SO") {
            reportItemSo = reportListSo[position]
            holder.tv_billamount.setText(reportItemSo!!.totalAmount)
            holder.tv_date.setText(reportItemSo!!.salesDate)
            holder.tv_number.setText((position + 1).toString())
            holder.tv_remark.setText(reportItemSo!!.remarks)
            holder.tv_supplyname.setText(reportItemSo!!.salesTo)
        } else if (type == "STA") {
            reportItemSta = reportListSta[position]
            holder.tv_billamount.setVisibility(View.GONE)
            holder.tv_date.setText(reportItemSta!!.stadjustDate)
            holder.tv_number.setText((position + 1).toString())
            holder.tv_remark.setText(reportItemSta!!.remarks)
            holder.tv_supplyname.setText("Stock Adjust")
        }
    }
}