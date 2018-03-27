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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelProcessor implements Processor {

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
        List<Travel> arrivedTravelers = voyageService.startHandlingArrivedTravelers();
        List<SendMessage> messages = new ArrayList<>();
        for (Travel voyage : arrivedTravelers) {
            SendMessage message = this.processTravelArrival(voyage);
            messages.add(message);
        }
        voyageService.finishHandlingArrivedTravelers();
        return messages;
    }

    private SendMessage processTravelArrival(Travel voyage) {
        Player player = playerService.getPlayer(voyage.getPlayerId());
        Port port = portService.getPort(voyage.getDestination());
        ReplyKeyboardMarkup keyboard = markupFactory.produceKeyboardMarkupWithButtons(Arrays.asList("INFO", "VOYAGE", "BUY SHIP"));
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
