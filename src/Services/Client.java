package Services;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Client {
    private String name;
    private Broker broker;

    public Client(String name, Broker broker) {
        this.name = name;
        this.broker = broker;
    }

    public void subscribeToTopic(String topicName) throws IOException, TimeoutException {
        broker.addClientToTopicRabbitMQ(this, topicName);
    }

    public void sendMessageToUser(String recipient, String message) {
        // Implementar o envio de mensagens diretas offline
    }

    public void sendMessageToTopic(String topicName, String message) {
        // Implementar o envio de mensagens para tópicos
    }
}
