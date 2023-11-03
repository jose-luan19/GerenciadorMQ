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
    private Map<String, Queue<String>> queues;
    private Map<String, List<Client>> topics;
    private ConfigMQ configMQ;

    public Broker() {
        configMQ = new ConfigMQ();
        queues = new HashMap<>();
        topics = new HashMap<>();
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


    public void addClientToTopicRabbitMQ(Client client, String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a adição de um cliente a um tópico
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

    public void addQueueActiveMq(String queueName) throws JMSException {
        configMQ.configActiveMQ();
        Destination destination = configMQ.getSessionActvieMQ().createQueue(queueName);
        configMQ.getSessionActvieMQ().createConsumer(destination);
        configMQ.closeActiveMQ();
    }

    public void removeQueueActiveMQ(String queueName) throws JMSException {
//        // Implementar a remoção de uma fila
//        try {
//            String jmxURL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"; // Ajuste conforme sua configuração
//            JMXServiceURL url = new JMXServiceURL(jmxURL);
//            JMXConnector connector = JMXConnectorFactory.connect(url, null);
//            connector.connect();
//            MBeanServerConnection connection = connector.getMBeanServerConnection();
//
//            ObjectName brokerName = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost");
//            ObjectName[] queueMBeans = (ObjectName[]) connection.getAttribute(brokerName, "Queues");
//
//            for (ObjectName queueMBean : queueMBeans) {
//                String name = queueMBean.getKeyProperty("destinationName");
//                if (name.equals(queueName)) {
//                    // Parar a fila
//                    connection.invoke(queueMBean, "stop", new Object[0], new String[0]);
//
//                    // Remover todas as mensagens
//                    connection.invoke(queueMBean, "purge", new Object[0], new String[0]);
//
//                    // Remover a fila
//                    connection.invoke(brokerName, "removeQueue", new Object[]{queueMBean}, new String[]{"javax.management.ObjectName"});
//                }
//            }
//
//            connector.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public int getQueueSizeActiveMQ(String queueName) throws JMSException {
        // Implementar para listar a quantidade de mensagens em uma fila
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
        return 0;
    }
    public void createTopicActiveMQ(String topicName) throws JMSException {
        // Implementar a criação de um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
    public void removeTopicActiveMQ(String topicName) throws JMSException {
        // Implementar a remoção de um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
    public void addClientToTopicActiveMQ(Client client, String topicName) throws JMSException {
        // Implementar a adição de um cliente a um tópico
        configMQ.configActiveMQ();
        configMQ.closeActiveMQ();
    }
}
