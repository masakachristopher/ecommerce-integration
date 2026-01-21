package com.apache.camel.ecommerce_integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvRecord(separator = ",", skipFirstLine = true)
public class CsvOrder {

    @DataField(pos = 1)
    private String orderRef;

    @DataField(pos = 2)
    private String customer;

    @DataField(pos = 3)
    private String sku;

    @DataField(pos = 4)
    private Integer quantity;

    @DataField(pos = 5)
    private String currency;

    @DataField(pos = 6)
    private String payment;

    @DataField(pos = 7)
    private String paymentTerms;

    @DataField(pos = 8)
    private String shipCity;

    @DataField(pos = 9)
    private String shipCountry;
}
