package ru.chatbot.warships.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class WarshipsBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            try {
                this.sendMessage(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Test"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public String getBotUsername() {
        return System.getenv().get("BOT_NAME");
    }

    public String getBotToken() {
        return System.getenv().get("BOT_TOKEN");
    }
}
