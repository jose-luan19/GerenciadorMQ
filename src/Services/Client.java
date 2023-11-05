package Services;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

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
            System.out.println("\n**** Mensagem recebida: \"" + message +"\" ****");
            System.out.println("**** Cliente: \"" + name +"\" ****");
        };
        broker.getConfigMQ().getChannelRabbitMQ().basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void sendMessageToUser(String message) throws IOException, TimeoutException {
        broker.getConfigMQ().configRabbitMQ();
        broker.getConfigMQ().getChannelRabbitMQ().exchangeDeclare("OFFLINE","direct");
        broker.getConfigMQ().getChannelRabbitMQ().queueDeclare("OFFQUEUE", false, false, false, null);
        broker.getConfigMQ().getChannelRabbitMQ().queueBind("OFFQUEUE", "OFFLINE", "USER.DIRECT");
        broker.getConfigMQ().getChannelRabbitMQ().basicPublish("OFFLINE","USER.DIRECT",null, message.getBytes());
        System.out.println("MENSAGEM ENVIADA");
        broker.getConfigMQ().closeRabbitMQ();
    }

    public void sendMessageToTopic(String topicName, String key, String message) throws IOException, TimeoutException {
        broker.getConfigMQ().configRabbitMQ();
        broker.getConfigMQ().getChannelRabbitMQ().exchangeDeclare(topicName,"topic");
        broker.getConfigMQ().getChannelRabbitMQ().queueDeclare(topicName+".queue", false, false, false, null);
        broker.getConfigMQ().getChannelRabbitMQ().queueBind(topicName+".queue", topicName, key);
        broker.getConfigMQ().getChannelRabbitMQ().basicPublish(topicName,key,null, message.getBytes());
        System.out.println("MENSAGEM ENVIADA PARA TOPIC");
        broker.getConfigMQ().closeRabbitMQ();
    }
}
