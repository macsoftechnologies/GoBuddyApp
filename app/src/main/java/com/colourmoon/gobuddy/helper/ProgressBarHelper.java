package com.colourmoon.gobuddy.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.colourmoon.gobuddy.R;

public class ProgressBarHelper {

    private static ProgressDialog progressDialog;

    public static void show(final Activity activity, final String message) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                progressDialog = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
                progressDialog.setMessage(message);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });
    }

    public static void dismiss(final Activity activity) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    Log.e("dismiss", "progress");
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }
}
