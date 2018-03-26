package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.Voyage;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.ShipService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;

public class PlayerInfoHandler implements Handler {
    @Autowired
    private ShipService shipService;

    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

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
        return update.getMessage().getText().equals("INFO");
    }

    @Override
    public SendMessage handle(Update update) {
        Integer userID = update.getMessage().getFrom().getId();
        Player player = playerService.getPlayer(userID);
        Ship ship = shipService.getEmployedShip(userID);
        Voyage voyage = voyageService.getVoyage(player);

        if (voyage == null) {
            Port port = portService.getPort(ship.getLocationId());
            return Message.makeReplyMessage(update, Message.getInfoMessage(player, ship, port),
                    markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP")));
        } else {
            Port port = portService.getPort(voyage.getDestination());
            return Message.makeReplyMessage(update, Message.getInfoMessage(player, ship, voyage, port),
                    markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP")));
        }


    }
}
