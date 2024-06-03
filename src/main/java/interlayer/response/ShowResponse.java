package interlayer.response;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

import java.util.List;

public class ShowResponse extends Response {
    public final List<Movie> m;

    public ShowResponse(List<Movie> m, String error) {
        super(Commands.show, error);
        this.m = m;
    }
}
