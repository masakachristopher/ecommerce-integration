package com.apache.camel.ecommerce_integration.constant;

public class MiscConstants {

    public final String sourceUri = "imaps://{{mail.host}}:{{mail.port}}?" +
                "username={{gmail.username}}&" +
                "password={{gmail.password}}&" +
                "folderName=INBOX&" +
                "unseenOnly=true&" +       // Only process unread emails
                "delete=false&" +         // Don't delete emails after reading
                "consumer.delay=60000";
}
