package com.colourmoon.gobuddy;



import com.google.gson.annotations.SerializedName;

public class SingleUserNotificationModel {


        private String status;
        private String message;
        @SerializedName("order_id")
        private String orderId;
        private String text;
        private Provider[] providers;
        private ResponseData[] response;

        // Constructors, getters, and setters...

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Provider[] getProviders() {
            return providers;
        }

        public void setProviders(Provider[] providers) {
            this.providers = providers;
        }

        public ResponseData[] getResponse() {
            return response;
        }

        public void setResponse(ResponseData[] response) {
            this.response = response;
        }
    }

    class Provider {
        private String id;
        private String name;
        private String profile;
        private String token;
        private String email;
        @SerializedName("phone_number")
        private String phoneNumber;
        private String distance;
        private String rating;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        // Constructors, getters, and setters...

        // Define getters and setters for all fields
    }

    class ResponseData {
        private String to;
        private String priority;
        private NotificationData notification;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        // Constructors, getters, and setters...

        // Define getters and setters for all fields
    }

class NotificationData {
    private String title;
    private String body;
    @SerializedName("provider_id")
    private String providerId;
    private String sound;

    // Constructors, getters, and setters...

    public NotificationData() {
        // Default constructor
    }

    public NotificationData(String title, String body, String providerId, String sound) {
        this.title = title;
        this.body = body;
        this.providerId = providerId;
        this.sound = sound;
    }

    // Define getters and setters for all fields

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
