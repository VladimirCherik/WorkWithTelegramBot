package com.javacore5.feature.telegram;

import com.javacore5.feature.currency.Currency;
import com.javacore5.feature.currency.CurrencyService;
import com.javacore5.feature.currency.PrivatBankCurrencyService;
import com.javacore5.feature.telegram.command.HelpCommand;
import com.javacore5.feature.telegram.command.StartCommand;
import com.javacore5.feature.ui.PrettyPrintCurrencyService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {
    private CurrencyService currencyService;
    private PrettyPrintCurrencyService prettyPrintCurrencyService;

    public CurrencyTelegramBot() {
        currencyService = new PrivatBankCurrencyService();
        prettyPrintCurrencyService = new PrettyPrintCurrencyService();

        register(new StartCommand());
        register(new HelpCommand());
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            String callBackQuery = update.getCallbackQuery().getData();

            Currency currency = Currency.valueOf(callBackQuery);

            double currencyRate = currencyService.getRate(currency);

            String prettyText = prettyPrintCurrencyService.convert(currencyRate, currency);

            SendMessage responseMessage = new SendMessage();
            responseMessage.setText(prettyText);
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            responseMessage.setChatId(Long.toString(chatId));
            try {
                execute(responseMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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
//        System.out.println("Non-command here");
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
