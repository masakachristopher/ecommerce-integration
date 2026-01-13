package com.apache.camel.ecommerce_integration.helper;

import com.apache.camel.ecommerce_integration.constant.OrderEmailThresholds;
import com.apache.camel.ecommerce_integration.model.OrderEmailType;

/**
 * Value object representing email classification score.
 */
public class OrderEmailScore {

    private int total = 0;

    public void add(int points) {
        total += points;
    }

    public int value() {
        return total;
    }

    public OrderEmailType classify() {
        if (total >= OrderEmailThresholds.STRUCTURED_MIN) {
            return OrderEmailType.STRUCTURED;
        }
        if (total >= OrderEmailThresholds.SEMI_STRUCTURED_MIN) {
            return OrderEmailType.SEMI_STRUCTURED;
        }
        return OrderEmailType.UNSTRUCTURED;
    }
}

