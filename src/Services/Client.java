package Services;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Client {
    private final Broker broker;
    private static String name;

    public Client(Broker broker, String name) {
        this.broker = broker;
        Client.name = name;
    }

    public void subscribeToTopic(String queueName) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("\n**** Mensagem recebida: " + message +" ****");
            System.out.println("**** Cliente: " + name +" ****");
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
