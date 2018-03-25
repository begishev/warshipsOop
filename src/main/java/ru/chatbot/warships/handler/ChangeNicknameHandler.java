package ru.chatbot.warships.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.chatbot.warships.resources.EasyConstructableKeyboard;
import ru.chatbot.warships.resources.Message;
import ru.chatbot.warships.service.PlayerService;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ChangeNicknameHandler implements Handler {
    private Pattern nicknamePattern = Pattern.compile("\\/nickname .*");

    @Autowired
    PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean matchCommand(Update update) {
        return nicknamePattern.matcher(update.getMessage().getText()).matches();
    }

    @Override
    public SendMessage handle(Update update) {
        String nickname = update.getMessage().getText().substring(10);
        Integer playerId = update.getMessage().getFrom().getId();
        playerService.setNickname(playerId, nickname);
        return Message.makeReplyMessage(update, Message.getChangeNicknameMessage(nickname),
                new EasyConstructableKeyboard(Arrays.asList("INFO", "VOYAGE")));
    }
}
