package com.colourmoon.gobuddy.model;

public class CustomerJobModel {

    private String jobId;
    private String orderId;
    private String serviceDate;
    private String serviceTime;
    private String providerId;
    private String serviceTitle;
    private String subServiceTitle;
    private String providerProfileImage;
    private String providerName;
    private String rating;
    private String reviewCount;
    private String totalAmount;
    private String extraAmount;
    private String orderStatus;
    private String isJobCompletedStatus;
    private String paymentMode;
    private String isFavourite;
    private String comments;



   private String customer_confrim;
    private String paid_amount;
    private String remaining_amount;

    public CustomerJobModel(String jobId, String orderId, String serviceDate, String serviceTime, String providerId,
                            String serviceTitle, String subServiceTitle, String providerProfileImage, String providerName,
                            String rating, String reviewCount, String orderStatus, String isJobCompletedStatus,
                            String paymentMode, String totalAmount, String paid_amount, String remaining_amount, String comments) {
        this.jobId = jobId;
        this.orderId = orderId;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.providerId = providerId;
        this.serviceTitle = serviceTitle;
        this.subServiceTitle = subServiceTitle;
        this.providerProfileImage = providerProfileImage;
        this.providerName = providerName;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.orderStatus = orderStatus;
        this.isJobCompletedStatus = isJobCompletedStatus;
        this.paymentMode = paymentMode;
        this.totalAmount = totalAmount;
        this.paid_amount = paid_amount;
        this.remaining_amount = remaining_amount;
        this.comments = comments;
    }

    public CustomerJobModel(String id, String order_id, String service_date, String service_time, String provider_id, String stitle, String sstitle, String provider_profile, String provider_name, String rating, String review_count, String total_amount, String extra_amount,String customer_confirm, String payment_mode, String favourite,String orderStatus) {

        this.jobId = id;
        this.orderId = order_id;
        this.serviceDate = service_date;
        this.serviceTime = service_time;
        this.providerId = provider_id;
        this.serviceTitle = stitle;
        this.subServiceTitle = sstitle;
        this.providerProfileImage = provider_profile;
        this.providerName = provider_name;
        this.rating = rating;
        this.reviewCount = review_count;
        this.paymentMode = payment_mode;
        this.totalAmount = total_amount;
        this.extraAmount  = extra_amount ;
        this.customer_confrim = customer_confirm;
        this.isFavourite = favourite;
        this.orderStatus = orderStatus;


    }

//    public CustomerJobModel(String jobId, String orderId, String serviceDate, String serviceTime, String providerId, String serviceTitle,
//                            String subServiceTitle, String providerProfileImage, String providerName, String rating, String reviewCount,
//                            String totalAmount, String extraAmount, String orderStatus, String isJobCompletedStatus,
//                            String paymentMode, String isFavourite,String comments ) {
//        this.jobId = jobId;
//        this.orderId = orderId;
//        this.serviceDate = serviceDate;
//        this.serviceTime = serviceTime;
//        this.providerId = providerId;
//        this.serviceTitle = serviceTitle;
//        this.subServiceTitle = subServiceTitle;
//        this.providerProfileImage = providerProfileImage;
//        this.providerName = providerName;
//        this.rating = rating;
//        this.reviewCount = reviewCount;
//        this.totalAmount = totalAmount;
//        this.extraAmount = extraAmount;
//        this.orderStatus = orderStatus;
//        this.isJobCompletedStatus = isJobCompletedStatus;
//        this.paymentMode = paymentMode;
//        this.isFavourite = isFavourite;
//        this.comments = comments;
//
//
//
//    }




    public String getIsFavourite() {
        return isFavourite;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getJobId() {
        return jobId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public String getSubServiceTitle() {
        return subServiceTitle;
    }

    public String getProviderProfileImage() {
        return providerProfileImage;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getRating() {
        return rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getIsJobCompletedStatus() {
        return isJobCompletedStatus;
    }

    public String getComments() {
        return comments;
    }


    public String getPaid_amount() {
        return paid_amount;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

}
