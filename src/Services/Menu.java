package Services;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class Menu {

    private static final Broker broker = new Broker();
    private static String input;
    private static final Faker faker = new Faker();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Client> listClient = new HashMap<>();

    private static void names(){
        printNames(broker.getNamesTopics(), "TOPICS");

        printNames(broker.getNamesQueues(), "FILAS");

        printNames(new ArrayList<>(listClient.keySet()), "CLIENTES");
    }
    public static void menuServidor() throws IOException, TimeoutException {

        do {

            names();

            System.out.println("\n\t\t  MENU GERAL");
            System.out.println("...................................................");
            System.out.println("\t 1 - CRIAR FILA");
            System.out.println("\t 2 - REMOVER FILA");
            System.out.println("\t 3 - CRIAR TOPIC");
            System.out.println("\t 4 - REMOVER TOPIC");
            System.out.println("\t 5 - Nº DE MENSAGENS NAS FILAS");
            System.out.println("\t 6 - CRIAR CLIENTE");
            System.out.println("\t 7 - ACESSAR CLIENTE");
            System.out.println("\t 0 - SAIR ");
            System.out.print("\t ESCOLHA OPÇÃO: ");

            input = scanner.next();

            try {
                int option = Integer.parseInt(input);
                switch (option) {
                    case 0:
                        System.out.println("VALEUUUUUUU");
                        return;
                    case 1:
                        System.out.println("NOME DA FILA:");
                        input = scanner.next();
                        broker.addQueueRabbitMQ(input);
                        break;
                    case 2:
                        System.out.println("NOME DA FILA:");
                        input = scanner.next();
                        broker.removeQueueRabbitMQ(input);
                        break;
                    case 3:
                        System.out.println("NOME DO TOPIC:");
                        input = scanner.next();
                        broker.createTopicRabbitMQ(input);
                        break;
                    case 4:
                        System.out.println("NOME DA TOPIC:");
                        input = scanner.next();
                        broker.removeTopicRabbitMQ(input);
                        break;
                    case 5:
                        broker.getQueuesMessagesSizeRabbitMQ();
                        break;
                    case 6:
                        menuCliente(createClient());
                        break;
                    case 7:
                        System.out.println("NOME DA CLIENTE:");
                        input = scanner.next();
                        menuCliente(getClient(input));
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
            }
        } while (!input.equals("0"));
    }

    public static Client createClient(){
        String clientName = faker.name().firstName();
        Client newClient = new Client(broker,clientName);
        listClient.put(clientName,newClient);
        System.out.println("\nCLIENTE \""+clientName+"\" CRIADO");
        return newClient;
    }
    public static Client getClient(String clientName){
        Client cliente = listClient.get(clientName);
        if (cliente == null) {
            cliente = new Client(broker, clientName);
            System.out.println("CLIENTE \""+clientName+"\" NÃO EXISTIA, MAS FOI CRIADO");
            listClient.put(clientName, cliente);
        }else {
            System.out.println("CLIENTE \""+clientName+"\"");
        }
        return cliente;
    }

    private static void printNames(List<String> listNames, String typeNames) {
        if (!listNames.isEmpty()) {
            System.out.println("**********************************");
            System.out.println("\t"+ typeNames +" EXISTENTES");
            String resultado = listNames.stream()
                    .map(s -> "\t\t" + s.replaceAll("\"", ""))
                    .collect(Collectors.joining("\n"));

            System.out.println(resultado);
            System.out.println("---------------------------------------");
        }
    }
    private static void menuCliente(Client client)throws IOException, TimeoutException {

        do {
            System.out.println("\n\n\n\t\t  MENU CLIENTE");
            System.out.println("===================================");
            System.out.println("\t 1 - ASSINAR TOPIC");
//            System.out.println("\t 2 - REMOVER FILA");
//            System.out.println("\t 3 - CRIAR TOPIC");
//            System.out.println("\t 4 - REMOVER TOPIC");
            System.out.println("\t 0 - VOLTAR AO MENU GERAL ");
            System.out.print("\t ESCOLHA OPÇÃO: ");
            input = scanner.next();

            try {
                int option = Integer.parseInt(input);
                switch (option) {
                    case 0:
                        System.out.println("VOLTANDO AO MENU GERAL! VALEUUUUUUU");
                        menuServidor();
                    case 1:
                        System.out.println("NOME DO TOPIC: ");
                        String topic = scanner.next();
                        System.out.println("NOME DA FILA PARA BIND: ");
                        String queue = scanner.next();
                        System.out.println("ROUTING KEY: ");
                        String key = scanner.next();
                        broker.addClientToTopicRabbitMQ(topic,queue,key);
                        client.subscribeToTopic(queue);
                        break;
                    case 2:
                        System.out.println("NOME DA FILA:");
                        input = scanner.next();
                        broker.removeQueueRabbitMQ(input);
                        break;
                    case 3:
                        System.out.println("NOME DO TOPIC:");
                        input = scanner.next();
                        broker.createTopicRabbitMQ(input);
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
            }
        } while (!input.equals("0"));
    }
}
