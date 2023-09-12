package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.PlaceOrderFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.AddressModel;
import com.colourmoon.gobuddy.model.OrderDetailsModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments.AddAddressFragment;
import com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments.SaveAddressFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.colourmoon.gobuddy.utilities.Constants.SELECT_PROVIDER_FRAGMENT_TAG;

public class PlaceOrderFragment extends Fragment implements View.OnClickListener, PlaceOrderFragmentController.PlaceOrderFragmentControllerListener {
    private static final String ORDER_ID_PARAM = "orderIdParam";

    private String order_id, couponText, couponId, couponAmount, finalPrice;

    private boolean isCouponApplied;

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    private TextView name_textView, address_textView, date_textView, address_changeBtn, orderName_textView,
            orderPrice_textView, payOnline_textView, payBycash_textView, placeOrderBtn, apply_couponBtn, couponPriceView, removeCouponBtn, finalPriceView,
            extra_charges_title,extra_charges_price,total_price;
    private LinearLayout onlineLayoutBtn, cashLayoutBtn, couponAppliedLayout, couponApplyLayout;
    private int paymentType = 0;
    private TextInputLayout couponEditText;
    private AddressModel addressModel;
    private OrderDetailsModel orderDetailsModel;

    public static PlaceOrderFragment newInstance(String orderId) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID_PARAM, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_id = getArguments().getString(ORDER_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Place Order");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        castingViews(view);

        couponEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        ProgressBarHelper.show(getActivity(), "Fetching Order Details");
        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("id", order_id);
        hashmap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        PlaceOrderFragmentController.getInstance().getOrderPageDetailsApiCall(hashmap);
        PlaceOrderFragmentController.getInstance().setPlaceOrderFragmentControllerListener(this);

        address_changeBtn.setOnClickListener(this);
        placeOrderBtn.setOnClickListener(this);
        onlineLayoutBtn.setOnClickListener(this);
        cashLayoutBtn.setOnClickListener(this);
        apply_couponBtn.setOnClickListener(this);
        removeCouponBtn.setOnClickListener(this);

        return view;
    }

    private void castingViews(View view) {
        name_textView = view.findViewById(R.id.placeOrder_nameView);
        address_textView = view.findViewById(R.id.placeOrder_addressView);
        date_textView = view.findViewById(R.id.placeOrder_dateView);
        address_changeBtn = view.findViewById(R.id.placeOrder_addressChangeBtn);
        orderName_textView = view.findViewById(R.id.placeOrder_TitleView);
        orderPrice_textView = view.findViewById(R.id.placeOrder_priceView);
        payOnline_textView = view.findViewById(R.id.payOnlineTextView);
        payBycash_textView = view.findViewById(R.id.payByCashTextView);
        placeOrderBtn = view.findViewById(R.id.placeOrderBtn);
        onlineLayoutBtn = view.findViewById(R.id.onlineLayout);
        cashLayoutBtn = view.findViewById(R.id.cashlayout);
        apply_couponBtn = view.findViewById(R.id.applyCouponBtn);
        couponEditText = view.findViewById(R.id.couponEditText);
        couponAppliedLayout = view.findViewById(R.id.couponAppliedLayout);
        couponApplyLayout = view.findViewById(R.id.couponApplyLayout);
        removeCouponBtn = view.findViewById(R.id.removeCouponBtn);
        couponPriceView = view.findViewById(R.id.placeOrder_couponPriceView);
        finalPriceView = view.findViewById(R.id.placeOrder_finalPriceView);
        extra_charges_title = view.findViewById(R.id.extra_charges_title);
        extra_charges_price = view.findViewById(R.id.extra_charges_price);
        total_price = view.findViewById(R.id.total_price);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeOrder_addressChangeBtn:
                if (addressModel == null) {
                    addToFragmentContainer(new AddAddressFragment(), true, "AddAddressTag");
                } else {
                    addToFragmentContainer(new SaveAddressFragment(), true, "saveAddressTag");
                }
                break;
            case R.id.placeOrderBtn:
               /* showPlaceOrderSnackBar(
                        "Sorry, You will be able to request services from August 2nd 2019 onwards, we are working hard to give you the best experience. Thank you for trying."
                );*/
                if (addressModel == null) {
                    showSnackBar("Please Add Address To Proceed");
                    return;
                }
                if (paymentType == 0) {
                    showSnackBar("Please Select Payment Type");
                } else {
                    ProgressBarHelper.show(getActivity(), "Placing Order");
                    PlaceOrderFragmentController.getInstance().placeOrderApiCall(createPlaceOrderMap());
                }
                break;
            case R.id.onlineLayout:
                onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                payOnline_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
                payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
                cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
                payBycash_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
                paymentType = 1;
                break;
            case R.id.cashlayout:
                cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                payBycash_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
                payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
                onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
                payOnline_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
                paymentType = 2;
                break;
            case R.id.applyCouponBtn:
                couponText = couponEditText.getEditText().getText().toString();
                if (!validateCoupon()) {
                    return;
                } else {
                    Map<String, String> couponMap = new HashMap<>();
                    couponMap.put("coupon", couponText);
                    couponMap.put("price", orderDetailsModel.getServicePrice());
                    couponMap.put("category_id", orderDetailsModel.getCategory_id());
                    couponMap.put("sub_category_id", orderDetailsModel.getSub_category());
                    couponMap.put("extra_charges_price", orderDetailsModel.getExtra_charges_price());
                    couponMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
                    ProgressBarHelper.show(getActivity(), "Applying PromoCode");
                    checkCouponApiCall(couponMap);
                }
                break;
            case R.id.removeCouponBtn:
                couponAppliedLayout.setVisibility(View.GONE);
                couponApplyLayout.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private Map<String, String> createPlaceOrderMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("address", addressModel.getAddressId());
        hashMap.put("payment_mode", paymentType == 1 ? "online" : "cash");
        hashMap.put("coupon_id", isCouponApplied ? couponId : "");
        hashMap.put("coupon_amount", isCouponApplied ? couponAmount : "");
        hashMap.put("sub_total", isCouponApplied ? finalPrice : orderDetailsModel.getTotal());
        hashMap.put("id", order_id);
        return hashMap;
    }

