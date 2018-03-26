package ru.chatbot.warships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Travel;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.List;

public class TravelController {
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

    public void processTravelArrivals() {
        List<Travel> arrivedTravelers = voyageService.startHandlingArrivedTravelers();
        for (Travel voyage : arrivedTravelers) {
            SendMessage message = this.processTravelArrival(voyage);
            try {
                warshipsBot.sendMessage(message);
            } catch (TelegramApiException e) {
                System.out.println(e);
            }
        }
        voyageService.finishHandlingArrivedTravelers();
    }

    private SendMessage processTravelArrival(Travel voyage) {
        Player player = playerService.getPlayer(voyage.getPlayerId());
        Port port = portService.getPort(voyage.getDestination());
        ReplyKeyboardMarkup keyboard = markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE"));
        SendMessage message;
        if (playerService.arrive(player, port)) {
            message = Message.makeReplyMessage(player.getChatId(), Message.getArrivalMessage(port),
                    keyboard);
        } else {
            message = Message.makeReplyMessage(player.getChatId(), Message.getPortTakenBeforeArrivalMessage(port),
                    keyboard);
        }
        return message;
    }
}
