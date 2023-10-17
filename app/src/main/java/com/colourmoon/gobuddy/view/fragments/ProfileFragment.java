package com.colourmoon.gobuddy.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.AnyFileUploadController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ProfileFragmentController;
import com.colourmoon.gobuddy.helper.LocationDetailsHelper;
import com.colourmoon.gobuddy.model.ProfileModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.MapsActivity;
import com.colourmoon.gobuddy.view.alertdialogs.CameraBottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient.BASE_URL;
import static com.colourmoon.gobuddy.utilities.Constants.CAMERA_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.CAMERA_REQUEST_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.GALLERY_PERMISSION_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.GALLERY_REQUEST_CODE;
import static com.colourmoon.gobuddy.utilities.Constants.LOCATION_PERMISSION_CODE;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_address;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_latitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.main_longitude;
import static com.colourmoon.gobuddy.view.activities.CustomerMainActivity.placeId;

public class ProfileFragment extends Fragment implements ProfileFragmentController.ProfileFragmentControllerListener, CameraBottomSheetDialog.CameraBottomSheetListener,
        AnyFileUploadController.AnyFileUploadControllerListener, LocationDetailsHelper.LocationDetailsResponseListener {

    private static final String FROM_PARAM = "fromParam";

    private String from;

    // widgets
    private TextInputLayout profileNameEditText, profileEmailEditText, profilePhoneNumEditText, profileDobEditText, profileAddressEditText;
    private CircleImageView profileImageView;
    private ImageView profileImageChangeBtn;
    private TextView updateProfileBtn;
    private EditText edtComment;

    // variables
    private String profileName, profileEmail, profilePhoneNum, profileDob, profileAddress, profileImage, mCurrentPhotoPath;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String from) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(FROM_PARAM, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString(FROM_PARAM);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // this is for casting all views in java to xml file
        castingViews(view);

        profileNameEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        profileEmailEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});
        profileAddressEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        ProfileFragmentController.getInstance().getProfileDetailsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        ProfileFragmentController.getInstance().setProfileFragmentControllerListener(this);
        AnyFileUploadController.getImageUploadControllerInstance().AnyFileUploadControllerListener(this);
        LocationDetailsHelper.getInstance(getActivity()).setLocationDetailsResponseListener(this);

        profileAddressEditText.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (profileAddressEditText.getEditText().getRight() -
                            profileAddressEditText.getEditText().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            startActivityForResult(new Intent(getActivity(), MapsActivity.class), 1005);
                        } else {
                            requestLocationPermissions();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // for getting location details
        getLocationDetails();

        updateProfileBtn.setOnClickListener(v -> {
            GetInputFromEditText();
            if (!validateEmail() | !validateName() | !validateAddress() |
                    !validateDateOfBirth() | !validateProfileImage()) {
                return;
            } else {
                ProfileFragmentController.getInstance().postProfileDetailsApiCall(createProfileMap());
            }
        });

        profileImageChangeBtn.setOnClickListener(v -> {
            CameraBottomSheetDialog cameraBottomSheetDialog = CameraBottomSheetDialog.getInstance();
            cameraBottomSheetDialog.setCameraBottomSheetListener(this);
            cameraBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "cameraBottomSheet");
        });

        profileDobEditText.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (profileDobEditText.getEditText().getRight() -
                            profileDobEditText.getEditText().getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                profileDobEditText.getEditText().setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                        datePickerDialog.show();
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    public void requestLocationPermissions() {

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Info")
                    .setMessage("Location Permissions are needed to Show the NearBy Services to You")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
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
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    private void getLocationDetails() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (LocationDetailsHelper.getInstance(getActivity()).isLocationEnabled()) {
                LocationDetailsHelper.getInstance(getActivity()).getFusedLocationDetails();
            } else {
                LocationDetailsHelper.getInstance(getActivity()).showAlert(getActivity());
            }
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onSuccessLocationResponse(Double latitude, Double longitude, String address, String place_id, String pincode) {
        profileAddressEditText.getEditText().setText(address);
        main_latitude = String.valueOf(latitude);
        main_longitude = String.valueOf(longitude);
        main_address = address;
        placeId = place_id;
    }

    private void GetInputFromEditText() {
        profileEmail = profileEmailEditText.getEditText().getText().toString();
        profileName = profileNameEditText.getEditText().getText().toString();
        profileDob = profileDobEditText.getEditText().getText().toString();
        profilePhoneNum = profilePhoneNumEditText.getEditText().getText().toString();
        profileAddress = profileAddressEditText.getEditText().getText().toString();
    }

    private Map<String, String> createProfileMap() {
        Map<String, String> profileMap = new HashMap<>();
        profileMap.put("name", profileName);
        profileMap.put("email", profileEmail);
        profileMap.put("phone_number", profilePhoneNum);
        profileMap.put("address", profileAddress);
        profileMap.put("profile", profileImage);
        profileMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        profileMap.put("dob", profileDob);
        profileMap.put("latitude", main_latitude);
        profileMap.put("longitude", main_longitude);
        profileMap.put("place_id", placeId);
        profileMap.put("landmark", main_address);
        return profileMap;
    }

    private void castingViews(View view) {
        profileNameEditText = view.findViewById(R.id.profile_name_edittext);
        profileEmailEditText = view.findViewById(R.id.profile_email_edittext);
        profilePhoneNumEditText = view.findViewById(R.id.profile_phoneNum_edittext);
        profilePhoneNumEditText.getEditText().setEnabled(false);
        profileDobEditText = view.findViewById(R.id.profile_dateofBirth_edittext);
        profileAddressEditText = view.findViewById(R.id.profile_address_edittext);
        updateProfileBtn = view.findViewById(R.id.profileUpdateBtn);
        profileImageView = view.findViewById(R.id.profileImageView);
        profileImageChangeBtn = view.findViewById(R.id.profileChangeImageBtn);

    }

    private void setTextToFields(ProfileModel profileModel) {
        profileNameEditText.getEditText().setText(profileModel.getName());
        profileEmailEditText.getEditText().setText(profileModel.getEmail());
        profilePhoneNumEditText.getEditText().setText(profileModel.getPhoneNumber());
        profileDobEditText.getEditText().setText(profileModel.getDob());
        profileAddressEditText.getEditText().setText(profileModel.getAddress());
        profileImage = profileModel.getImageUrl();
        Glide.with(getActivity()).load(BASE_URL.substring(0, BASE_URL.length() - 4) + profileImage).into(profileImageView);
    }

    private boolean validateEmailWithRegex(String Email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    private boolean validateEmail() {
        if (profileEmail.isEmpty()) {
            return true;
        } else {
            if (!validateEmailWithRegex(profileEmail)) {
                profileEmailEditText.setError("Please Enter Valid Email");
                return false;
            } else {
                profileEmailEditText.setError(null);
                return true;
            }
        }
    }

    private boolean validatePhone() {
        if (profilePhoneNum.isEmpty()) {
            profilePhoneNumEditText.setError("Please Enter Your Phone Number");
            return false;
        } else if (profilePhoneNum.length() != 10) {
            profilePhoneNumEditText.setError("Please Enter a Valid Mobile Number");
            return false;
        } else {
            profilePhoneNumEditText.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        if (profileName.isEmpty()) {
            profileNameEditText.setError("Please Enter Your Name");
            return false;
        } else {
            profileNameEditText.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        if (profileAddress.isEmpty()) {
            profileAddressEditText.setError("Please Enter Your Address");
            return false;
        } else {
            profileAddressEditText.setError(null);
            return true;
        }
    }

    private boolean validateDateOfBirth() {
        if (profileDob.isEmpty()) {
            profileDobEditText.setError("Please Enter Your DateofBirth");
            return false;
        } else {
            profileDobEditText.setError(null);
            return true;
        }
    }

    private boolean validateProfileImage() {
        if (profileImage.isEmpty()) {
            if (from.equalsIgnoreCase("provider")) {
                Utils.getInstance().showSnackBarOnProviderScreen("Please Upload Image", getActivity());
            } else {
                Utils.getInstance().showSnackBarOnCustomerScreen("Please Upload Image", getActivity());
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onProfileDetailsSuccessResponse(ProfileModel profileModel) {
        setTextToFields(profileModel);
    }

    @Override
    public void onProfileUpdateSuccessResponse(String successResponse) {
        if (from.equalsIgnoreCase("provider")) {
            Utils.getInstance().showSnackBarOnProviderScreen(successResponse, getActivity());
        } else {
            Utils.getInstance().showSnackBarOnCustomerScreen(successResponse, getActivity());
        }
    }

    @Override
    public void onFailureReason(String failureReason) {
        if (from.equalsIgnoreCase("provider")) {
            Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
        } else {
            Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
        }
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
            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    if (LocationDetailsHelper.getInstance(getActivity()).isLocationEnabled()) {
                        LocationDetailsHelper.getInstance(getActivity()).getFusedLocationDetails();
                    } else {
                        LocationDetailsHelper.getInstance(getActivity()).showAlert(getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission DENIED \n Unable to Access Your Location", Toast.LENGTH_SHORT).show();
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
            profileImageView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            //profileImage = mCurrentPhotoPath;
            AnyFileUploadController.getImageUploadControllerInstance().callImageUploadApi(mCurrentPhotoPath, getActivity(), "");
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
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                options.inSampleSize = 2;
                //  profileImage = mCurrentPhotoPath;
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            }
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            profileImageView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            AnyFileUploadController.getImageUploadControllerInstance().callImageUploadApi(mCurrentPhotoPath, getActivity(), "");
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 1005 && resultCode == 1003) {
            profileAddressEditText.getEditText().setText(data.getStringExtra("addressLine"));
            main_latitude = data.getStringExtra("latitude");
            main_longitude = data.getStringExtra("longitude");
            main_address = data.getStringExtra("addressLine");
            placeId = data.getStringExtra("placeId");
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
        profileImage = imageUrl;
        if (from.equalsIgnoreCase("provider")) {
            Utils.getInstance().showSnackBarOnProviderScreen("Success", getActivity());
        } else {
            Utils.getInstance().showSnackBarOnCustomerScreen("Success", getActivity());
        }
    }

    @Override
    public void onImageUploadFailureResponse(String failureReason) {
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
    }

    private Bitmap rotatedImageBitmap(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = 0;
        if (ei != null) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        }

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
