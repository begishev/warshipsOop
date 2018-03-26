package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.resources.EasyConstructableKeyboard;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.regex.Pattern;

public class TravelHandler implements Handler {
    private Pattern travelPattern = Pattern.compile("\\/travel_(\\d)+");

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
    private VoyageService voyageService;

    public void setVoyageService(VoyageService voyageService) {
        this.voyageService = voyageService;
    }

    @Override
    public boolean matchCommand(Update update) {
        return travelPattern.matcher(update.getMessage().getText()).matches();
    }

    @Override
    public SendMessage handle(Update update) {

        Integer userId = update.getMessage().getFrom().getId();
        Player player = playerService.getPlayer(userId);
        Integer destinationId = Integer.valueOf(update.getMessage().getText().substring(8));
        Port port = portService.getPort(destinationId);
        ReplyKeyboardMarkup keyboardMarkup = new EasyConstructableKeyboard(Arrays.asList("INFO", "VOYAGE"));
        if (playerService.getPlayerLocation(player.getId()).equals(port.getId())) {
            return Message.makeReplyMessage(update, Message.getAlreadyHereMessage(port), keyboardMarkup);
        }
        if (port == null) {
            return Message.makeReplyMessage(update, Message.getNoSuchPortMessage(), keyboardMarkup);
        }
        if (!port.getOwner().equals(player.getTeam())) {
            return Message.makeReplyMessage(update, Message.getTravelEnemyPort(), keyboardMarkup);
        }
        try {
            voyageService.createTravel(player, playerService.getPlayerLocation(player.getId()), destinationId);
            return Message.makeReplyMessage(update, Message.getTravelStartedMessage());
        } catch (IllegalArgumentException e) {
            return Message.makeReplyMessage(update, Message.getSorryMessage());
        }
    }
}