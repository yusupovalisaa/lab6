package server.commands;

import interlayer.models.Movie;
import interlayer.reqyest.AddMaxRequest;
import interlayer.reqyest.FilterLessGenreRequest;
import interlayer.reqyest.Request;
import interlayer.response.AddMaxResponse;
import interlayer.response.AddResponse;
import interlayer.response.FilterLessGenreResponse;
import interlayer.response.Response;
import server.managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда 'filter_less_genre'. Выводит элементы, значение поля genre которых меньше заданного.
 */
public class FilterLessGenre extends Command {
    private final CollectionManager collectionManager;

    public FilterLessGenre (CollectionManager collectionManager) {
        super("filter_than_less_genre", "вывести элементы, значение поля genre которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (FilterLessGenreRequest) request;
        var genre = req.genre;
        List<Movie> filterMovies = new ArrayList<>();
        try {
            if (genre != null) {
                for (var id: collectionManager.minGenre(genre)) {
                    if (id != 0) {
                        filterMovies.add(collectionManager.byId(id));
                        collectionManager.save();
                    }
                }
            }
            return new FilterLessGenreResponse(filterMovies, null);
        }
        catch (Exception e){
            return new FilterLessGenreResponse(filterMovies, e.toString());
        }
    }
}

