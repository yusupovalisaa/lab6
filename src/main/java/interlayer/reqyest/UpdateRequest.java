package interlayer.reqyest;

import interlayer.models.Movie;
import interlayer.utilities.Commands;

public class UpdateRequest extends Request {
    public final long id;
    public final Movie updatedMovie;

    public UpdateRequest(long id, Movie updatedMovie) {
        super(Commands.update);
        this.id = id;
        this.updatedMovie = updatedMovie;
    }
}
