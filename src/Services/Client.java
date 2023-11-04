package Services;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Client {
    private Broker broker;

    public Client(Broker broker) {
        this.broker = broker;
    }

    public void subscribeToTopic(String queueName) throws IOException, TimeoutException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("\n**** Mensagem recebida: " + message +" ****");
        };
        broker.getConfigMQ().getChannelRabbitMQ().basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    public void sendMessageToUser(String recipient, String message) {
        // Implementar o envio de mensagens diretas offline
    }

    public void sendMessageToTopic(String topicName, String message) {
        // Implementar o envio de mensagens para tópicos
    }
}
