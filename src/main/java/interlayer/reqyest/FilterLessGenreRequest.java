package interlayer.reqyest;

import interlayer.models.Movie;
import interlayer.models.MovieGenre;
import interlayer.utilities.Commands;

public class FilterLessGenreRequest extends Request{
    public final MovieGenre genre;

    public FilterLessGenreRequest(MovieGenre genre) {
        super(Commands.filter_than_less_genre);
        this.genre = genre;
    }
}
