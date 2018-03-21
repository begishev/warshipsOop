package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.entity.Team;
import ru.chatbot.warships.service.PlayerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseTeamHandler implements Handler {
    @Autowired
    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean matchCommand(Update update) {
        return playerService.getPlayer(update.getMessage().getFrom().getId()) == null;
    }

    @Override
    public SendMessage handle(Update update) {
        Integer userID = update.getMessage().getFrom().getId();
        String nickname = update.getMessage().getFrom().getUserName();
        String message = update.getMessage().getText();
        List<String> teamNames = Arrays.stream(Team.values())
                .map(Team::toString)
                .collect(Collectors.toList());
        if (teamNames.contains(message)) {
            Team team = Team.valueOf(message);
            playerService.createPlayer(userID, update.getMessage().getChatId(), nickname, team);
            return new SendMessage().setChatId(update.getMessage().getChatId()).setText("You successfully joined team " + team.toString());
        } else {
            return new SendMessage().setChatId(update.getMessage().getChatId()).setText("Team with this name does not exist");
        }
    }
}
