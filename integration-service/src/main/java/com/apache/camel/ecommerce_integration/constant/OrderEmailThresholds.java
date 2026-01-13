package com.apache.camel.ecommerce_integration.constant;

/**
 * Classification thresholds for order email confidence.
 */
public final class OrderEmailThresholds {

    private OrderEmailThresholds() {}

    public static final int STRUCTURED_MIN = 70;
    public static final int SEMI_STRUCTURED_MIN = 40;
}
