package com.javacore5.feature.currency;

import com.javacore5.feature.currency.Currency;

import java.io.IOException;
import java.util.List;

public interface CurrencyService {
    float[] getRate(Currency currency);
}
