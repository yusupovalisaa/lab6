package client.commands;


import client.network.TCPClient;
import client.utilities.Check;
import client.utilities.StandartConsole;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.reqyest.UpdateRequest;
import interlayer.response.UpdateResponse;

import java.io.IOException;

/**
 * Команда 'update ID'. Обновляет элемент коллекции по ID.
 */
public class UpdateID extends Command{
    private final StandartConsole console;
    private final TCPClient client;

    public UpdateID(StandartConsole console, TCPClient client) {
        super("update Id", "обновить значение элемента коллекции по ID");
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

            console.println("* Введите данные обновленного продукта:");
            var updatedMovie = (Check.RequestMovie(console));
            System.out.println(updatedMovie);
            var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(id, updatedMovie));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно обновлен.");
            return true;

        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (Check.RequestBreak exception) {
            console.printError("Поля продукта не валидны! Продукт не создан!");
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