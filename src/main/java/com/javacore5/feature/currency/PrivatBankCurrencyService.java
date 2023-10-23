package com.javacore5.feature.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PrivatBankCurrencyService implements CurrencyService {

    public static void main(String[] args) {

        System.out.println("new PrivatBankCurrencyService().getRate(Currency.UAH) = "
                + new PrivatBankCurrencyService().getRate(Currency.UAH));
    }

    @Override
    public double getRate(Currency currency) {

        String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
//        String url = "https://api.monobank.ua/bank/currency";
        
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
//        System.out.println("json = " + json);
//        return 0;
        Type typeToken = TypeToken.getParameterized(List.class, CurrencyItem.class)
                .getType();
        List<CurrencyItem> currencyItems = new Gson().fromJson(json, typeToken);


        return currencyItems.stream()
                .filter(it -> it.getCcy() == currency)
                .filter(it -> it.getBase_ccy() == Currency.UAH)
                .map(CurrencyItem::getBuy)
                .findFirst()
                .orElseThrow();
    }
}
