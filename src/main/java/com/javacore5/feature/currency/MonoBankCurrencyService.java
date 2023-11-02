package com.javacore5.feature.currency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MonoBankCurrencyService implements CurrencyService{

    public static void main(String[] args) {


            new MonoBankCurrencyService().getRate(Currency.USD);

    }
    @Override
    public float[] getRate(Currency currency) {

        String url = "https://api.monobank.ua/bank/currency";
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
            throw new IllegalStateException("Can't connect to Mono Bank" );
        }
//        HttpRequest build = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .GET()
//                .build();
//        String body = HttpClient.newHttpClient().send(build, HttpResponse.BodyHandlers.ofString()).body();
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        CurrencyItem[] currencyItems1 = gson.fromJson(body, CurrencyItem[].class);
//
//
//        System.out.println("body = " + body);
//        System.out.println("currencyItems1[0] = " + currencyItems1[0]);

//        float aFloat = Arrays.stream(currencyItems1)
//                .filter(it -> it.getCcy() == currency)
//                .filter(it -> it.getBase_ccy() == Currency.UAH)
//                .map(CurrencyItem::getBuy)
//                .findFirst()
//                .get();
//        float bFloat = Arrays.stream(currencyItems1)
//                .filter(it -> it.getCcy() == currency)
//                .filter(it -> it.getBase_ccy() == Currency.UAH)
//                .map(CurrencyItem::getSale)
//                .findFirst()
//                .get();

//        System.out.println(aFloat + " " + bFloat + "!!!!!!!!!!!!!!!!!!!");
//        return new float[]{aFloat, bFloat};


//        Type typeToken = TypeToken.getParameterized(List.class, CurrencyItem.class)
//                .getType();
//        List<CurrencyItem> currencyItems = new Gson().fromJson(json, typeToken);


        int intCurrency;
        if(currency.name().equals("EUR")){
            intCurrency = 978;
        } else {
            intCurrency = 840;
        }
        Type typeToken = TypeToken.getParameterized(List.class, MonoBankClass.class)
                .getType();
        List<MonoBankClass> currencyItems = new Gson().fromJson(json, typeToken);





        float buyFloat = currencyItems.stream()
                .filter(it -> it.getCurrencyCodeA() == intCurrency)
                .filter(it -> it.getCurrencyCodeB() == 980)
                .map(it -> it.rateBuy)
                .findFirst()
                .orElseThrow();
        float saleFloat = currencyItems.stream()
                .filter(it -> it.getCurrencyCodeA() == intCurrency)
                .filter(it -> it.getCurrencyCodeB() == 980)
                .map(it -> it.rateSell)
                .findFirst()
                .orElseThrow();


//        System.out.println("json = " + json);
//        System.out.println("currencyItems.get(0).toString()!! = " + currencyItems.get(0).toString());
////        return null;


        return new float[]{buyFloat, saleFloat};


    }
    @Data
    static class MonoBankClass {

        int currencyCodeA;
        int currencyCodeB;
        int date;
        float rateSell;
        float rateBuy;
        float rateCross;


    }

}
