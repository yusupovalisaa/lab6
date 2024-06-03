package server.commands;

import server.managers.CommandsManager;
import interlayer.response.HelpResponse;
import interlayer.response.Response;
import interlayer.reqyest.Request;


/**
 * Команда 'help'. Выводит справку по доступным командам.
 */
public class Help extends Command{
    private final CommandsManager commandsManager;

    public Help(CommandsManager commandsManager) {
        super("help", "вывести справку по доступным командам");
        this.commandsManager = commandsManager;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var helpMessage = new StringBuilder();

        commandsManager.getCommands().values().forEach(command -> {
            helpMessage.append(" %-35s%-1s%n".formatted(command.getName(), command.getDescription()));
        });

        return new HelpResponse(helpMessage.toString(), null);
    }
}

