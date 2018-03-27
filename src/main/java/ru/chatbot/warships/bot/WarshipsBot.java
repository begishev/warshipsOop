package ru.chatbot.warships.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.handler.Handler;

import java.util.List;

public class WarshipsBot extends TelegramLongPollingBot {
    @Autowired
    private List<Handler> handlers;

    public void setHandlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            try {
                for (Handler handler : handlers) {
                    if (handler.matchCommand(update)) {
                        this.sendMessage(handler.handle(update));
                        break;
                    }
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public String getBotUsername() {
        return "OopHomeworkBot";
    }

    public String getBotToken() {
            return "566498037:AAFJmdQRKXlxtDWASLgYHiLMY3BXyTNONPA";
    }
}
