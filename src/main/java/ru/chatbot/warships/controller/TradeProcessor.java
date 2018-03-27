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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradeProcessor implements Processor {
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

    public List<SendMessage> process() {
        List<Trade> arrivedTraders = voyageService.startHandlingArrivedTraders();
        List<SendMessage> messages = new ArrayList<>();
        for (Trade voyage : arrivedTraders) {
            SendMessage message = this.processTradeArrival(voyage);
            messages.add(message);
        }
        voyageService.finishHandlingArrivedTraders();
        return messages;
    }

    private SendMessage processTradeArrival(Trade voyage) {
        Player player = playerService.getPlayer(voyage.getPlayerId());
        Port port = portService.getPort(voyage.getDestination());
        SendMessage message;
        ReplyKeyboardMarkup keyboard = markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP"));
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
