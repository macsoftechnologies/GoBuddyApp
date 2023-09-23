package com.colourmoon.gobuddy.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.colourmoon.gobuddy.R;

import org.w3c.dom.Text;

public class DialogHelper {
    private Context context;

    public DialogHelper(Context mContext) {
        this.context = mContext;
    }

    public void showAlert(String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title)
                .setIcon(R.drawable.ic_logout_icon)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
     //   TextView textView = alertDialog.findViewById(android.R.id.message);
     //   textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "poppins.ttf"));
        alertDialog.show();
    }
}
