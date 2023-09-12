package com.colourmoon.gobuddy.view.alertdialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.colourmoon.gobuddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PaymentBottomSheetDialog extends BottomSheetDialogFragment {

    public static PaymentBottomSheetDialog getInstance() {
        return new PaymentBottomSheetDialog();
    }

    public interface PaymentBottomSheetDialogListener {
        void onEventSelected(String type);
    }

    private PaymentBottomSheetDialogListener paymentBottomSheetDialogListener;

    public void setPaymentBottomSheetDialogListener(PaymentBottomSheetDialogListener paymentBottomSheetDialogListener) {
        this.paymentBottomSheetDialogListener = paymentBottomSheetDialogListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alertdialog_paymenttype, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setWhiteNavigationBar(getDialog());
        }
        //  Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font/poppins.ttf");
        TextView viaOnline = view.findViewById(R.id.alert_payViaOnline);
        TextView viaCash = view.findViewById(R.id.alert_payViacash);
        // takePhoto.setTypeface(face);
        // openGallery.setTypeface(face);
        viaOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                paymentBottomSheetDialogListener.onEventSelected("online");
            }
        });
        viaCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                paymentBottomSheetDialogListener.onEventSelected("cash");
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

}
