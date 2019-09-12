package security.messaging;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import security.service.impl.StationServiceImpl;

import javax.jms.*;
import java.net.ConnectException;
/**
 * Class to send messages to provide interaction with timetable
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Component
public class MessageSender {

    @Autowired
    JmsTemplate jmsTemplate;
    private static final Logger log = Logger.getLogger(MessageSender.class);

    public void sendMessage(final String message) {
        jmsTemplate.send(new MessageCreator(){
            public Message createMessage(Session session)  {
                try {
                    return session.createTextMessage(message);
                } catch (JMSException e) {
                    log.error("JMSException: " + e);
                }
                return null;
            }
        });

    }

}