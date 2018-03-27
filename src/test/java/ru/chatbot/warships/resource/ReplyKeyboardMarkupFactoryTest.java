package ru.chatbot.warships.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.chatbot.warships.resources.ReplyKeyboardMarkupFactory;

import java.util.Arrays;

public class ReplyKeyboardMarkupFactoryTest {
    private ReplyKeyboardMarkupFactory factory;

    @Before
    public void setUp() {
        factory = new ReplyKeyboardMarkupFactory();
    }

    @Test
    public void testProduceOneLineKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = factory.produceKeyboardMarkupWithButtons(Arrays.asList("Button1", "Button2"));
        Assert.assertEquals("Button1", keyboardMarkup.getKeyboard().get(0).get(0).getText());
        Assert.assertEquals("Button1", keyboardMarkup.getKeyboard().get(0).get(0).getText());
    }

    @Test
    public void testProduceTwoLineKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = factory.produceKeyboardMarkupWithButtons(Arrays.asList("Button1", "Button2", "Button3", "Button4"));
        Assert.assertEquals("Button1", keyboardMarkup.getKeyboard().get(0).get(0).getText());
        Assert.assertEquals("Button2", keyboardMarkup.getKeyboard().get(0).get(1).getText());
        Assert.assertEquals("Button3", keyboardMarkup.getKeyboard().get(1).get(0).getText());
        Assert.assertEquals("Button4", keyboardMarkup.getKeyboard().get(1).get(1).getText());
    }
}
