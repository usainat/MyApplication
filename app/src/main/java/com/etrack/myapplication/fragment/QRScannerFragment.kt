package com.etrack.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.etrack.myapplication.Utils.CommonValue
import com.etrack.myapplication.`interface`.UpdateList
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView


/**
 * Created by Husain on 10/22/17.
 * OCS infotech Ltd
 */

@SuppressLint("ValidFragment")
class QRScannerFragment(updateQr:UpdateQr) : DialogFragment(), ZBarScannerView.ResultHandler {
    private var mScannerView: ZBarScannerView? = null
    private var updateQr: UpdateQr

    init {
//        this.productList = productList
        this.updateQr = updateQr
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mScannerView = ZBarScannerView(activity)
        return mScannerView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // request a window without the title
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        //CommonValue.qrCode
        updateQr!!.updateQrValue(rawResult.contents.toString())
        this.dismiss()


        /*     Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String nameOffragmentOnForeground = fm.getBackStackEntryAt(
                        fm.getBackStackEntryCount() - 1).getName();

                if (nameOffragmentOnForeground != null) {
                    ScanedProductFragment scanedProductFragment = (ScanedProductFragment) getActivity().getSupportFragmentManager()
                               .findFragmentByTag(FrgmentName.SIMPLE_SCANNER_FRAGMENT);
                    scanedProductFragment.updateQr(rawResult.getContents());
                }
                getDialog().dismiss();
            }
        }, 1000);*/
    }


    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

}