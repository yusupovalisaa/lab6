package interlayer.reqyest;

import interlayer.utilities.Commands;

public class RemoveByIDRequest extends Request {
    public final long id;

    public RemoveByIDRequest(long id) {
        super(Commands.remove_by_id);
        this.id = id;
    }
}
