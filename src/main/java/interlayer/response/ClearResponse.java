package interlayer.response;

import interlayer.utilities.Commands;

public class ClearResponse extends Response {
    public ClearResponse(String error) {
        super(Commands.clear, error);
    }
}