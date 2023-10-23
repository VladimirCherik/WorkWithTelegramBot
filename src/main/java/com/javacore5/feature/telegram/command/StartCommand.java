package com.javacore5.feature.telegram.command;

import com.javacore5.feature.currency.Currency;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StartCommand extends BotCommand {

    public StartCommand() {
        super("start", "Start command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String text = "what is the kind of currency you would like to know : ";

        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(Long.toString(chat.getId()));

        List<InlineKeyboardButton> buttons = Stream.of(Currency.USD, Currency.EUR, Currency.UAH)
                .map(Enum::name)
                .map(it -> InlineKeyboardButton.builder().text(it).callbackData(it).build())
                .collect(Collectors.toList());

//        InlineKeyboardButton usdButton = InlineKeyboardButton
//                .builder().text("USD").callbackData("USD").build();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup
                .builder()
                .keyboard(Collections.singleton(buttons))
                .build();


//        KeyboardButton usdButton = KeyboardButton.builder().text("USD").build();
//
//        KeyboardRow keyboardRow = new KeyboardRow();
//        keyboardRow.add(usdButton);
//
//        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup
//                .builder().keyboardRow(keyboardRow).build();
        message.setReplyMarkup(keyboard);

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
