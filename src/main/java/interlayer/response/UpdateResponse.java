package interlayer.response;

import interlayer.utilities.Commands;

public class UpdateResponse extends Response {
    public UpdateResponse(String error) {
        super(Commands.update, error);
    }
}
