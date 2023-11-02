package com.javacore5.feature.telegram;

import com.javacore5.feature.currency.*;
import com.javacore5.feature.currency.Currency;
import com.javacore5.feature.telegram.command.HelpCommand;
import com.javacore5.feature.telegram.command.StartCommand;
import com.javacore5.feature.ui.PrettyPrintCurrencyService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {
    private CurrencyService privatBank;
    private CurrencyService monoBank;
    private CurrencyService nbuBank;
    private CurrencyService currencyService;
    private PrettyPrintCurrencyService prettyPrintCurrencyService;
    private UserSettings userSettings;

    private static Map<String, String> settings;

    private List<String> listForTime = Arrays.asList("9",
            "10", "11","12" ,"13" ,"14" ,"15" ,"16" ,"17" ,"18", "turn off notifications");


    public CurrencyTelegramBot() {

        privatBank = new PrivatBankCurrencyService();
        monoBank = new MonoBankCurrencyService();
        nbuBank = new NBUBankCurrencyService();

        prettyPrintCurrencyService = new PrettyPrintCurrencyService();
        userSettings = new UserSettings();

        register(new StartCommand());
        register(new HelpCommand());
        settings = new HashMap<>();
    }

    @Override
    public void processNonCommandUpdate(Update update) {



        int namber = 0;
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("to get data")) {

            //Перевірка на вібир банку
            if (settings.containsKey("Banks")) {
                String bank = settings.get("Banks");
                if (bank.equals("PrivatBank")){
                    currencyService = privatBank;
                    namber = 0;
                }
                if (bank.equals("MonoBank")){
                    currencyService = monoBank;
                    namber = 1;
                }
                if (bank.equals("NBU")){
                    currencyService = nbuBank;
                    namber = 2;
                }
            } else {
                currencyService = privatBank;

            }

            //Перевірка на вибір валюти
            Currency currency1 = Currency.valueOf(settings.getOrDefault("Currency", "USD"));

            //Отримання запиту від банку
            float[] rates = currencyService.getRate(currency1);

            //Перевірка на кількість знаків після коми
            String afterTheComma = settings.getOrDefault("The signs after the comma", "4");

            // Фанільне повідомлення , готове для виводу
            String prettyText = prettyPrintCurrencyService.convert(currency1, rates, afterTheComma, namber);



            SendMessage responseMessage = new SendMessage();
            responseMessage.setText(prettyText);
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            responseMessage.setChatId(Long.toString(chatId));

            List<InlineKeyboardButton> buttons = Stream.of("to get data", "settings")
//                .map(Enum::name)
                    .map(it -> InlineKeyboardButton.builder().text(it).callbackData(it).build())
                    .collect(Collectors.toList());
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttons))
                    .build();
            responseMessage.setReplyMarkup(keyboard);
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            try {

                if(settings.containsKey("Time to create notification")){
                    String timeToNotice = settings.get("Time to create notification");
//                    executor.schedule()

                }else {
                    execute(responseMessage);
                }



            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("settings")) {

            SendMessage responseMessage = new SendMessage();
            responseMessage.setText("settings");
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            responseMessage.setChatId(Long.toString(chatId));


            Set<String> strings = Set.of("The signs after the comma",
                    "Banks", "Currency", "Time to create notification");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> keyboard1 = new ArrayList<>();

            for (String buttonName : strings) {

                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
                button.setCallbackData(buttonName);
                keyboard1.add(Arrays.asList(button));
            }
            markup.setKeyboard(keyboard1);
            responseMessage.setReplyMarkup(markup);

            try {
                execute(responseMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("The signs after the comma")) {
                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("The signs after the comma");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                List<String> list = Arrays.asList("2",
                        "3", "4");

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> keyboard1 = new ArrayList<>();

                for (String buttonName : list) {

                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
                    button.setCallbackData(buttonName);
                    keyboard1.add(Arrays.asList(button));
                }
                markup.setKeyboard(keyboard1);
                responseMessage.setReplyMarkup(markup);

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()) {

            if (update.getCallbackQuery().getData().equals("Currency")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("Currency");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                List<Currency> list = Arrays.asList(Currency.EUR, Currency.USD);


                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> keyboard1 = new ArrayList<>();

                for (Currency buttonName : list) {

                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(new String(buttonName.name().getBytes(), StandardCharsets.UTF_8));
                    button.setCallbackData(buttonName.name());
                    keyboard1.add(Arrays.asList(button));
                }
                markup.setKeyboard(keyboard1);
                responseMessage.setReplyMarkup(markup);

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }

        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Banks")) {
                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("Banks");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                List<String> list = Arrays.asList("PrivatBank",
                        "MonoBank", "NBU");

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> keyboard1 = new ArrayList<>();

                for (String buttonName : list) {

                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
                    button.setCallbackData(buttonName);
                    keyboard1.add(Arrays.asList(button));
                }
                markup.setKeyboard(keyboard1);
                responseMessage.setReplyMarkup(markup);

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("PrivatBank")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen PrivatBank");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("Banks", "PrivatBank");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("MonoBank")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen MonoBank");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("Banks", "MonoBank");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("NBU")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen NBU");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("Banks", "NBU");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("USD")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("You chose USD");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));


                settings.put("Currency", "USD");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("EUR")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("You chose EUR");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("Currency", "EUR");

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("2")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen 2 signs after the comma");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("The signs after the comma", "2");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("3")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen 3 signs after the comma");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("The signs after the comma", "3");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (update.getCallbackQuery().getData().equals("4")) {

                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("you have chosen 4 signs after the comma");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

                settings.put("The signs after the comma", "4");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()) {


            if (update.getCallbackQuery().getData().equals("Time to create notification")) {
                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("Time to create notification");
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                responseMessage.setChatId(Long.toString(chatId));

//                listForTime = Arrays.asList("9",
//                        "10", "11","12" ,"13" ,"14" ,"15" ,"16" ,"17" ,"18", "turn off notifications");

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> keyboard1 = new ArrayList<>();

                for (String buttonName : listForTime) {

                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
                    button.setCallbackData(buttonName);
                    keyboard1.add(Arrays.asList(button));
                }
                markup.setKeyboard(keyboard1);
                responseMessage.setReplyMarkup(markup);

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

//            }
//            if(!timeToNotif.isEmpty()){
//                settings.put("Time to create notification", timeToNotif);
//            }


        }
        if(update.hasCallbackQuery()){

                for (String s : listForTime) {

                    if (update.getCallbackQuery().getData().equals(s)) {

                        String text = "${it} is time you will get the notification";
                        String replaced = text.replace("${it}", s);

                        SendMessage responseMessage = new SendMessage();
                        responseMessage.setText(replaced);
                        long chatId = update.getCallbackQuery().getMessage().getChatId();
                        responseMessage.setChatId(Long.toString(chatId));

                        settings.put("Time to create notification", s);
                        try {
                            execute(responseMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }

        }






        if (update.hasMessage()) {
            String text = update.getMessage().getText();
            String response = "You have written - " + text;

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(response);
            sendMessage.setChatId(Long.toString(update.getMessage().getChatId()));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public String getBotUsername() {
        return BotConstants.BOT_NAME;
    }
    @Override
    public String getBotToken() {
        return BotConstants.BOT_TOKEN;
    }



}
