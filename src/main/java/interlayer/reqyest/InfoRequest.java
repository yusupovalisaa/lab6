package interlayer.reqyest;

import interlayer.utilities.Commands;

public class InfoRequest extends Request {
    public InfoRequest() {
        super(Commands.info);
    }
}
