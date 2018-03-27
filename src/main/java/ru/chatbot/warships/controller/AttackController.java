package ru.chatbot.warships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;
import ru.chatbot.warships.entity.*;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.ShipService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.List;

public class AttackController {
    @Autowired
    private WarshipsBot warshipsBot;

    public void setWarshipsBot(WarshipsBot warshipsBot) {
        this.warshipsBot = warshipsBot;
    }

    @Autowired
    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    private ShipService shipService;

    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
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

    public void processAttackArrivals() {
        List<Attack> arrivedAttackers = voyageService.startHandlingArrivedAttackers();
        for (Attack voyage : arrivedAttackers) {
            SendMessage message = this.processAttackArrival(voyage);
            try {
                warshipsBot.sendMessage(message);
            } catch (TelegramApiException e) {
                System.out.println(e);
            }
        }
        voyageService.finishHandlingArrivedAttackers();
    }

    private SendMessage processAttackArrival(Attack voyage) {
        Player player = playerService.getPlayer(voyage.getPlayerId());
        Port port = portService.getPort(voyage.getDestination());
        SendMessage message;
        ReplyKeyboardMarkup keyboard = markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP"));
        Ship ship = shipService.getEmployedShip(player.getId());
        Integer power = port.getPower();
        if (ship.getPower() > power) {
            port.setOwner(player.getTeam());
            if (power > 0) {
                port.setPower(power - 1);
            }
            portService.capturePort(port);
            playerService.arrive(player, port);
            Long reward = new Long(power * 50);
            playerService.giveGold(player, reward);

            message = Message.makeReplyMessage(player.getChatId(), Message.getAttackSuceededMessage(reward),
                    keyboard);
        } else {
            Long penalty = player.getGold() / 2;
            playerService.giveGold(player, -1 * penalty);
            message = Message.makeReplyMessage(player.getChatId(), Message.getAttackFailedMessage(penalty),
                    keyboard);
        }
        return message;
    }
}
