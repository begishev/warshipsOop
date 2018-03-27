package ru.chatbot.warships.controller;

import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

public interface Processor {
    public List<SendMessage> process();
}
