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
public class AttackHandlerTest {
    AttackHandler attackHandler = new AttackHandler();

    @Mock
    Update update;
    @Mock
    Message message;

    @Test
    public void testMatchCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/attack_1");
        assertTrue(attackHandler.matchCommand(update));
        when(message.getText()).thenReturn("/attack_100");
        assertTrue(attackHandler.matchCommand(update));
        when(message.getText()).thenReturn("attack_1");
        assertTrue(!attackHandler.matchCommand(update));
    }
}
