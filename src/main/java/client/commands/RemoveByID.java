package client.commands;


import client.network.TCPClient;
import client.utilities.StandartConsole;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.reqyest.RemoveByIDRequest;
import interlayer.response.RemoveByIdResponse;

import java.io.IOException;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 */
public class RemoveByID extends Command {
    private final StandartConsole console;
    private final TCPClient client;

    public RemoveByID(StandartConsole console, TCPClient client) {
        super("remove_by_id Id", "удалить элемент из коллекции по ID");
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
            if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();
            var id = Integer.parseInt(arguments[1]);

            var response = (RemoveByIdResponse) client.sendAndReceiveCommand(new RemoveByIDRequest(id));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно удален.");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            console.printError("ID должен быть представлен числом!");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}