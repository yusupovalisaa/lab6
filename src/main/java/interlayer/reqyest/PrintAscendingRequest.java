package interlayer.reqyest;

import interlayer.utilities.Commands;

public class PrintAscendingRequest extends Request{
    public PrintAscendingRequest() {
        super(Commands.print_ascending);
    }
}
