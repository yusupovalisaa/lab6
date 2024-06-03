package interlayer.reqyest;

import interlayer.utilities.Commands;

public class ClearRequest extends Request {
    public ClearRequest() {
        super(Commands.clear);
    }
}
