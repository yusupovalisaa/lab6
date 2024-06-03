package client;

import client.network.TCPClient;
import client.utilities.Run;
import client.utilities.StandartConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Главный класс клиентского приложения.
 */
public class Main {
    private static final int PORT = 23586;
    public static final Logger logger = LogManager.getLogger("ClientLogger");

    public static void main(String[] args) {
        var console = new StandartConsole();
        try {
            var client = new TCPClient(InetAddress.getLocalHost(), PORT);
            var cli = new Run(client, console);

            cli.interactiveMode();
        } catch (IOException e) {
            logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
        }
    }
}