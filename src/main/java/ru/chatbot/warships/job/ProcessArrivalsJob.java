package ru.chatbot.warships.job;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.chatbot.warships.controller.TradeController;
import ru.chatbot.warships.controller.TravelController;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class ProcessArrivalsJob extends QuartzJobBean {

    @Autowired
    private TravelController travelController;

    public void setTravelController(TravelController travelController) {
        this.travelController = travelController;
    }

    @Autowired
    private TradeController tradeController;

    public void setTradeController(TradeController tradeController) {
        this.tradeController = tradeController;
    }

    @Override
    public void executeInternal(JobExecutionContext context) {
        travelController.processTravelArrivals();

        tradeController.processTradeArrivals();
    }
}
