package Configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Connection connectionRabbitMq;
    private Channel channelRabbitMQ;
    private static final Logger logger = LoggerFactory.getLogger(ConfigMQ.class);


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

            URI uri = new URI(apiUrl);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            InputStream inputStream = connection.getInputStream();
            String jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            connection.disconnect();

            jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

        } catch (IOException e) {
            logger.error("Ocorreu um erro inesperado", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return jsonArray;
    }

    public Channel getChannelRabbitMQ() {
        return channelRabbitMQ;
    }
}
