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
public class TravelHandlerTest {
    TravelHandler travelHandler = new TravelHandler();

    @Mock
    Update update;
    @Mock
    Message message;

    @Test
    public void testMatchCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/travel_1");
        assertTrue(travelHandler.matchCommand(update));
        when(message.getText()).thenReturn("/travel_5");
        assertTrue(travelHandler.matchCommand(update));
        when(message.getText()).thenReturn("hack-hack");
        assertTrue(!travelHandler.matchCommand(update));
    }
}
