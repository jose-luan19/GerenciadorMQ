import Services.Broker;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        Broker broker = new Broker();
        Scanner scanner = new Scanner(System.in);
        String input;

        do {

            printNames(broker.getNamesTopics(), "TOPICS");

            printNames(broker.getNamesQueues(), "FILAS");

            System.out.println("----------------------------------");
            System.out.println("\t\t  MENU");
            System.out.println("\t 1 - CRIAR FILA");
            System.out.println("\t 2 - REMOVER FILA");
            System.out.println("\t 3 - CRIAR TOPIC");
            System.out.println("\t 4 - REMOVER TOPIC");
            System.out.println("\t 5 - Nº DE MENSAGENS NAS FILAS ");
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
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
            }
        } while (!input.equals("0"));
    }

    public static void printNames(List<String> listNames, String typeNames) {
        if (!listNames.isEmpty()) {
            System.out.println("**********************************");
            System.out.println("\t"+ typeNames +" EXISTENTES");
            String resultado = listNames.stream()
                    .map(s -> "\t\t" + s.replaceAll("\"", ""))
                    .collect(Collectors.joining("\n"));

            System.out.println(resultado);
        }
    }
}