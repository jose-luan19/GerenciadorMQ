package Services;

import Configuration.ConfigMQ;
import org.apache.activemq.ActiveMQSession;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class Broker {
    private Map<String, Queue<String>> queues;
    private Map<String, List<Client>> topics;
    private ConfigMQ configMQ;

    public Broker() {
        configMQ = new ConfigMQ();
        queues = new HashMap<>();
        topics = new HashMap<>();
    }

    public void addQueueActiveMq(String queueName) throws JMSException {
        configMQ.configActiveMQ();
        Destination destination = configMQ.getSessionActvieMQ().createQueue(queueName);
        configMQ.closeActiveMQ();
    }
    public void addQueueRabbitMQ(String queueName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.getChannelRabbitMQ().queueDeclare(queueName, false, false, false, null);
        configMQ.closeRabbitMQ();
    }

    public void removeQueueActiveMQ(String queueName) throws JMSException {
        // Implementar a remoção de uma fila
        configMQ.configActiveMQ();
        if (configMQ.getSessionActvieMQ() instanceof ActiveMQSession activeMQSession) {
//            activeMQSession.deleteQueue(queueName);
        }
        configMQ.closeActiveMQ();
    }
    public void removeQueueRabbitMQ(String queueName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.getChannelRabbitMQ().queueDelete(queueName);
        configMQ.closeRabbitMQ();
    }

    public int getQueueSizeActiveMQ(String queueName) throws JMSException {
        // Implementar para listar a quantidade de mensagens em uma fila
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
        return 0;
    }
    public int getQueueSizeRabbitMQ(String queueName) throws IOException, TimeoutException {
        // Implementar para listar a quantidade de mensagens em uma fila
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        return 0;
    }

    public void createTopicActiveMQ(String topicName) throws JMSException {
        // Implementar a criação de um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
    public void createTopicRabbitMQ(String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a criação de um tópico
    }

    public void removeTopicActiveMQ(String topicName) throws JMSException {
        // Implementar a remoção de um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
    public void removeTopicRabbitMQ(String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a remoção de um tópico
    }

    public void addClientToTopicActiveMQ(Client client, String topicName) throws JMSException {
        // Implementar a adição de um cliente a um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
    public void addClientToTopicRabbitMQ(Client client, String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a adição de um cliente a um tópico
    }
}
