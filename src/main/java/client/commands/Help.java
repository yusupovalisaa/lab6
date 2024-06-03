package client.commands;


import client.network.TCPClient;
import client.utilities.StandartConsole;
import interlayer.response.HelpResponse;
import interlayer.reqyest.HelpRequest;

import java.io.IOException;

/**
 * Команда 'help'. Выводит справку по доступным командам.
 */
public class Help extends Command{
    private final StandartConsole console;
    private final TCPClient client;

    public Help(StandartConsole console, TCPClient client) {
        super("help", "вывести справку по доступным командам");
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
            var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest());
            console.print(response.helpMessage);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}