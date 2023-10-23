package com.javacore5.feature.telegram.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelpCommand extends BotCommand {

    public HelpCommand() {
        super("help", "Help Command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        SendMessage helpText = new SendMessage();
        helpText.setText("The Bot can help you !!");
        helpText.setChatId(Long.toString(chat.getId()));
        try {
            absSender.execute(helpText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
