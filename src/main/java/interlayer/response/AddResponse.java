package interlayer.response;

import interlayer.utilities.Commands;

public class AddResponse extends Response{
    public final int newId;

    public AddResponse(int newId, String error) {
        super(Commands.add, error);
        this.newId = newId;
    }
}