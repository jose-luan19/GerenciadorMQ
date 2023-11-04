package Services;

import Configuration.ConfigMQ;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Broker {
    private static  Map<String, Client> clients;
    private final ConfigMQ configMQ;

    public Broker() {
        configMQ = new ConfigMQ();
        clients = new HashMap<>();
    }

    public void addQueueRabbitMQ(String queueName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        try{
            configMQ.getChannelRabbitMQ().queueDeclarePassive(queueName).getQueue();
            System.out.println("FILA JÁ EXISTE!");
        }catch (Exception e){
            configMQ.configRabbitMQ();
            configMQ.getChannelRabbitMQ().queueDeclare(queueName, false, false, false, null);
            configMQ.closeRabbitMQ();
        }
    }

    public void removeQueueRabbitMQ(String queueName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        try{
            configMQ.getChannelRabbitMQ().queueDeclarePassive(queueName).getQueue();
            configMQ.getChannelRabbitMQ().queueDelete(queueName);
            configMQ.closeRabbitMQ();
        }catch (Exception e){
            System.out.println("FILA NÃO EXISTE!");
        }
    }

    public void getQueuesMessagesSizeRabbitMQ() throws IOException, TimeoutException {

        JsonArray jsonArray = configMQ.requestApiRabbitMQ("queues");

        int quantidade = 0;
        for (JsonElement jsonElement : jsonArray) {
            quantidade += jsonElement.getAsJsonObject().get("messages").getAsInt();
        }
        System.out.println("Existem "+ quantidade +" mensagens pendentes!");
    }

    public void createTopicRabbitMQ(String topicName) throws IOException, TimeoutException {
        try{
            configMQ.configRabbitMQ();
            configMQ.getChannelRabbitMQ().exchangeDeclarePassive(topicName);
            System.out.println("TOPIC JÁ EXISTE!");
        }catch (Exception e){
            configMQ.configRabbitMQ();
            configMQ.getChannelRabbitMQ().exchangeDeclare(topicName, "topic");
            configMQ.closeRabbitMQ();
        }
    }

    public void removeTopicRabbitMQ(String topicName) throws IOException, TimeoutException {

        try{
            configMQ.configRabbitMQ();
            configMQ.getChannelRabbitMQ().exchangeDeclarePassive(topicName);
            configMQ.getChannelRabbitMQ().exchangeDelete(topicName);
            configMQ.closeRabbitMQ();
        }catch (Exception e){
            System.out.println("TOPIC NÃO EXISTE!");
        }

    }


    public void addClientToTopicRabbitMQ(String clientName, String topicName, String queueName, String key) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        try{
            configMQ.getChannelRabbitMQ().exchangeDeclarePassive(topicName);
        }catch (Exception e){
            System.out.println("TOPIC NÃO EXISTE!");
            return;
        }
        configMQ.getChannelRabbitMQ().queueDeclare(queueName, false, false, false, null);

        configMQ.getChannelRabbitMQ().queueBind(queueName, topicName, key);
        Client newClient = new Client(this);
        clients.put(clientName,newClient);
        newClient.subscribeToTopic(queueName);
//        configMQ.closeRabbitMQ();
    }

    private List<String> requestNamesTopics(){
        List<String> namesTopics = new ArrayList<>();

        JsonArray jsonArray = configMQ.requestApiRabbitMQ("exchanges");

        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.getAsJsonObject().get("type").toString().equals("\"topic\"")){
                namesTopics.add(jsonElement.getAsJsonObject().get("name").toString());
            }
        }
        return namesTopics;
    }

    private List<String> requestNamesQueues(){
        List<String> namesQueues = new ArrayList<>();

        JsonArray jsonArray = configMQ.requestApiRabbitMQ("queues");

        for (JsonElement jsonElement : jsonArray) {
            namesQueues.add(jsonElement.getAsJsonObject().get("name").toString());
        }
        return namesQueues;
    }

    public List<String> getNamesQueues(){
        return requestNamesQueues();
    }
    public List<String> getNamesTopics(){
        return requestNamesTopics();
    }

    public ConfigMQ getConfigMQ() {
        return configMQ;
    }
}
