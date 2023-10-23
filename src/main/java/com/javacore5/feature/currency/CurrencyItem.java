package com.javacore5.feature.currency;

import com.javacore5.feature.currency.Currency;
import lombok.Data;

@Data
public class CurrencyItem {


    private Currency ccy;
    private Currency base_ccy;
    float buy;
    float sale;

}
