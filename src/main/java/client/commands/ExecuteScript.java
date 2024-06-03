package client.commands;

import client.utilities.StandartConsole;

/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 * @author maxbarsukov
 */
public class ExecuteScript extends Command {
    private final StandartConsole console;

    public ExecuteScript(StandartConsole console) {
        super("execute_script file_name", "исполнить скрипт из указанного файла");
        this.console = console;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        console.println("Выполнение скрипта '" + arguments[1] + "'...");
        return true;
    }
}
