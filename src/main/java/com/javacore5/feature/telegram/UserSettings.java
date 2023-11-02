package com.javacore5.feature.telegram;

import com.javacore5.feature.currency.Currency;
import lombok.Data;

@Data
public class UserSettings {


    private int singsAfterComma;
    private Currency currency;
    private String bank;
    private int timeToCreate;


}
