package com.javacore5.feature.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javacore5.feature.telegram.CurrencyTelegramBot;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PrivatBankCurrencyService implements CurrencyService {


    public static void main(String[] args) {

        System.out.println("new PrivatBankCurrencyService().getRate(Currency.UAH) = "
                + new PrivatBankCurrencyService().getRate(Currency.USD));
    }

    @Override
    public float[] getRate(Currency currency) {


        String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";

        
        String json = null;
        try {
            json = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to Privat Bank" );
        }

        System.out.println("json = " + json);


        Type typeToken = TypeToken.getParameterized(List.class, CurrencyItem.class)
                .getType();
        List<CurrencyItem> currencyItems = new Gson().fromJson(json, typeToken);

        float buyFloat = currencyItems.stream()
                .filter(it -> it.getCcy() == currency)
                .filter(it -> it.getBase_ccy() == Currency.UAH)
                .map(it -> it.getBuy())
                .findFirst()
                .orElseThrow();
        float saleFloat = currencyItems.stream()
                .filter(it -> it.getCcy() == currency)
                .filter(it -> it.getBase_ccy() == Currency.UAH)
                .map(it -> it.getSale())
                .findFirst()
                .orElseThrow();


        return new float[]{buyFloat, saleFloat};









//        return currencyItems.stream()
//                .filter(it -> it.getCcy() == currency)
//                .filter(it -> it.getBase_ccy() == Currency.UAH)
//                .map(CurrencyItem::getBuy)
//                .findFirst()
//                .orElseThrow();
    }
}
