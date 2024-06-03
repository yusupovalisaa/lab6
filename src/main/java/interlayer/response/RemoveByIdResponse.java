package interlayer.response;

import interlayer.utilities.Commands;

public class RemoveByIdResponse extends Response {
    public RemoveByIdResponse(String error) {
        super(Commands.remove_by_id, error);
    }
}
