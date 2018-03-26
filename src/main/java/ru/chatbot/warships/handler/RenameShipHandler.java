package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.ShipService;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RenameShipHandler implements Handler {
    private Pattern nicknamePattern = Pattern.compile("\\/rename_ship .*");

    @Autowired
    ShipService shipService;

    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @Autowired
    private ReplyKeyboardMarkupFactory markupFactory;

    public void setMarkupFactory(ReplyKeyboardMarkupFactory markupFactory) {
        this.markupFactory = markupFactory;
    }

    @Override
    public boolean matchCommand(Update update) {
        return nicknamePattern.matcher(update.getMessage().getText()).matches();
    }

    @Override
    public SendMessage handle(Update update) {
        String name = update.getMessage().getText().substring(13);
        Integer playerId = update.getMessage().getFrom().getId();
        shipService.renameShip(playerId, name);
        return Message.makeReplyMessage(update, Message.getChangeShipNameMessage(name),
                markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE")));
    }
}
