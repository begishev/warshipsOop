package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;

import java.util.List;

public class TravelPreparationHandler implements Handler {
    @Autowired
    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    private PortService portService;

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    @Autowired
    private ReplyKeyboardMarkupFactory markupFactory;

    public void setMarkupFactory(ReplyKeyboardMarkupFactory markupFactory) {
        this.markupFactory = markupFactory;
    }

    @Override
    public boolean matchCommand(Update update) {
        return update.getMessage().getText().equals("TRAVEL");
    }

    @Override
    public SendMessage handle(Update update) {
        Integer userId = update.getMessage().getFrom().getId();
        Player player = playerService.getPlayer(userId);
        List<Port> ports = portService.getAllyPorts(playerService.getPlayerLocation(player.getId()), player.getTeam());
        try {
            return Message.makeReplyMessage(update, Message.getTravelPreparationMessage(ports));
        } catch (IllegalArgumentException e) {
            return Message.makeReplyMessage(update, Message.getSorryMessage());
        }
    }
}

