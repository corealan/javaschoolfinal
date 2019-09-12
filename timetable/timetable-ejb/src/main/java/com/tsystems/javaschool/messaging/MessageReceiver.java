package com.tsystems.javaschool.messaging;

import com.tsystems.javaschool.data.TimetableStorage;
import com.tsystems.javaschool.rest.RestClient;
import org.jboss.annotation.ejb.ResourceAdapter;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.List;

@Stateless(name = "receiver")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination", propertyValue="timetable-topic"),
        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
@ResourceAdapter("activemq-rar-5.15.9.rar")
public class MessageReceiver implements MessageListener {
    @Inject
    private TimetableStorage timetableStorage;
    @Inject
    private RestClient restClient;

    public MessageReceiver() {
    }
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            String[] stations = null;
            try {
                stations = ((TextMessage) message).getText().trim().split("#");
            }catch (JMSException e) {
                e.printStackTrace();
            }
            List<String> stationList = Arrays.asList(stations);
            for(String station : stationList){
                if(!timetableStorage.getStations().contains(station)){
                    timetableStorage.addStation(station);
                }
                timetableStorage.setTimetable(station, restClient.getTimetable(station));
            }
        }

    }
}
