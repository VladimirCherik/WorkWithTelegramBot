package com.javacore5.feature.ui;

import com.javacore5.feature.currency.Currency;
import com.javacore5.feature.currency.CurrencyService;

import java.util.List;

public class PrettyPrintCurrencyService {
    public String convert (Currency currency, float[] rates, String afterComma, int namber){

        String[] banksNames = {"PrivatBank", "MonoBank", "NBU"};

        String template = " The rate in ${Bank}: ${USD}/UAH \n" +
                " buy: ${buy}\n" +
                " sale: ${sale}";


        int aC = Integer.parseInt(afterComma);
        float roundedBuy;
        float roundedSale;

        if(aC == 2){
            roundedBuy =  Math.round(rates[0] * 100d) / 100f;
            roundedSale =  Math.round(rates[1] * 100d) / 100f;
        } else if(aC == 3){
            roundedBuy =  Math.round(rates[0] * 1000d) / 1000f;
            roundedSale =  Math.round(rates[1] * 1000d) / 1000f;
        } else {
            roundedBuy =  Math.round(rates[0] * 10000d) / 10000f;
            roundedSale =  Math.round(rates[1] * 10000d) / 10000f;
        }

        return template.replace("${USD}", currency.name() )
                .replace("${buy}", roundedBuy + "")
                .replace("${sale}", roundedSale + "")
                .replace("${Bank}", banksNames[namber]);

    }
}
