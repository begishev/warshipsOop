package ru.chatbot.warships.resources;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import ru.chatbot.warships.entity.Player;
import ru.chatbot.warships.entity.Port;
import ru.chatbot.warships.entity.Ship;
import ru.chatbot.warships.entity.Team;

import java.util.Collection;
import java.util.List;

    /**
     * Created by ospen on 2/3/2017.
     */
    public class Message {
        private static final String SORRY_MESSAGE = "We so sorry, but we can't do anything with that. Really apologize. Please, forgive us.";

        private static final String CREDITS = "This game is homework on oop class";

        public static String getSorryMessage() {
            return SORRY_MESSAGE;
        }

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

        public static String getVoyageMessage() {
            return "Choose what do you want to do" + "\n" +
                    "Attack is the only way to get enemy's port" + "\n" +
                    "Trade is the easiest way to earn some gold" + "\n" +
                    "Travel allows you to move between ports of your team without being in danger";
        }

        public static String getAttackMessage() {
            return "Attack is not implemented yet";
        }

        public static String getNoSuchPortMessage() {
            return "No such port";
        }

        public static String getAttackOwnPort() {
            return "You can't attack your own port";
        }

        public static String getTradeEnemyPort() {
            return "You can't trade with enemy port";
        }

        public static String getTravelEnemyPort() {
            return "You can't travel to enemy port";
        }

        public static String getAttackPreparationMessage(Collection<Port> ports) {
            String msg = "Choose port to attack:" + "\n";
            for (Port port : ports) {
                msg += "To attack port " + port.getName() + " write /attack_" + port.getId() + "\n" +
                        "There are " + port.getDistance() + " miles to travel before attack" + "\n";
            }
            return msg;
        }

        public static String getPortTakenBeforeArrivalMessage(Port port) {
            return "Port" + port.getName() + "was captured by enemy to you decided to turn your ship around";
        }

        public static String getArrivalTradeMessage(Port port, Long gold) {
            return "You arrived to " + port.getName() + "\n" +
                    "You earn " + gold + " gold";
        }

        public static String getAlreadyHereMessage(Port port) {
            return "You are already in port " + port.getName();
        }

        public static String getTradePreparationMessage(Collection<Port> ports) {
            String msg = "Choose port to trade with:" + "\n";
            for (Port port : ports) {
                msg += "To trade with port " + port.getName() + " write /trade_" + port.getId() + "\n" +
                        "There are " + port.getDistance() + " miles to travel, but you will get " + port.getReward() + "\n";
            }
            return msg;
        }

        public static String getArrivalMessage(Port port) {
            return "You arrived to " + port.getName();
        }

        public static String getTravelPreparrationMessage(Collection<Port> ports) {
            String msg = "Choose port to travel to:" + "\n";
            for (Port port : ports) {
                msg += "To travel to port " + port.getName() + " write /travel_" + port.getId() + "\n" +
                        "There are " + port.getDistance() + " miles to travel, and you will get no reward" + "\n";
            }
            return msg;
        }

        public static String getTravelStartedMessage() {
            return "Your travel has been was pios began gon gen";
        }

        public static String getTradeStartedMessage() {
            return "Your trade has been was pios began gon gen";
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