package client.commands;

import client.network.TCPClient;
import client.utilities.Check;
import interlayer.response.AddMaxResponse;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.models.Movie;
import interlayer.reqyest.AddMaxRequest;
import client.utilities.StandartConsole;

import java.io.IOException;


/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
 * Работает по полям oscarsCount.
 */
public class AddMax extends Command {
    private final StandartConsole console;
    private final TCPClient client;


    public AddMax(StandartConsole console, TCPClient client) {
        super("addMax", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     *
     * @return успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) {
                throw new WrongAmountOfElementsException();
            }
            console.println("Создание нового фильма:");
            Movie m = Check.RequestMovie(console);

            var response = (AddMaxResponse) client.sendAndReceiveCommand(new AddMaxRequest(m));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            if (!response.isAdded) {
                console.println("Фильм не добавлен, количество оскаров " + m.getOscarsCount() + " не максимально.");
                return true;
            }
            console.println("Фильм успешно добавлен!");
            return true;
        } catch (Check.RequestBreak e) {
            console.printError("Поля фильма не валидны. Фильм не создан!");
            return false;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером!");
            return false;
        } catch (APIException e) {
            console.printError(e.getMessage());
            return false;
        } catch (WrongAmountOfElementsException e) {
            console.printError("Неправильное количество аргументов");
            return false;
        }
    }
}