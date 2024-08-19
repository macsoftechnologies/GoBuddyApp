package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colourmoon.gobuddy.controllers.commoncontrollers.ProfileFragmentController;
import com.colourmoon.gobuddy.model.ProfileModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
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

public class PlaceOrderFragment extends Fragment implements PaymentResultListener, View.OnClickListener, PlaceOrderFragmentController.PlaceOrderFragmentControllerListener {
    private static final String ORDER_ID_PARAM = "orderIdParam";

    private String order_id,currentOrderId, couponText, couponId, couponAmount, finalPrice, price, pioncode, result, name, email, contact_no,totalprice;

    private boolean isCouponApplied;
    private  String amountSt;
     private TextView payonlineTextview,payordercash,paytotalcash,pay_cashtxt;


    private float  amount15Percent,amount;
    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    private TextView name_textView, address_textView, date_textView, address_changeBtn, orderName_textView,
            orderPrice_textView, payOnline_textView, payBycash_textView, placeOrderBtn, apply_couponBtn, couponPriceView, removeCouponBtn, finalPriceView,
            extra_charges_title, extra_charges_price, total_price, errortxt,manaulhandfee,cashorderbtn,onlineorderbtn,sftunits;
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
        Checkout.preload(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        castingViews(view);


        ProfileFragmentController.getInstance().getProfileDetailsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        ProfileFragmentController.getInstance().setProfileFragmentControllerListener(new ProfileFragmentController.ProfileFragmentControllerListener() {
            @Override
            public void onProfileDetailsSuccessResponse(ProfileModel profileModel) {

                // Split the address by commas

                name = profileModel.getName();
                email = profileModel.getEmail();
                contact_no = profileModel.getPhoneNumber();


            }

            @Override
            public void onProfileUpdateSuccessResponse(String successResponse) {

            }

            @Override
            public void onFailureReason(String failureReason) {

            }
        });


        //pricinglayout();



//        if (UserSessionManagement.getInstance(getContext()).getRegpincode() != null) {
//            pioncode = UserSessionManagement.getInstance(getContext()).getRegpincode();
//        } else {
//            pioncode = UserSessionManagement.getInstance(getContext()).getPincode();
//        }


        couponEditText.getEditText().setFilters(new InputFilter[]{Utils.getInstance().getEditTextFilter()});

        ProgressBarHelper.show(getActivity(), "Fetching Order Details");
        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("id", order_id);
       // Toast.makeText(getContext()," "+order_id,Toast.LENGTH_LONG).show();
        //  hashmap.put("pincode", pioncode);
        hashmap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
      //  Toast.makeText(getContext(),"id"+UserSessionManagement.getInstance(getActivity()).getUserId(),Toast.LENGTH_SHORT).show();
        PlaceOrderFragmentController.getInstance().getOrderPageDetailsApiCall(hashmap);
        PlaceOrderFragmentController.getInstance().setPlaceOrderFragmentControllerListener(this);

        address_changeBtn.setOnClickListener(this);
        placeOrderBtn.setOnClickListener(this);
        onlineLayoutBtn.setOnClickListener(this);
        cashLayoutBtn.setOnClickListener(this);
        apply_couponBtn.setOnClickListener(this);
        removeCouponBtn.setOnClickListener(this);
        onlineorderbtn.setOnClickListener(this);
        cashorderbtn.setOnClickListener(this);

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
        pay_cashtxt = view.findViewById(R.id.pay_cashtxt);
        sftunits = view.findViewById(R.id.sft_units);
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

        errortxt = view.findViewById(R.id.error_text);
        manaulhandfee = view.findViewById(R.id.manaul_handfee);
        payordercash = view.findViewById(R.id.payByCashTextView);
        paytotalcash = view.findViewById(R.id.orderToatalecash);
        cashorderbtn = view.findViewById(R.id.cashorderbtn);
        onlineorderbtn = view.findViewById(R.id.onlineOrderbtn);

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
                } else if(paymentType == 1) {
                    initiateRazorpayPayment(order_id);
                }
                 else{   ProgressBarHelper.show(getActivity(), "Placing Order");
                    PlaceOrderFragmentController.getInstance().placeOrderApiCall(createPlaceOrderMap());
                }
                break;

            case R.id. onlineOrderbtn:
                paymentType = 1;

