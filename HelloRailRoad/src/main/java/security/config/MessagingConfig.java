package security.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import java.util.Arrays;
/**
 * Messaging java-config class
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Configuration
public class MessagingConfig {

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    private static final String TIMETABLE_TOPIC = "timetable-topic";

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_URL);
        connectionFactory.setTrustedPackages(Arrays.asList("security"));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate(connectionFactory());
        ActiveMQTopic activeMQTopic = new ActiveMQTopic(TIMETABLE_TOPIC);
        template.setDefaultDestination(activeMQTopic);
        return template;
    }
}
