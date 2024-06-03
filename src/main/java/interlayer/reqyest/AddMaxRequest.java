package interlayer.reqyest;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

public class AddMaxRequest extends Request{
    public final Movie m;

    public AddMaxRequest(Movie m) {
        super(Commands.addMax);
        this.m = m;
    }
}