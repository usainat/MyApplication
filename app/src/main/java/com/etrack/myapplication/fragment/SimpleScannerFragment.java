package com.etrack.myapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.etrack.myapplication.Utils.CommonValue;

import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


/**
 * Created by Husain on 10/22/17.`
 * OCS infotech Ltd
 */

@SuppressLint("ValidFragment")
public class SimpleScannerFragment extends DialogFragment implements ZBarScannerView
        .ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    public static boolean hasOpenedDialogs(FragmentActivity activity) {

        return false;
    }
    @Override
    public void handleResult(final Result rawResult) {
     //   CommonValue.qrCode ="1234" ;
        rawResult.getContents();
        this.dismiss();


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


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}