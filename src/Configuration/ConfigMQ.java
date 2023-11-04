package Configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.IOUtils;

import javax.jms.JMSException;
import javax.jms.Session;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

public class ConfigMQ {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private javax.jms.Connection connectionActiveMQ;
    private Session sessionActvieMQ;
    private Connection connectionRabbitMq;
    private Channel channelRabbitMQ;

    public void configActiveMQ() throws JMSException {
        javax.jms.ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connectionActiveMQ = connectionFactory.createConnection("admin", "admin");
        connectionActiveMQ.start();

        sessionActvieMQ = connectionActiveMQ.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void closeActiveMQ() throws JMSException {
        sessionActvieMQ.close();
        connectionActiveMQ.close();
    }

    public void configRabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        connectionRabbitMq = factory.newConnection();
        channelRabbitMQ = connectionRabbitMq.createChannel();
    }

    public void closeRabbitMQ() throws IOException, TimeoutException {
        channelRabbitMQ.close();
        connectionRabbitMq.close();
    }

    public JsonArray requestApiRabbitMQ(String path){
        JsonArray jsonArray = null;
        try {
            String apiUrl = "http://localhost:15672/api/"+ path;
            String encodedCredentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());

            // Crie uma URL a partir da String da API
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();

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

            jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return jsonArray;
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
