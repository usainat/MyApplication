package com.etrack.myapplication.commonUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.etrack.myapplication.R;
import com.etrack.myapplication.controller.MyApplication;

/**
 * Created by Hussain on 16-02-2018.
 */

public class CommonUtils {
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static void makeText(Context content, String message, int duration) {
        Toast.makeText(content, message, duration).show();
    }
    public static String  getUserId(Context mContext) {
        int userI = SharedPreferenceHelper.getSharedPreferenceInt(mContext,
                SharedPreferenceHelper.PREF_APP_USERID, 0);
        String userId= userI + "" ;
        return userId; }

    public static  String getUserRole(Context mContext) {
        String userRole = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_USERROLE, "0");
        return userRole;
    }

    public static  String  getCompanyId(Context mContext) {
        String companyId = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_COMPANY, "0");
        return companyId;  }

    public static String  getBranchId(Context mContext) {
        String branchId = SharedPreferenceHelper.getSharedPreferenceString(mContext,
                SharedPreferenceHelper.PREF_APP_BRANCH, "0");
        return branchId; }

    public static void popupDialog(final Activity context, final String dialogType, String message, String postiveText, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.exit_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (dialogType) {
                            case "Exit App":
                                context.finish();
                                break;

                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static  String  getUserName(Context mContext) {
        String companyId = SharedPreferenceHelper.getSharedPreferenceString(mContext,
                SharedPreferenceHelper.PREF_APP_USERNAME, "userName");
        return companyId;  }
}
