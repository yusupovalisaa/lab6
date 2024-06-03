package server.managers;

import server.commands.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  Класс управляющий командами.
 */
public class CommandsManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();


    /**
     * Добавляет команду.
     * @param commandName
     * @param command
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     *  Словарь команд.
     * @return commands
     */
    public Map<String, Command> getCommands() {
        return commands;
    }


    /**
     *  Добавляет команду в историю.
     * @param command
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

}
