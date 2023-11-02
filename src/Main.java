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
            List<String> namesQueues =  broker.getNamesQueues();

            if (!namesQueues.isEmpty()) {
                System.out.println("**********************************");
                System.out.println("\tFILAS EXISTENTES");
                String resultado = namesQueues.stream()
                        .map(s -> "\t\t" + s.replaceAll("\"", ""))
                        .collect(Collectors.joining("\n"));

                System.out.println(resultado);
            }
            System.out.println("----------------------------------");
            System.out.println("\t\t  MENU");
            System.out.println("\t 1 - CRIAR FILA");
            System.out.println("\t 2 - REMOVER FILA");
            System.out.println("\t 3 - Nº DE MENSAGENS NAS FILAS ");
            System.out.println("\t 4 - XXXXXXXXX ");
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
                        String queueName = scanner.next();
                        broker.addQueueRabbitMQ(queueName);
                        break;
                    case 2:
                        System.out.println("NOME DA FILA:");
                        queueName = scanner.next();
                        broker.removeQueueRabbitMQ(queueName);
                        break;
                    case 3:
                        broker.getQueueSSizeRabbitMQ();
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