import Services.Broker;
import Services.Menu;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        Menu.menuServidor();
    }
}