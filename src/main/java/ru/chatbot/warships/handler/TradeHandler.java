package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Voyage;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.regex.Pattern;

public class TradeHandler implements Handler {
    private Pattern tradePattern = Pattern.compile("\\/trade_(\\d)+");

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

    @Autowired
    private ReplyKeyboardMarkupFactory markupFactory;

    public void setMarkupFactory(ReplyKeyboardMarkupFactory markupFactory) {
        this.markupFactory = markupFactory;
    }

    @Override
    public boolean matchCommand(Update update) {
        return tradePattern.matcher(update.getMessage().getText()).matches();
    }

    @Override
    public SendMessage handle(Update update) {
        ReplyKeyboardMarkup keyboardMarkup =
                markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE"));

        Integer userId = update.getMessage().getFrom().getId();
        Player player = playerService.getPlayer(userId);
        Voyage voyage = voyageService.getVoyage(player);
        if (voyage != null) {
            Port destination = portService.getPort(voyage.getDestination());
            return Message.makeReplyMessage(update, Message.getAlreadyVoyaging(destination), keyboardMarkup);
        }
        Integer destinationId = Integer.valueOf(update.getMessage().getText().substring(7));
        Port port = portService.getPort(destinationId);
        if (playerService.getPlayerLocation(player.getId()).equals(port.getId())) {
            return Message.makeReplyMessage(update, Message.getAlreadyHereMessage(port), keyboardMarkup);
        }
        if (port == null) {
            return Message.makeReplyMessage(update, Message.getNoSuchPortMessage(), keyboardMarkup);
        }
        if (!port.getOwner().equals(player.getTeam())) {
            return Message.makeReplyMessage(update, Message.getTradeEnemyPort(), keyboardMarkup);
        }

        try {
            voyageService.createTrade(player, player, playerService.getPlayerLocation(player.getId()), destinationId);
            return Message.makeReplyMessage(update, Message.getTradeStartedMessage(), keyboardMarkup);
        } catch (IllegalArgumentException e) {
            return Message.makeReplyMessage(update, Message.getSorryMessage());
        }
    }
}
