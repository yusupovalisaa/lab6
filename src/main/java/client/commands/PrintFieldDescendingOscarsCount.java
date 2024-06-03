package client.commands;

import client.network.TCPClient;
import client.utilities.Check;
import client.utilities.StandartConsole;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.models.MovieGenre;
import interlayer.reqyest.FilterLessGenreRequest;
import interlayer.reqyest.PrintFieldDescendingOscarsCountRequest;
import interlayer.response.FilterLessGenreResponse;
import interlayer.response.PrintFieldDescendingOscarsCountResponse;

import java.io.IOException;

/**
 * Команда 'print_field_descending_oscars_count'.
 * Выводит значения поля oscarsCount всех элементов в порядке убывания.
 */
public class PrintFieldDescendingOscarsCount extends Command {
    private final StandartConsole console;
    private final TCPClient client;


    public PrintFieldDescendingOscarsCount(StandartConsole console, TCPClient client) {
        super("print_field_descending_oscars_count", "вывести значения поля oscarsCount всех элементов в порядке убывания");
        this.console = console;
        this.client = client;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            var response = (PrintFieldDescendingOscarsCountResponse) client.sendAndReceiveCommand(new PrintFieldDescendingOscarsCountRequest());

            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.oscars.length == 0) {
                console.println("Коллекция пуста!");
                return true;
            }
            for (var oscar : response.oscars) {
                console.println(oscar);
            }
            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером!");
            return false;
        } catch (APIException e) {
            console.printError(e.getMessage());
            return false;
        }
    }
}