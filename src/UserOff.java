import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class UserOff {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connectionRabbitMq = factory.newConnection();
        Channel channelRabbitMQ = connectionRabbitMq.createChannel();


        while (true){
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("\n**** Mensagem recebida: \"" + message +"\" ****");
            };
            channelRabbitMQ.basicConsume("OFFQUEUE", true, deliverCallback, consumerTag -> {});
            Scanner scan = new Scanner(System.in);
            scan.next();
        }
    }
}
