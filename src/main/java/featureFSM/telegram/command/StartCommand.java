package featureFSM.telegram.command;

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
        String text = "Hi! I am a Bot Reminder. Push the \"to create reminder\" to start working";

        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(Long.toString(chat.getId()));

        KeyboardButton createNotificationButton = KeyboardButton
                .builder()
                .text("to create reminder")
//                .text("ddd")
                .build();
        KeyboardRow keyboard = new KeyboardRow(Collections.singletonList(createNotificationButton));
        message.setReplyMarkup(ReplyKeyboardMarkup.builder().keyboardRow(keyboard).build());

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
