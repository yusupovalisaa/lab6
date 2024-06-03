package client.commands;

import client.network.TCPClient;
import client.utilities.Check;
import client.utilities.StandartConsole;
import interlayer.exception.APIException;
import interlayer.exception.WrongAmountOfElementsException;
import interlayer.models.MovieGenre;
import interlayer.reqyest.FilterLessGenreRequest;
import interlayer.response.FilterLessGenreResponse;

import java.io.IOException;
/**
 * Команда 'filter_less_genre'. Выводит элементы, значение поля genre которых меньше заданного.
 */
public class FillterLessGenre extends Command{
    private final StandartConsole console;
    private final TCPClient client;


    public FillterLessGenre(StandartConsole console, TCPClient client) {
        super("filter_than_less_genre", "вывести элементы, значение поля genre которых меньше заданного");
        this.console = console;
        this.client = client;
    }
    /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды.
     * @return успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) {
                throw new WrongAmountOfElementsException();
            }
            MovieGenre genre = Check.RequestGenre(console);


            var response = (FilterLessGenreResponse) client.sendAndReceiveCommand(new FilterLessGenreRequest(genre));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            if (response.m.isEmpty()) {
                console.println("Фильмов меньше чем заданный жанр: " + genre + " не существует.");
                return true;
            }
            for (var movie : response.m) {
                console.println(movie + "\n");
            }
            return true;
        } catch (Check.RequestBreak e) {
            console.printError("Ввод некорректный. Повторите.");
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
