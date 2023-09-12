package com.colourmoon.gobuddy.controllers.commoncontrollers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnyFileUploadController {

    private static AnyFileUploadController anyFileUploadController;
    private GoBuddyApiInterface goBuddyApiInterface;

    private AnyFileUploadController() {
        // private constructor
    }

    public static AnyFileUploadController getImageUploadControllerInstance() {
        if (anyFileUploadController == null) {
            anyFileUploadController = new AnyFileUploadController();
        }
        return anyFileUploadController;
    }

    public interface AnyFileUploadControllerListener {
        void onImageUploadSuccessResponse(String imageUrl, String fromWhichProof);

        void onImageUploadFailureResponse(String failureReason);
    }

    private AnyFileUploadControllerListener imageUploadControllerListener;

    public void AnyFileUploadControllerListener(AnyFileUploadControllerListener imageUploadControllerListener) {
        this.imageUploadControllerListener = imageUploadControllerListener;
    }

    public void callImageUploadApi(String imageFilePath, Context context, String fromWhichProof) {
        goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        File actualImageFile = new File(imageFilePath);
        File compressedImageFile = null;
        try {
            compressedImageFile = new Compressor(context).compressToFile(actualImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
        MultipartBody.Part multiPartImagePart = MultipartBody.Part.createFormData("file",
                compressedImageFile.getName(), imagePart);
        Call<ResponseBody> imageUploadCall = goBuddyApiInterface.anyUploadFile(multiPartImagePart);
        imageUploadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (imageUploadControllerListener != null) {
                                imageUploadControllerListener.onImageUploadSuccessResponse(jsonObject.getString("file_name"), fromWhichProof);
                            }
                        } else {
                            if (imageUploadControllerListener != null) {
                                imageUploadControllerListener.onImageUploadFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (imageUploadControllerListener != null) {
                        imageUploadControllerListener.onImageUploadFailureResponse("No Response From Server \nPlease try Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (imageUploadControllerListener != null) {
                    imageUploadControllerListener.onImageUploadFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
