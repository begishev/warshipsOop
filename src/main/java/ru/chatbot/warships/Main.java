package ru.chatbot.warships;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();


        try {
            WarshipsBot warshipBot = new WarshipsBot();
            botsApi.registerBot(warshipBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
