package ru.chatbot.warships.resources;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class EasyConstructableKeyboard extends ReplyKeyboardMarkup {
    public EasyConstructableKeyboard(List<String> buttons) {
        int rows = (buttons.size() - 1) / 3 + 1;
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        int buttonCount = buttons.size() / rows;
        for (int i = 0; i < rows; i++) {
            keyboardRows.add(new KeyboardRow());
            for (int j = 0; j < buttonCount; j++) {
                keyboardRows.get(i).add(buttons.get(i * buttonCount + j));
            }
        }
        int lastIndex = (rows - 1) * buttonCount + buttonCount ;
        while (lastIndex < buttons.size()) {
            keyboardRows.get(rows - 1).add(buttons.get(lastIndex));
            lastIndex++;
        }
        this.setKeyboard(keyboardRows);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboad(true);
    }
}