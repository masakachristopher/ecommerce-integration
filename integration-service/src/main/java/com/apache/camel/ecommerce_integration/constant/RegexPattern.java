package com.apache.camel.ecommerce_integration.constant;
import java.util.regex.Pattern;

public class RegexPattern {

    public static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("(?i)Order\\s*#(\\d+)");

}

