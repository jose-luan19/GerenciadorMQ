package Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Session;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfigMQ {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private javax.jms.Connection connectionActiveMQ;
    private Session sessionActvieMQ;
    private Connection connectionRabbitMq;
    private Channel channelRabbitMQ;

    public void configActiveMQ() throws JMSException {
        javax.jms.ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connectionActiveMQ = connectionFactory.createConnection();
        connectionActiveMQ.start();

        /*
         * Criando Session
         */
        sessionActvieMQ = connectionActiveMQ.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void closeActiveMQ() throws JMSException {
        connectionActiveMQ.close();
        sessionActvieMQ.close();
    }

    public void configRabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connectionRabbitMq = factory.newConnection();

        // Abrir um canal
        channelRabbitMQ = connectionRabbitMq.createChannel();
    }

    public void closeRabbitMQ() throws IOException, TimeoutException {
        connectionRabbitMq.close();
        channelRabbitMQ.close();
    }
    public javax.jms.Connection getConnectionActiveMQ() {
        return connectionActiveMQ;
    }

    public Session getSessionActvieMQ() {
        return sessionActvieMQ;
    }

    public Connection getConnectionRabbitMq() {
        return connectionRabbitMq;
    }

    public Channel getChannelRabbitMQ() {
        return channelRabbitMQ;
    }
}
