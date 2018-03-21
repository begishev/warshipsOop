package ru.chatbot.warships.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public interface Handler {
    boolean matchCommand(Update update);

    SendMessage handle(Update update);
}
