package interlayer.reqyest;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

public class AddRequest extends Request{
    public final Movie m;

    public AddRequest(Movie m) {
        super(Commands.add);
        this.m = m;
    }
}
