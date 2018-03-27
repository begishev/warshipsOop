package ru.chatbot.warships.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradeHandlerTest {
    TradeHandler tradeHandler = new TradeHandler();

    @Mock
    Update update;
    @Mock
    Message message;

    @Test
    public void testMatchCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/trade_2");
        assertTrue(tradeHandler.matchCommand(update));
        when(message.getText()).thenReturn("/trade_25");
        assertTrue(tradeHandler.matchCommand(update));
        when(message.getText()).thenReturn("/travel_5");
        assertTrue(!tradeHandler.matchCommand(update));
    }
}