    private boolean validateCoupon() {
        if (couponText.isEmpty()) {
            couponEditText.setError("Please Enter Your Coupon Code");
            return false;
        } else {
            couponEditText.setError(null);
            return true;
        }
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.customer_fragments_container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.customerFragments_coordinator_layout);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", view -> snackbar.dismiss());
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    public void showPlaceOrderSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.customerFragments_coordinator_layout);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, 4000);
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(5);
    }

    private void checkCouponApiCall(Map<String, String> checkCouponMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> checkCouponCall = goBuddyApiInterface.checkCoupon(checkCouponMap);
        checkCouponCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                ProgressBarHelper.dismiss(getActivity());
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            isCouponApplied = true;
                            couponApplyLayout.setVisibility(View.GONE);
                            couponAppliedLayout.setVisibility(View.VISIBLE);
                            couponAmount = jsonObject.getString("coupon_amount");
                            couponPriceView.setText(getResources().getString(R.string.indian_rupee) + couponAmount);
                            finalPrice = jsonObject.getString("amount");
                            finalPriceView.setText(getResources().getString(R.string.indian_rupee) + finalPrice);
                            couponId = jsonObject.getString("coupon_id");
                        } else {
                            showSnackBar(jsonObject.getString("message"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSnackBar("No Response From Server \n Please Try Again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ProgressBarHelper.dismiss(getActivity());
                t.printStackTrace();
                showSnackBar(t.getLocalizedMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetOrderDetailsSuccess(OrderDetailsModel orderDetailsModel, AddressModel addressModel) {
        if (addressModel == null) {
            name_textView.setVisibility(View.GONE);
            address_textView.setText("No Address Yet \nClick on Change Button on Add Address");
        } else {
            name_textView.setText(addressModel.getName());
            this.addressModel = addressModel;
            address_textView.setText(addressModel.getHouse_street() + " , " + addressModel.getLocality());
        }

        date_textView.setText(orderDetailsModel.getServiceDate() + " , " + orderDetailsModel.getServiceTime());
        orderName_textView.setText(orderDetailsModel.getServiceTitle());
        if (orderDetailsModel.getExtra_charges_title().isEmpty()){
            extra_charges_title.setText("Extra Charges");
        }else {
            extra_charges_title.setText(orderDetailsModel.getExtra_charges_title());
        }

        // TODO error - java.lang.IllegalStateException: Fragment PlaceOrderFragment{520f37e} not attached to a context.
        if (getActivity() != null) {
            orderPrice_textView.setText(getActivity().getResources().getString(R.string.indian_rupee) + orderDetailsModel.getServicePrice());
        }

        if (getActivity() != null) {
            extra_charges_price.setText(getActivity().getResources().getString(R.string.indian_rupee) + orderDetailsModel.getExtra_charges_price());
        }

        if (getActivity() != null) {
            total_price.setText(getActivity().getResources().getString(R.string.indian_rupee) + orderDetailsModel.getTotal());
        }
        this.orderDetailsModel = orderDetailsModel;
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onPlaceOrderSuccess(String orderId) {
        Log.d("placeOrder", "triggere2" + orderId);
        ProgressBarHelper.dismiss(getActivity());
        callOrderNotifications();
        addToFragmentContainer(SelectProviderFragment.newInstance(orderId), true, SELECT_PROVIDER_FRAGMENT_TAG);

    }

    @Override
    public void onFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        showSnackBar(failureReason);
    }

    private void callOrderNotifications() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> placeOrderCall = goBuddyApiInterface.callOrderNotifications(createNotificationMap());
        placeOrderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("placeOrder", "triggered3" + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Map<String, String> createNotificationMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("id", order_id);
        hashMap.put("address", addressModel.getAddressId());
        return hashMap;
    }
}
