package interlayer.response;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

import java.util.List;

public class PrintAscendingResponse extends Response{
    public final List<Movie> m;

    public PrintAscendingResponse(List<Movie> m, String error) {
        super(Commands.print_ascending, error);
        this.m = m;
    }
}
