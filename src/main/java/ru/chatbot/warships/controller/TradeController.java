package ru.chatbot.warships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Trade;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.VoyageService;

import java.util.Arrays;
import java.util.List;

public class TradeController {
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

    public void processTradeArrivals() {
        List<Trade> arrivedTraders = voyageService.startHandlingArrivedTraders();
        for (Trade voyage : arrivedTraders) {
            SendMessage message = this.processTradeArrival(voyage);
            try {
                warshipsBot.sendMessage(message);
            } catch (TelegramApiException e) {
                System.out.println(e);
            }
        }
        voyageService.finishHandlingArrivedTraders();
    }

    private SendMessage processTradeArrival(Trade voyage) {
        Player player = playerService.getPlayer(voyage.getPlayerId());
        Port port = portService.getPort(voyage.getDestination());
        SendMessage message;
        ReplyKeyboardMarkup keyboard = markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE"));
        if (playerService.arrive(player, port)) {
            playerService.giveGold(player, Long.valueOf(voyage.getReward()));
            message = Message.makeReplyMessage(player.getChatId(),
                    Message.getArrivalTradeMessage(port, Long.valueOf(voyage.getReward())),
                    keyboard);
        } else {
            message = Message.makeReplyMessage(player.getChatId(), Message.getPortTakenBeforeArrivalMessage(port),
                    keyboard);
        }
        return message;
    }
}
