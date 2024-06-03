package interlayer.reqyest;

import interlayer.utilities.Commands;

public class HelpRequest extends Request {
    public HelpRequest() {
        super(Commands.help);
    }
}
