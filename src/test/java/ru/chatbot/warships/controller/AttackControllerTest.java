package ru.chatbot.warships.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.chatbot.warships.entity.*;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;
import ru.chatbot.warships.service.PlayerService;
import ru.chatbot.warships.service.PortService;
import ru.chatbot.warships.service.ShipService;
import ru.chatbot.warships.service.VoyageService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackControllerTest {

    @Mock private PlayerService playerService;
    @Mock private ShipService shipService;
    @Mock private PortService portService;
    @Mock private VoyageService voyageService;
    @Mock private ReplyKeyboardMarkupFactory markupFactory;

    @InjectMocks private AttackProcessor attackController;

    @Before
    public void setUp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack(1, 1, 1, timestamp, timestamp, 0));
        when(voyageService.startHandlingArrivedAttackers()).thenReturn(attacks);
        when(playerService.getPlayer(1)).thenReturn(new Player(1, 1L, "", Team.BRITAIN, 500L));
        when(shipService.getEmployedShip(1)).thenReturn(new Ship(1L, 1, "Ship",
                "BigShip", 5L, 5L, 5L, true, 2));
    }

    @Test
    public void testSuccessfulAttack() {
        when(portService.getPort(1)).thenReturn(new Port(1, "Port", 0, 0, Team.SPAIN, 0));
        List<SendMessage> messages = attackController.process();
        String expected = "Your attack succeeded!\n" +
                "You earn 0 gold as reward";
        Assert.assertEquals(expected, messages.get(0).getText());
    }

    @Test
    public void testSuccessfulAttackWithReward() {
        when(portService.getPort(1)).thenReturn(new Port(1, "Port", 0, 0, Team.SPAIN, 4));
        List<SendMessage> messages = attackController.process();
        String expected = "Your attack succeeded!\n" +
                "You earn 200 gold as reward";
        Assert.assertEquals(expected, messages.get(0).getText());
    }

    @Test
    public void testFailed() {
        when(portService.getPort(1)).thenReturn(new Port(1, "Port", 0, 0, Team.SPAIN, 10));
        List<SendMessage> messages = attackController.process();
        String expected = "Your attack failed, so you paid 250 penalty for lost battle";
        Assert.assertEquals(expected, messages.get(0).getText());
    }
}
