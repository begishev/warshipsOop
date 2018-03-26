package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;

import java.util.Arrays;

public class DefaultHandler implements Handler {
    @Autowired
    private ReplyKeyboardMarkupFactory markupFactory;

    public void setMarkupFactory(ReplyKeyboardMarkupFactory markupFactory) {
        this.markupFactory = markupFactory;
    }

    @Override
    public boolean matchCommand(Update update) {
        return true;
    }

    @Override
    public SendMessage handle(Update update) {
        return Message.makeReplyMessage(update, Message.getCreditsMessage(),
                markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE")));
    }
}
