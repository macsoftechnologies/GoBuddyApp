package com.colourmoon.gobuddy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("locations")
    private List<Location> locations;

    // Getter and setter methods for each field
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    // Inner class representing the structure of each location object
    public static class Location {
        @SerializedName("id")
        private String id;
        @SerializedName("location")
        private String location;
        @SerializedName("status")
        private String status;
        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("updated_date")
        private String updatedDate;

        // Getter and setter methods for each field
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }
    }
}
