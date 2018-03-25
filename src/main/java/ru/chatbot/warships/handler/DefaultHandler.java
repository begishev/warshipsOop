package ru.chatbot.warships.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.resources.EasyConstructableKeyboard;
import ru.chatbot.warships.resources.Message;

import java.util.Arrays;

public class DefaultHandler implements Handler {
    @Override
    public boolean matchCommand(Update update) {
        return true;
    }

    @Override
    public SendMessage handle(Update update) {
        return Message.makeReplyMessage(update, Message.getCreditsMessage(),
                new EasyConstructableKeyboard(Arrays.asList("INFO", "VOYAGE")));
    }
}
