package featureFSM.telegram;


import featureFSM.fsm.StateMachine;
import featureFSM.fsm.StateMachineListener;
import featureFSM.telegram.command.StartCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.*;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private Map<String, StateMachine> stateMachines;
    private ScheduledExecutorService scheduledExecutorService;

    public CurrencyTelegramBot() {
        register(new StartCommand());

        stateMachines = new ConcurrentHashMap<>();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            String chatId = Long.toString(update.getMessage().getChatId());

            if(!stateMachines.containsKey(chatId)){
                StateMachine fsm = new StateMachine();

                fsm.addListener(new MessageListener(chatId));

                stateMachines.put(chatId, fsm);
            }

            String text = update.getMessage().getText();
            stateMachines.get(chatId).handle(text);

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
    private class MessageListener implements StateMachineListener{
        private String chatId;

        public MessageListener(String chatId) {

            this.chatId = chatId;
        }

        @Override
        public void onSwitchedToWaitForMessage() {

            sendText("Write a text for a notice");
        }

        @Override
        public void onSwitchedToWaitForTime() {
            sendText("Got it! After how many seconds would you like to get the reminder?");

        }

        @Override
        public void onMessageAndTimeReceived(String message, int time) {
            sendText("The notice is written. The second left to callback is: " + time);

            scheduledExecutorService.schedule(() -> sendText(message),
                    time,
                    TimeUnit.SECONDS);

        }

        private void sendText(String text){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(chatId);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
