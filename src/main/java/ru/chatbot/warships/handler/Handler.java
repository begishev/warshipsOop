package ru.chatbot.warships.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 * This interface is responsible for matching incoming commands
 * and producing response based on those commands
 */
public interface Handler {
    /**
     * Determine is this handler is responsible for this particular command
     * @param update entity representing incoming message and all meta-information
     * @return true if the handler is supposed to handle this command
     */
    boolean matchCommand(Update update);

    /**
     * Process message and produce response
     * @param update entity representing incoming message and all meta-information
     * @return message to be sent as response
     */
    SendMessage handle(Update update);
}
