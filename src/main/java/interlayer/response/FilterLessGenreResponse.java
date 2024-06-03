package interlayer.response;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

import java.util.List;

public class FilterLessGenreResponse extends Response{
    public final List<Movie> m;

    public FilterLessGenreResponse(List<Movie> m, String error) {
        super(Commands.addMax, error);
        this.m = m;
    }
}
