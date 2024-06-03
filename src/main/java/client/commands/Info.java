package client.commands;

import client.network.TCPClient;
import client.utilities.StandartConsole;
import interlayer.reqyest.InfoRequest;
import interlayer.response.InfoResponse;

import java.io.IOException;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command{
    private final StandartConsole console;
    private final TCPClient client;

    public Info(StandartConsole console, TCPClient client) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.client = client;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest());
            console.println(response.infoMessage);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}