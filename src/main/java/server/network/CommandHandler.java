package server.network;

import interlayer.reqyest.Request;
import interlayer.response.NoSuchCommandResponse;
import interlayer.response.Response;
import org.apache.commons.lang3.SerializationUtils;
import server.managers.CommandsManager;

import java.io.Serializable;
/**
 * Обработчик команд, который отвечает за выполнение команд, полученных в запросах.
 */
public class CommandHandler {
    private final CommandsManager manager;

    public CommandHandler(CommandsManager manager) {
        this.manager = manager;
    }

    public Response handle(Request request) {
        var command = manager.getCommands().get(request.getName());
        if (command == null) return new NoSuchCommandResponse(request.getName());
        return command.apply(request);
    }
}