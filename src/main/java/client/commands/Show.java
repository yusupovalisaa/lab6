package client.commands;


import client.network.TCPClient;
import client.utilities.StandartConsole;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.reqyest.ShowRequest;
import interlayer.response.ShowResponse;

import java.io.IOException;


/**
 * Команда 'show'. Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends Command {
    private final StandartConsole console;
    private final TCPClient client;

    public Show(StandartConsole console, TCPClient client) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.client = client;
    }


    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

            var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest());
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.m.isEmpty()) {
                console.println("Коллекция пуста!");
                return true;
            }

            for (var product : response.m) {
                console.println(product + "\n");
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}