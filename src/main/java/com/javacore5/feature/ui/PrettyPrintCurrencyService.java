package com.javacore5.feature.ui;

import com.javacore5.feature.currency.Currency;

public class PrettyPrintCurrencyService {
    public String convert (double rate, Currency currency){

        String template = "Rate ${currency} => UAH = ${rate}";


        float rounded =  Math.round(rate * 100d) / 100f;

        return template
                .replace("${currency}", currency.name())
                .replace("${rate}", rounded + "");

    }
}
