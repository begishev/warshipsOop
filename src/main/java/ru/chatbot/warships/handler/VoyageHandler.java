package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;

import java.util.Arrays;

public class VoyageHandler implements Handler {
    @Autowired
    private ReplyKeyboardMarkupFactory markupFactory;

    public void setMarkupFactory(ReplyKeyboardMarkupFactory markupFactory) {
        this.markupFactory = markupFactory;
    }

    @Override
    public boolean matchCommand(Update update) {
        return update.getMessage().getText().equals("VOYAGE");
    }

    @Override
    public SendMessage handle(Update update) {
        try {
            return Message.makeReplyMessage(update, Message.getVoyageMessage(),
                    markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("ATTACK", "TRADE", "TRAVEL")));
        } catch (IllegalArgumentException e) {
            return Message.makeReplyMessage(update, Message.getSorryMessage());
        }

    }
}
