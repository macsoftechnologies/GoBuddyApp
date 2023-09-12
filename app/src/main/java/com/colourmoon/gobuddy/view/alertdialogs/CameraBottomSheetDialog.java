package com.colourmoon.gobuddy.view.alertdialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;

public class CameraBottomSheetDialog extends BottomSheetDialogFragment {

    private CameraBottomSheetListener cameraBottomSheetListener;

    public static CameraBottomSheetDialog getInstance() {
        return new CameraBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alertdialog_uploadphoto, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setWhiteNavigationBar(getDialog());
        }
      //  Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font/poppins.ttf");
        TextView takePhoto = view.findViewById(R.id.takePhotoView);
        TextView openGallery = view.findViewById(R.id.galleryView);
       // takePhoto.setTypeface(face);
       // openGallery.setTypeface(face);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                cameraBottomSheetListener.onEventSelected("OpenCamera");
            }
        });
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                cameraBottomSheetListener.onEventSelected("SelectFromGallery");
            }
        });

        return view;
    }

    public interface CameraBottomSheetListener {
        void onEventSelected(String eventType);
    }

    public void setCameraBottomSheetListener(CameraBottomSheetListener cameraBottomSheetListener) {
        this.cameraBottomSheetListener = cameraBottomSheetListener;
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

    /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            cameraBottomSheetListener = (CameraBottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CameraBottomSheetListener");
        }
    }*/
}
