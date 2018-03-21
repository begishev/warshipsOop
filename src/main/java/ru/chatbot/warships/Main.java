package ru.chatbot.warships;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        ApplicationContext context =
                new ClassPathXmlApplicationContext("beans.xml");

        try {
            WarshipsBot warshipsBot = (WarshipsBot) context.getBean("warshipsBot");
            botsApi.registerBot(warshipsBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
