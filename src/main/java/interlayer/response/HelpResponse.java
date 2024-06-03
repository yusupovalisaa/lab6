package interlayer.response;

import interlayer.utilities.Commands;

public class HelpResponse extends Response {
    public final String helpMessage;

    public HelpResponse(String helpMessage, String error) {
        super(Commands.help, error);
        this.helpMessage = helpMessage;
    }
}