                if (addressModel == null) {
                    showSnackBar("Please Add Address To Proceed");
                    return;
                }
                if (paymentType == 0) {
                    showSnackBar("Please Select Payment Type");
                } else if(paymentType == 1) {
                    onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                    payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
                    cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//                payBycash_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    pay_cashtxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
//
                   onlineorderbtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                   cashorderbtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
                    initiateRazorpayPayment(order_id);
                }
                else{   ProgressBarHelper.show(getActivity(), "Placing Order");

                    PlaceOrderFragmentController.getInstance().placeOrderApiCall(createPlaceOrderMap());
                }
                break;

            case R.id.cashorderbtn:
                paymentType = 2;
                if (addressModel == null) {
                    showSnackBar("Please Add Address To Proceed");
                    return;
                }
                if (paymentType == 0) {
                    showSnackBar("Please Select Payment Type");
                } else if(paymentType == 1) {
                    initiateRazorpayPayment(order_id);
                }
                else{   ProgressBarHelper.show(getActivity(), "Placing Order");
                    cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                    pay_cashtxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
                    onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//                payOnline_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
                    cashorderbtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
                    onlineorderbtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));

                    PlaceOrderFragmentController.getInstance().placeOrderApiCall(createPlaceOrderMap());
                }
                break;


//            case R.id.onlineLayout:
//                String amountSt = isCouponApplied? finalPrice:orderDetailsModel.getTotal();
//                float amt = Float.parseFloat(amountSt);
//
//// Calculate the 15% deduction
//                float amount15 = amt * 0.15f;
//                float remaingAmount = (amt - amount15);
//
//// Format the amount15Percent to 2 decimal places
//                String  formattedAmount15 = String.format("%.2f", amount15 );
//
//
//
//                payOnline_textView.setText("Pay 15% of amount Right now (Online Payment) "+"→ "+"₹ " +formattedAmount15);
//                onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
//                payOnline_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
//                payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
//                cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//                payBycash_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
//                payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
//                paymentType = 1;
//                break;
//            case R.id.cashlayout:
//                String amount = isCouponApplied? finalPrice:orderDetailsModel.getTotal();
//
//                String price = String.valueOf(Integer.parseInt(amount)+100);
//
//                 payordercash.setText("Order fee  ₹ "+amount);
//                 paytotalcash.setText("Toatl cash  ₹ "+price);
//                cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
//                payBycash_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
//                payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
//                onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//                payOnline_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
//                payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
//                paymentType = 2;
//                break;
            case R.id.applyCouponBtn:
                couponText = couponEditText.getEditText().getText().toString();

                if (!validateCoupon()) {
                    return;
                } else {

                    Map<String, String> couponMap = new HashMap<>();
                    couponMap.put("coupon", couponText);
                    // couponMap.put("price",UserSessionManagement.getInstance(getContext()).getSubServicePrice() );
//                    String price = UserSessionManagement.getInstance(getContext()).getServicePrice();
//                   if(UserSessionManagement.getInstance(getContext()).getServicePrice() != null){
//                       price = UserSessionManagement.getInstance(getContext()).getServicePrice();
//                      couponMap.put("price",price);
//
//                    }
//                    else{
//                        price = UserSessionManagement.getInstance(getContext()).getSubServicePrice();
                     couponMap.put("price",orderDetailsModel.getTotal());
                   // couponMap.put("price", result);

//                    }

                    //  couponMap.put("category_id", orderDetailsModel.getCategory_id());
                    couponMap.put("category_id", "20");
                  //  couponMap.put("pincode", pioncode);
                    //  couponMap.put("sub_category_id", orderDetailsModel.getSub_category());
                    couponMap.put("sub_category_id", "32");
                    couponMap.put("extra_charges_price", orderDetailsModel.getExtra_charges_price());
                    couponMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
                  //  couponMap.put("user_id", "1749");
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



//    private void pricinglayout() {
//
//        String amountSt = isCouponApplied? finalPrice:totalprice;
//        float amt = Float.parseFloat(amountSt);
//
//// Calculate the 15% deduction
//        float amount15 = amt * 0.15f;
//        float remaingAmount = (amt - amount15);
//
//// Format the amount15Percent to 2 decimal places
//        String  formattedAmount15 = String.format("%.2f", amount15 );
//
//
//
//        payOnline_textView.setText("Pay 15% of amount Right now  "+"→ "+"₹ " +formattedAmount15);
//        onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
//        payOnline_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
//        payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
//        cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//        payBycash_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
//        payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
//
//
//        String amount = isCouponApplied? finalPrice: this.orderDetailsModel.getTotal();
//
//        String price = String.valueOf(Integer.parseInt(amount)+100);
//
//        payordercash.setText("Order fee  ₹ "+amount);
//        paytotalcash.setText("Toatl cash  ₹ "+price);
//        cashLayoutBtn.setBackground(getResources().getDrawable(R.drawable.green_rectangular_box));
//        payBycash_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));
//        payBycash_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_active, 0, 0, 0);
//        onlineLayoutBtn.setBackground(getResources().getDrawable(R.drawable.grey_rectangle_box));
//        payOnline_textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
//        payOnline_textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_nonactive, 0, 0, 0);
//
//
//    }

    private Map<String, String> createPlaceOrderMap() {
        if (paymentType == 1) {


            float remainingAmount = (amount - amount15Percent);

// Format the amount15Percent to 2 decimal places
        String  formattedAmount15Percent = String.format("%.2f", amount15Percent / 100);

        UserSessionManagement.getInstance(getContext()).setPaidamount(formattedAmount15Percent);


            String reamingprice  = String.format("%2f", remainingAmount/100);
            UserSessionManagement.getInstance(getContext()).setRemainingamount(reamingprice);

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("address", addressModel.getAddressId());
            hashMap.put("payment_mode", paymentType == 1 ? "online" : "cash");
            hashMap.put("coupon_id", isCouponApplied ? couponId : "");
            hashMap.put("coupon_amount", isCouponApplied ? couponAmount : "");
            //  hashMap.put("sub_total", isCouponApplied ? finalPrice : orderDetailsModel.getTotal());
            hashMap.put("sub_total", isCouponApplied ? finalPrice : orderDetailsModel.getTotal());
            hashMap.put("paid_amount", formattedAmount15Percent);
            hashMap.put("remaining_amount",reamingprice);

         //   Toast.makeText(getContext(),"price"+String.valueOf(formattedAmount15Percent),Toast.LENGTH_LONG).show();
            //  hashMap.put("sub_total", isCouponApplied ? finalPrice : storedResult);
            hashMap.put("id", order_id);
            return hashMap;

        } else {

            String finalPriceString = finalPrice != null ? finalPrice : "0";
            String resultString = orderDetailsModel.getTotal() != null ? orderDetailsModel.getTotal() : "0";

            int finalPriceInt = 0;
            int resultInt = 0;

            try {
                finalPriceInt = Integer.parseInt(finalPriceString);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // or handle it appropriately
            }

            try {
                resultInt = Integer.parseInt(resultString);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // or handle it appropriately
            }

            String fp = String.valueOf(finalPriceInt + 100);
            String res = String.valueOf(resultInt + 100);

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("address", addressModel.getAddressId());
            hashMap.put("payment_mode", paymentType == 1 ? "online" : "cash");
            hashMap.put("coupon_id", isCouponApplied ? couponId : "");
            hashMap.put("coupon_amount", isCouponApplied ? couponAmount : "");
            //  hashMap.put("sub_total", isCouponApplied ? finalPrice : orderDetailsModel.getTotal());
            hashMap.put("sub_total", isCouponApplied ? fp : res);
            //  hashMap.put("paid_amount", String.valueOf(amount15Percent));

            //  hashMap.put("sub_total", isCouponApplied ? finalPrice : storedResult);
            hashMap.put("id", order_id);
            return hashMap;
        }

    }


    private boolean validateCoupon() {
        String totalPriceString = total_price.getText().toString();
        int totalPrice = Integer.parseInt(totalPriceString.replaceAll("[^0-9]", ""));


        if(totalPrice >= 500){

            if (couponText.isEmpty()) {
                couponEditText.setError("Please Enter Your Coupon Code");
                return false;
            } else {
                couponEditText.setError(null);
                return true;
            }
        }else{
            showSnackBar("Total price must be greater than or equal to 500 to apply the coupon.");
            return false;
        }
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


                            String amountSt = isCouponApplied? finalPrice:orderDetailsModel.getTotal();
                            float amt = Float.parseFloat(amountSt);

// Calculate the 15% deduction
                            float amount15 = amt * 0.15f;
                            float remaingAmount = (amt - amount15);

// Format the amount15Percent to 2 decimal places
                            String  formattedAmount15 = String.format("%.2f", amount15 );



                            payOnline_textView.setText("Avoid manual handling fee and pay 15% advance now "+"→ "+"₹ " +formattedAmount15);
                            payOnline_textView.setTextColor(getResources().getColor(R.color.quantum_googgreen));

                            String amount = isCouponApplied? finalPrice:orderDetailsModel.getTotal();

                            String price = String.valueOf(Integer.parseInt(amount)+100);
                            String fees = "&#8226"+" Order amount → ₹  <font color='#FFA500'><big>" + amount + "</big></font>";


                            payordercash.setText(Html.fromHtml(fees));

                            String formattedPrice ="&#8226"+" Total amount → ₹  <font color='#FFA500'><big>" + price + "</big></font>";

                            // Set the formatted string to the TextView
                            paytotalcash.setText(Html.fromHtml(formattedPrice));



                            // Toast.makeText(getContext(),"price"+finalPrice,Toast.LENGTH_SHORT).show();
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

        String sft = orderDetailsModel.getQuantity();

      //  Toast.makeText(getContext(), " sftunits"+sft, Toast.LENGTH_SHORT).show();

        totalprice = orderDetailsModel.getTotal();
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
        if (orderDetailsModel.getExtra_charges_title().contains("  ")|| orderDetailsModel.getExtra_charges_title().isEmpty()){
            extra_charges_title.setText("Extra Charges");
        }else {
            extra_charges_title.setText(orderDetailsModel.getExtra_charges_title());
        }
        if(orderDetailsModel.getQuantity().equals("1")){
            sftunits.setVisibility(View.GONE);
        }
        else{
            sftunits.setVisibility(View.VISIBLE);
            sftunits.setText("Selected "+sft+" sft units");
        }

        // TODO error - java.lang.IllegalStateException: Fragment PlaceOrderFragment{520f37e} not attached to a context.
        if (getActivity() != null) {

//            double serviceprice = Double.parseDouble(orderDetailsModel.getServicePrice());
//            double  loctprice = Double.parseDouble(locationprice);
//
//            double sum = serviceprice + loctprice;
//
//            long roundedSum = Math.round(sum);
//
//result = String.valueOf(roundedSum);

//
//
                           String amountSt = isCouponApplied? finalPrice:orderDetailsModel.getTotal();
                float amt = Float.parseFloat(amountSt);

// Calculate the 15% deduction
                float amount15 = amt * 0.15f;
                float remaingAmount = (amt - amount15);

// Format the amount15Percent to 2 decimal places
                String  formattedAmount15 = String.format("%.2f", amount15 );



                String onlinetxt ="Avoid manual handling fee and"+"\n"+" Pay 15% advance now "+"→ " +"<font color='#0000'><big>"+"₹ "+formattedAmount15 + "</big></font>";
            payOnline_textView.setText(Html.fromHtml(onlinetxt));

            String amount = isCouponApplied? finalPrice:orderDetailsModel.getTotal();

        String price = String.valueOf(Integer.parseInt(amount)+100);
        String fees = "&#8226"+" Order amount →    ₹ <font color='#FFA500'><big>" + amount + "</big></font>";


        payordercash.setText(Html.fromHtml(fees));

            String formattedPrice = "&#8226"+" Total amount →    ₹  <font color='#FFA500'><big>" + price + "</big></font>";

            // Set the formatted string to the TextView
            paytotalcash.setText(Html.fromHtml(formattedPrice));


            orderPrice_textView.setText(getActivity().getResources().getString(R.string.indian_rupee)+orderDetailsModel.getServicePrice());
//          if(UserSessionManagement.getInstance(getContext()).getServicePrice() != null){
//              price = UserSessionManagement.getInstance(getContext()).getServicePrice();
//              orderPrice_textView.setText(getActivity().getResources().getString(R.string.indian_rupee)+ UserSessionManagement.getInstance(getContext()).getServicePrice());
//          }
//          else{
//              price = UserSessionManagement.getInstance(getContext()).getSubServicePrice();
//              orderPrice_textView.setText(getActivity().getResources().getString(R.string.indian_rupee)+ price);
//          }
        }

        if (getActivity() != null) {
            extra_charges_price.setText(getActivity().getResources().getString(R.string.indian_rupee) + orderDetailsModel.getExtra_charges_price());
        }

        if (getActivity() != null  ) {
//            if(UserSessionManagement.getInstance(getContext()).getServicePrice() != null){
//                price = UserSessionManagement.getInstance(getContext()).getServicePrice();
//                total_price.setText(getActivity().getResources().getString(R.string.indian_rupee)+ price);
//            }
//            else{
//                price = UserSessionManagement.getInstance(getContext()).getSubServicePrice();
//                total_price.setText(getActivity().getResources().getString(R.string.indian_rupee)+ price);
//            }
           total_price.setText(getActivity().getResources().getString(R.string.indian_rupee) +orderDetailsModel.getTotal());
        }
        this.orderDetailsModel = orderDetailsModel;
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onPlaceOrderSuccess(String orderId) {
        Log.d("placeOrder", "triggere2" + orderId);
        ProgressBarHelper.dismiss(getActivity());
//        if (paymentType == 1) { // If payment type is online
//            initiateRazorpayPayment(orderId);
//            currentOrderId = orderId;
//        } else {
            addToFragmentContainer(SelectProviderFragment.newInstance(orderId), true, SELECT_PROVIDER_FRAGMENT_TAG);
 //       }

//        Log.d("placeOrder", "triggere2" + orderId);
//        ProgressBarHelper.dismiss(getActivity());
//        callOrderNotifications();
//        addToFragmentContainer(SelectProviderFragment.newInstance(orderId), true, SELECT_PROVIDER_FRAGMENT_TAG);
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


    private void initiateRazorpayPayment(String order_id){

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_ZdGjJKZdukGGzL");

       //  checkout.setKeyID("rzp_live_ZdGjJKZdukGGzL");
        //checkout.setKeyID("rzp_test_B54BlMynixkzHI");
        String amountStr = isCouponApplied? finalPrice:totalprice;

             amount = Float.parseFloat(amountStr) * 100;

// Calculate the 15% deduction
        amount15Percent = amount * 0.15f;

     //   Toast.makeText(getContext(), "  f"+amount15Percent, Toast.LENGTH_LONG).show();


        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            // options.put("order_id", order_id);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("description", order_id);
            options.put("currency", "INR");

            // Determine the amount
            // Calculate 15%
            int amountInPaise = Math.round(amount15Percent);

            options.put("amount", amountInPaise);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", contact_no);

            options.put("prefill", preFill);

            checkout.open(getActivity(), options);


        } catch (Exception e) {
            Log.e("Razorpay", "Error in starting Razorpay Checkout", e);
            //errortxt.setText("msg"+e.getMessage());
        }




    }


    //
//    @Override
//    public void onPaymentSuccess(String s) {
//        try {
//            Log.d("Razorpay", "Payment successful, ID: " + s);
//            Log.d("Razorpay", "Navigating to SelectProviderFragment with orderId: " + currentOrderId);
//            Toast.makeText(getContext(), "Payment successful", Toast.LENGTH_SHORT).show();
//            addToFragmentContainer(SelectProviderFragment.newInstance(currentOrderId), true, SELECT_PROVIDER_FRAGMENT_TAG);
//        } catch (Exception e) {
//            Log.e("Razorpay", "E in onPaymentSuccess: " + e.getMessage(), e);
//            Toast.makeText(getContext(),"error msgg",Toast.LENGTH_SHORT).show();
//            errortxt.setText("msg: " + e.getMessage());
//
//    }
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        Log.d("Razorpay", "Payment failed, Code: " + i + ", Reason: " + s);
//        Toast.makeText(getContext(), "Payment not successful", Toast.LENGTH_SHORT).show();
//        errortxt.setText("reason"+s);
//    }
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

    @Override
    public void onPaymentSuccess(String s) {
        //    addToFragmentContainer(SelectProviderFragment.newInstance(currentOrderId), true, SELECT_PROVIDER_FRAGMENT_TAG);

//        Toast.makeText(getContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
//
//        SelectProviderFragment paymentSuccessFragment = new SelectProviderFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.customer_fragments_container, paymentSuccessFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
        ProgressBarHelper.show(getActivity(), "Placing Order");
        PlaceOrderFragmentController.getInstance().placeOrderApiCall(createPlaceOrderMap());


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(), "Payment not successful", Toast.LENGTH_SHORT).show();
//        errortxt.setText("reason"+s);
    }


}
