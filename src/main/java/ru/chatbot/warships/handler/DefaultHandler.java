package ru.chatbot.warships.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class DefaultHandler implements Handler {
    @Override
    public boolean matchCommand(Update update) {
        return true;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage().setChatId(update.getMessage().getChatId()).setText("Default response");
    }
}
