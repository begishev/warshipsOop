package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.chatbot.warships.entity.*;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.ShipService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.regex.Pattern;

public class BuyShipHandler implements Handler {
    private Pattern tradePattern = Pattern.compile("\\/buy_ship_(\\d)+");

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
    private ShipService shipService;

    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
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
                markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP"));

        Integer userId = update.getMessage().getFrom().getId();
        Player player = playerService.getPlayer(userId);
        Voyage voyage = voyageService.getVoyage(player);
        Ship oldShip = shipService.getEmployedShip(player.getId());

        if (voyage != null) {
            Port destination = portService.getPort(voyage.getDestination());
            return Message.makeReplyMessage(update, Message.getAlreadyVoyaging(destination), keyboardMarkup);
        }
        Long shipTypeId = Long.valueOf(update.getMessage().getText().substring(10));
        ShipType shipType = shipService.getShipType(shipTypeId);
        if (shipType.getPrice() <= player.getGold()) {
            shipService.createShip("defaultName", player.getId(), shipTypeId, oldShip.getLocationId());
            Ship newShip = shipService.getEmployedShip(player.getId());
            return Message.makeReplyMessage(update, Message.getShipBoughtMessage(newShip), keyboardMarkup);
        } else {
            return Message.makeReplyMessage(update, Message.getNotEnoughMoneyMessage(), keyboardMarkup);
        }
    }
}
