package ru.chatbot.warships.job;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.chatbot.warships.bot.WarshipsBot;
import ru.chatbot.warships.controller.AttackProcessor;
import ru.chatbot.warships.controller.Processor;
import ru.chatbot.warships.controller.TradeProcessor;
import ru.chatbot.warships.controller.TravelProcessor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;


public class ProcessArrivalsJob extends QuartzJobBean {

    @Autowired
    private WarshipsBot warshipsBot;

    public void setWarshipsBot(WarshipsBot warshipsBot) {
        this.warshipsBot = warshipsBot;
    }

    @Autowired
    List<Processor> processors;

    public void setProcessors(List<Processor> processors) {
        this.processors = processors;
    }

    @Override
    public void executeInternal(JobExecutionContext context) {
        for (Processor processor : processors) {
            List<SendMessage> messages = processor.process();
            for (SendMessage message : messages) {
                try {
                    warshipsBot.sendMessage(message);
                } catch (TelegramApiException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
