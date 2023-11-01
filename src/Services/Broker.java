package Services;

import Configuration.ConfigMQ;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import org.apache.commons.io.IOUtils;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    public void getQueueSSizeRabbitMQ() throws IOException, TimeoutException {
        try {
            String apiUrl = "http://localhost:15672/api/queues";
            String encodedCredentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());

            // Crie uma URL a partir da String da API
            URL url = new URL(apiUrl);

            // Abra uma conexão HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Defina o método de solicitação (GET, POST, etc.)
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            // Ler a resposta da API inteira de uma só vez
            InputStream inputStream = connection.getInputStream();
            String jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            connection.disconnect();
            // Use o Gson para analisar o JSON em uma lista de objetos
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

            // Extrai a quantidade de mensagens de cada fila
            int quantidade = 0;
            for (JsonElement jsonElement : jsonArray) {
                quantidade += jsonElement.getAsJsonObject().get("messages").getAsInt();
            }
            System.out.println("Existem "+ quantidade +" mensagens pendentes!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTopicRabbitMQ(String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a criação de um tópico
    }

    public void removeTopicRabbitMQ(String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a remoção de um tópico
    }


    public void addClientToTopicRabbitMQ(Client client, String topicName) throws IOException, TimeoutException {
        configMQ.configRabbitMQ();
        configMQ.closeRabbitMQ();
        // Implementar a adição de um cliente a um tópico
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
