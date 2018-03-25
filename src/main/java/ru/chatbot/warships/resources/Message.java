package ru.chatbot.warships.resources;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.Team;

import java.util.List;

public class Message {
    private static final String CREDITS = "This game is homework on oop class";

    public static String getCreditsMessage() {
        return CREDITS;
    }

    public static String getJoinTeamMessage(Team team) {
        return "You successfully joined team " + team.toString();
    }

    public static String getSelectTeamMessage(List<Team> teams) {
        return "To select team write one of " + teams.toString();
    }

    public static String getInfoMessage(Player player, Ship ship, Port port) {
        return "Your nickname: " + player.getNickname() + "\n" +
                "Your team: " + player.getTeam().toString() + "\n" +
                "Your gold: " + player.getGold() + "\n" +
                "Your ship:" + "\n" +
                "    Power:   " + ship.getPower().toString() + "\n" +
                "    Speed:   " + ship.getSpeed().toString() + "\n" +
                "    Tonnage: " + ship.getTonnage().toString() + "\n" +
                "    Type: " + ship.getTypeName() + "\n" +
                "You are in " + port.getName();
    }

    public static String getChangeNicknameMessage(String nickname) {
        return "Your nickname successfully changed to " + nickname;
    }

    public static String getChangeShipNameMessage(String name) {
        return "You renamed ship to " + name;
    }

    public static SendMessage makeReplyMessage(Long chatId, String message, ReplyKeyboard keyboard) {
        return new SendMessage().setChatId(chatId).setText(message).setReplyMarkup(keyboard);
    }

    public static SendMessage makeReplyMessage(Update update, String message) {
        return new SendMessage().setChatId(update.getMessage().getChatId()).setText(message);
    }

    public static SendMessage makeReplyMessage(Update update, String message, ReplyKeyboard keyboard) {
        return makeReplyMessage(update, message).setReplyMarkup(keyboard);
    }

    public static SendMessage makeMessage(Long chatId, String message) {
        return new SendMessage().setChatId(chatId).setText(message);
    }

    public static SendMessage makeMessage(Long chatId, String message, ReplyKeyboard keyboard) {
        return makeMessage(chatId, message).setReplyMarkup(keyboard);
    }
}