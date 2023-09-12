package com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.colourmoon.gobuddy.model.EkycModel;
import com.colourmoon.gobuddy.utilities.Utils;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ImageUploadController;
import com.colourmoon.gobuddy.controllers.providercontrollers.UpdateEkycController;
import com.colourmoon.gobuddy.utilities.ProviderPostRegFragmentsListener;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.alertdialogs.CameraBottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.colourmoon.gobuddy.utilities.Constants.CAMERA_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.CAMERA_REQUEST_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.GALLERY_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.GALLERY_REQUEST_CODE;

public class UpdateEKycFragment extends Fragment implements CameraBottomSheetDialog.CameraBottomSheetListener,
        ImageUploadController.ImageUploadControllerListener, UpdateEkycController.UpdateEkycControllerListener {

    private static final String TYPE_PARAM = "typeParam";

    // TODO: Rename and change types of parameters
    private String updateType;

    private Spinner addressProofSpinner, idProofSpinner;
    private ImageView addressProofImageView, idProofImageView;
    private TextView addressProofImageUploadBtn, idProofImageUploadBtn;
    private String mCurrentPhotoPath, fromWhichProof;
    private TextView updateEkyc_skipBtn, updateEkyc_nextBtn;
    private String addressProofText, idProofText, addressProofImageString, idProofImageString;

    private OnFragmentInteractionListener mListener;

    public UpdateEKycFragment() {
        // Required empty public constructor
    }

    private ProviderPostRegFragmentsListener postRegFragmentsListener;

    public void setPostRegFragmentsListener(ProviderPostRegFragmentsListener postRegFragmentsListener) {
        this.postRegFragmentsListener = postRegFragmentsListener;
    }

    public static UpdateEKycFragment newInstance(String updateType) {
        UpdateEKycFragment fragment = new UpdateEKycFragment();
        Bundle args = new Bundle();
        args.putString(TYPE_PARAM, updateType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            updateType = getArguments().getString(TYPE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update e-KYC");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_ekyc, container, false);

        // this method is responsible for casting all views in xml file with java file
        castingViews(view);

        UpdateEkycController.getInstance().setUpdateEkycControllerListener(this);

        // creating instance for cameraBottomSheet dialog
        CameraBottomSheetDialog cameraBottomSheetDialog = CameraBottomSheetDialog.getInstance();
        cameraBottomSheetDialog.setCameraBottomSheetListener(this);
        Bundle bundle = new Bundle();
        idProofImageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("isFrom", "IdProof");
                fromWhichProof = "IdProof";
                cameraBottomSheetDialog.setArguments(bundle);
                cameraBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "cameraBottomSheet");
            }
        });

        addressProofImageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("isFrom", "AddressProof");
                fromWhichProof = "AddressProof";
                cameraBottomSheetDialog.setArguments(bundle);
                cameraBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "cameraBottomSheet");
            }
        });

        updateEkyc_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPhotoPath != null) {
                    if (!validateAddressProofImage() | !validateIdProofImage()) {
                        return;
                    } else {
                        GetTextFromSpinners();
                        UpdateEkycController.getInstance().submitEkycApiCall(createEkycMap());
                    }
                } else {
                    showSnackBar("Please Upload Both Proof's Images");
                }
            }
        });

        updateEkyc_skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSkipClick();
                }
            }
        });

        ImageUploadController.getImageUploadControllerInstance().setImageUploadControllerListener(this);
        return view;
    }

    private Map<String, String> createEkycMap() {
        Map<String, String> ekycMap = new HashMap<>();
        ekycMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        ekycMap.put("document_type", addressProofText);
        ekycMap.put("document", addressProofImageString);
        ekycMap.put("document_type1", idProofText);
        ekycMap.put("document1", idProofImageString);
        return ekycMap;
    }

    private void GetTextFromSpinners() {
        addressProofText = addressProofSpinner.getSelectedItem().toString();
        idProofText = idProofSpinner.getSelectedItem().toString();
    }

    private boolean validateIdProofImage() {
        if (idProofImageString != null && idProofImageString.isEmpty()) {
            showSnackBar("Please Upload IdProof Image");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddressProofImage() {
        if (addressProofImageString != null && addressProofImageString.isEmpty()) {
            showSnackBar("Please Upload AddressProof Image");
            return false;
        } else {
            return true;
        }
    }

    private void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.postRegCoordinatorLayout);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    private void castingViews(View view) {
        addressProofSpinner = view.findViewById(R.id.address_proof_spinner);
        idProofSpinner = view.findViewById(R.id.idproof_spinner);
        addressProofImageUploadBtn = view.findViewById(R.id.address_proof_imageUploadBtn);
        idProofImageUploadBtn = view.findViewById(R.id.idProof_imageUploadBtn);
        addressProofImageView = view.findViewById(R.id.address_proof_imageView);
        idProofImageView = view.findViewById(R.id.idproof_imageview);
        updateEkyc_nextBtn = view.findViewById(R.id.updateEkyc_nextBtn);
        updateEkyc_skipBtn = view.findViewById(R.id.updateEkyc_skipBtn);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEventSelected(String eventType) {
        if (eventType.equals("OpenCamera")) {
            if (checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                LoadCaptureImageScreen();
            } else {
                requestCameraPermission();
            }
        } else if (eventType.equals("SelectFromGallery")) {
            if (checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                LoadImageFromGallery();
            } else {
                requestStoragePermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(getActivity(), "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(getActivity(), "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Info")
                    .setMessage("Camera Permission is Needed for Adding your Proof Image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Info")
                    .setMessage("Gallery Permission is needed for adding your Proof Image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void LoadCaptureImageScreen() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.colourmoon.gobuddy.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap bitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            if (fromWhichProof.equals("IdProof")) {
                idProofImageView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            } else {
                addressProofImageView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            }
            ImageUploadController.getImageUploadControllerInstance().callImageUploadApi(mCurrentPhotoPath, getActivity(), fromWhichProof);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            if (selectedImage != null && selectedImage.toString().startsWith("content://com.google.android.apps.photos.content")) {
                if (selectedImage.toString().contains("video")) {
                    Utils.getInstance().showSnackBarOnCustomerScreen("Hey! Its Video Buddy", getActivity());
                    return;
                }
                createImageFromPhotosUri(selectedImage);
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                options.inSampleSize = 2;
            }
            if (fromWhichProof.equals("IdProof")) {
                idProofImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath, options));
            } else {
                addressProofImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath, options));
            }

            ImageUploadController.getImageUploadControllerInstance().callImageUploadApi(mCurrentPhotoPath, getActivity(), fromWhichProof);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void createImageFromPhotosUri(Uri selectedImage) {
        try {
            InputStream is = getActivity().getContentResolver().openInputStream(selectedImage);
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
                File f = createImageFile();
                try {
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(outStream.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    Log.w("TAG", "Error saving image file: " + e.getMessage());

                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onImageUploadSuccessResponse(String imageUrl, String fromWhichProof) {
        if (fromWhichProof.equals("IdProof")) {
            idProofImageString = imageUrl;
        } else {
            addressProofImageString = imageUrl;
        }
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageUploadFailureResponse(String failureReason) {
        Toast.makeText(getActivity(), "Image Upload Failed \nPlease Try Again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEkycSuccessResponse(String successMessage) {
        if (mListener != null) {
            mListener.onNextClick();
        }
    }

    @Override
    public void onGetEkycDetailsSuccessResponse(List<EkycModel> ekycModelList) {
        // update ekyc
    }

    @Override
    public void onFailureResponse(String failureReason) {
        showSnackBar(failureReason);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onNextClick();

        void onSkipClick();
    }

    private Bitmap rotatedImageBitmap(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
