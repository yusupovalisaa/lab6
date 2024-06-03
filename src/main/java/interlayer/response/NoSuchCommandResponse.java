package interlayer.response;

public class NoSuchCommandResponse extends Response {
    public NoSuchCommandResponse(String name) {
        super(name, "No such command");
    }
}