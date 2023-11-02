package com.javacore5.feature.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class NBUBankCurrencyService implements CurrencyService{
    @Override
    public float[] getRate(Currency currency) {

        String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json";
        String json;
        try {
            json = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to NBU" );
        }

        Type typeToken = TypeToken.getParameterized(List.class, NbuClass.class)
                .getType();
        List<NbuClass> currencyItems = new Gson().fromJson(json, typeToken);

        float buyFloat = currencyItems.stream()
                .filter(it -> it.getCc() == currency)
                .map(NbuClass::getRate)
                .findFirst()
                .orElseThrow();


        return new float[]{buyFloat, 0};

    }
    @Data
    static class NbuClass{
        int r030;
        String txt;
        float rate;
        Currency cc;
        String exchangedate;
    }
}
