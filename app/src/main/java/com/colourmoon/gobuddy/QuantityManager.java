package com.colourmoon.gobuddy;

import java.util.HashMap;
import java.util.Map;

public class QuantityManager {
    private static Map<Integer, Integer> itemQuantities = new HashMap<>();

    // Method to get the quantity of a specific item by item ID
    public static int getQuantity(int itemId) {
        return itemQuantities.getOrDefault(itemId, 1);  // Default to 1 if no value is found
    }

    // Method to set quantity for a specific item by item ID
    public static void setQuantity(int itemId, int quantity) {
        itemQuantities.put(itemId, quantity);
    }


    public static void resetQuantities() {
        for (Integer key : itemQuantities.keySet()) {
            itemQuantities.put(key, 1);
        }
    }
}

